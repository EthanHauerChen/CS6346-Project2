package tests;

import structures.ConcurrentLinkedList;

import java.util.ArrayList;

public class ConcurrentLinkedListTest extends AbstractTest {

    private ConcurrentLinkedList list;

    @Override
    public void beforeEach() {
        list = new ConcurrentLinkedList();
    }

    public static void main(String[] args) {
        new ConcurrentLinkedListTest().runTests();
    }

    private void runTests() {
        it("should add all items to the list", () -> {
            try {
                int NUM_THREADS = 5000;
                Thread[] threads = new Thread[NUM_THREADS];

                for (int i = 0; i < NUM_THREADS; i++) {
                    int key = i + 1;
                    threads[i] = new Thread(() -> list.add(key));
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

                expect(list.toArrayList().equals(expectedList));

            } catch (InterruptedException e) {}
        });
    }
}
