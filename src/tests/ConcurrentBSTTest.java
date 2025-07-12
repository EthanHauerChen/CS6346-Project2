package tests;

import common.TestUtil;
import structures.ConcurrentBST;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashSet;

public class ConcurrentBSTTest extends AbstractTest {

    private ConcurrentBST bst;

    @Override
    public void beforeEach() {
        bst = new ConcurrentBST();
    }

    public static void main(String[] args) {
        new ConcurrentBSTTest().runTests();
    }

    private void runTests() {
        it("should add all items to the list", () -> {
            try {
                int NUM_THREADS = 5000;
                Thread[] threads = new Thread[NUM_THREADS];

                ArrayList<Integer> numbers = TestUtil.getListOfNumbers(0, NUM_THREADS);
                ArrayList<Integer> shuffledNumbers = TestUtil.getShuffledList(numbers);

                for (int i = 0; i < NUM_THREADS; i++) {
                    int value = shuffledNumbers.get(i);
                    threads[i] = new Thread(() -> bst.add(value));
                }

                TestUtil.runThreads(threads);
                TestUtil.joinThreads(threads);
                expect(bst.toArrayList().equals(numbers));

            } catch (InterruptedException _) {}
        });

        it("should remove all items from the list", () -> {
            try {
                int NUM_THREADS = 1000;
                Thread[] threads = new Thread[NUM_THREADS];

                ArrayList<Integer> numbers = TestUtil.getListOfNumbers(0, NUM_THREADS);
                ArrayList<Integer> shuffledNumbers = TestUtil.getShuffledList(numbers);

                for (int i = 0; i < NUM_THREADS; i++) {
                    int value = numbers.get(i);
                    bst.add(value);
                }

                for (int i = 0; i < NUM_THREADS; i++) {
                    int value = shuffledNumbers.get(i);
                    threads[i] = new Thread(() -> bst.remove(value));
                }

                TestUtil.runThreads(threads);
                TestUtil.joinThreads(threads);
                expect(bst.toArrayList().equals(new ArrayList<>()));

            } catch (InterruptedException _) {}
        });

        it("should have mutually exclusive removals", () -> {
            try {
                int NUM_THREADS = 1000;
                Thread[] threads = new Thread[NUM_THREADS];

                ArrayList<Integer> numbers = TestUtil.getListOfNumbers(0, NUM_THREADS);
                ArrayList<Integer> shuffledNumbers = TestUtil.getShuffledList(numbers);
                HashSet<Integer> leftOverNumbers = new HashSet<>(shuffledNumbers.subList((int) (NUM_THREADS * 0.75), NUM_THREADS));

                for (int i = 0; i < NUM_THREADS; i++) {
                    int value = numbers.get(i);
                    bst.add(value);
                }

                for (int i = 0; i < (int) (NUM_THREADS * 0.75); i++) { //only remove a portion of numbers in bst
                    int value = shuffledNumbers.get(i);
                    threads[i] = new Thread(() -> bst.remove(value));
                }

                TestUtil.runThreads(Arrays.copyOfRange(threads, 0, (int) (NUM_THREADS * 0.75)));
                TestUtil.joinThreads(Arrays.copyOfRange(threads, 0, (int) (NUM_THREADS * 0.75)));

                boolean correctItemsLeftInBST = leftOverNumbers.equals(new HashSet<>(bst.toArrayList()));
                expect(bst.toArrayList().size() == (int) (NUM_THREADS * 0.25) && correctItemsLeftInBST);

            } catch (InterruptedException _) {}
        });
    }
}
