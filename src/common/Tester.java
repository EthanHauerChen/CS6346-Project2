package common;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Tester {
    int numTestRuns;
    int numThreads;
    int iterations;
    int keySpaceSize;

    public Tester(int numTestRuns, int numThreads, int iterations, int keySpaceSize) {
        this.numTestRuns = numTestRuns;
        this.numThreads = numThreads;
        this.iterations = iterations;
        this.keySpaceSize = keySpaceSize;
    }

    public Result testReadDominated(IListLikeDataStructure dataStruct) throws InterruptedException {
        Result result = new Result();
        for (int i = 0; i < numTestRuns; i++) {
            result.runResults.add(new TestRun(90, 5, 5, dataStruct).run());
        }
        return result;
    }

    public Result testWriteDominated(IListLikeDataStructure dataStruct) throws InterruptedException {
        Result result = new Result();
        for (int i = 0; i < numTestRuns; i++) {
            result.runResults.add(new TestRun(0, 50, 50, dataStruct).run());
        }
        return result;
    }

    public static class Result {
        private final ArrayList<TestRun.Result> runResults = new ArrayList<>();

        public long getAverageExecutionTime() {
            return this.runResults.stream().mapToLong((r) -> r.executionTime).sum() / this.runResults.size();
        }
    }

    private class TestRun {
        private final AtomicInteger totalIterations;
        private final int searchProportion;
        private final int addProportion;
        private final int removeProportion;
        IListLikeDataStructure dataStruct;

        public TestRun(int searchProportion, int addProportion, int removeProportion, IListLikeDataStructure dataStruct) {
            this.totalIterations = new AtomicInteger(0);
            this.searchProportion = searchProportion;
            this.addProportion = addProportion;
            this.removeProportion = removeProportion;
            this.dataStruct = dataStruct;
        }

        private static class Result {
            private long executionTime;
        }

        private Result run() throws InterruptedException {
            ArrayList<Integer> numbers = new ArrayList<>();
            ArrayList<Character> operations = new ArrayList<>();
            Thread[] threads = new Thread[numThreads];
            Result result = new Result();
            Random random = new Random();

            int iterXThreads = iterations * numThreads;

            // create shuffled list of numbers (of size iterations * num threads) within keyspace
            for (int i = 0; i < iterXThreads; i++) {
                numbers.add(random.nextInt(keySpaceSize));
            }

            // create shuffled list of search, add, remove operations
            int addStart = searchProportion;
            int removeStart = addStart + addProportion;

            for (int i = 0; i < iterXThreads; i++) {
                int num = random.nextInt(100);

                if (num >= removeStart) {
                    operations.add('r');
                } else if (num >= addStart) {
                    operations.add('a');
                } else {
                    operations.add('s');
                }
            }

            // create threads to iterate through lists
            for (int t = 0; t < numThreads; t++) {
                int threadOffset = t * iterations;
                ArrayList<Integer> finalNumbers = numbers;
                ArrayList<Character> finalOperations = operations;

                threads[t] = new Thread(() -> {
                    for (int i = 0; i < iterations; i++) {
                        if (totalIterations.get() > iterations) return;
                        Integer number = finalNumbers.get(threadOffset + i);
                        Character operation = finalOperations.get(threadOffset + i);

                        switch (operation) {
                            case 's':
                                dataStruct.search(number);
                                break;
                            case 'a':
                                dataStruct.add(number);
                                break;
                            case 'r':
                                dataStruct.remove(number);
                                break;
                        }

                        totalIterations.incrementAndGet();
                    }
                });
            }

            long startTime = System.currentTimeMillis();
            TestUtil.runThreads(threads);
            TestUtil.joinThreads(threads);
            result.executionTime = System.currentTimeMillis() - startTime;
            return result;
        }
    }
}
