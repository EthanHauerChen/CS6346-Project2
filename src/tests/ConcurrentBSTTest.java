package tests;

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

                for (int i = 0; i < NUM_THREADS; i++) {
                    int key = i + 1;
                    threads[i] = new Thread(() -> bst.add(key));
                }

                for (int i = 0; i < NUM_THREADS; i++) {
                    threads[i].start();
                }

                for (int i = 0; i < NUM_THREADS; i++) {
                    threads[i].join();
                }

                ArrayList<Integer> expectedList = new ArrayList<>();

                for (int i = 0; i < NUM_THREADS; i++) {
                    expectedList.add(i + 1);
                }

                expect(bst.toArrayList().equals(expectedList));

            } catch (InterruptedException e) {}
        });
    }
}
