package tests;

import common.TestUtil;
import structures.ConcurrentBST;

import java.util.ArrayList;

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
                ArrayList<Integer> shuffledNumbers = TestUtil.getShuffledNumbers(numbers);

                for (int i = 0; i < NUM_THREADS; i++) {
                    int value = shuffledNumbers.get(i);
                    threads[i] = new Thread(() -> bst.add(value));
                }

                TestUtil.runThreads(threads);
                TestUtil.joinThreads(threads);
                expect(bst.toArrayList().equals(numbers));

            } catch (InterruptedException e) {}
        });

        it("should remove all items from the list", () -> {
           try {
               int NUM_THREADS = 1000;
               Thread[] threads = new Thread[NUM_THREADS];

               ArrayList<Integer> numbers = TestUtil.getListOfNumbers(0, NUM_THREADS);
               ArrayList<Integer> shuffledNumbers = TestUtil.getShuffledNumbers(numbers);

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
               System.out.println(bst.toArrayList());
               expect(bst.toArrayList().equals(new ArrayList<>()));

           } catch (InterruptedException e) {}
        });
    }
}
