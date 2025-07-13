package tests;

import java.util.ArrayList;
import java.util.HashSet;

import structures.ConcurrentStack;

public class ConcurrentStackTest extends AbstractTest {
    private ConcurrentStack stack;
    
    @Override
    public void beforeEach() {
        stack = new ConcurrentStack();
    }

    public static void main(String[] args) {
        new ConcurrentStackTest().runTests();
    }

    public void runTests() {
        Thread[] threads = new Thread[8];
        int NUM_ITERATIONS = 10000;

        it("should contain x number of items in stack after x number of pushes", () -> {
            try {
                for (int i = 0; i < threads.length; i++) {
                    threads[i] = new Thread(() -> {
                        for (int j = 0; j < NUM_ITERATIONS; j++) {
                            stack.add(0);
                        }
                    });
                }
                for (int i = 0; i < threads.length; i++) threads[i].start();
                for (int i = 0; i < threads.length; i++) threads[i].join();

                int count = 0;
                while(stack.pop() != null) count++;
                expect(count == NUM_ITERATIONS * threads.length);

            } catch (InterruptedException e) {}
        });

        it("should contain x-n number of items in stack after n number of pops", () -> {
            try {
                for (int i = 0; i < NUM_ITERATIONS * threads.length; i++) stack.push(0);
                for (int i = 0; i < threads.length-1; i++) {
                    threads[i] = new Thread(() -> {
                        for (int j = 0; j < NUM_ITERATIONS; j++) {
                            stack.remove(0);
                        }
                    });
                }
                threads[threads.length-1] = new Thread(() -> {
                    for (int i = 0; i < NUM_ITERATIONS-1; i++) stack.remove(0);
                });
                //at this point, stack should have 1 element left
                for (int i = 0; i < threads.length; i++) threads[i].start();
                for (int i = 0; i < threads.length; i++) threads[i].join();

                //should print "true" if num of stack pops is correct
                expect(stack.pop() != null && stack.pop() == null);
            } catch (InterruptedException e) {}
        });

        it("should contain all items pushed onto stack, and should not contain items in the stack that were not pushed onto stack", () -> {
            try {
                HashSet<Integer> itemsInStack = new HashSet<>();
                
                for (int i = 0; i < NUM_ITERATIONS * threads.length; i++) itemsInStack.add(i);
                stack.clear();
                for (int i = 0; i < threads.length; i++) {
                    int tempI = i;
                    threads[i] = new Thread(() -> {
                        for (int j = 0; j < NUM_ITERATIONS; j++) {
                            stack.add(tempI * NUM_ITERATIONS + j);
                        }
                    });
                }
                //at this point, stack should contain all integers from 0 to NUM_ITERATIONS*threads.length
                for (int i = 0; i < threads.length; i++) threads[i].start();
                for (int i = 0; i < threads.length; i++) threads[i].join();

                boolean allItemsInStack = true;
                for (int i = 0; i < NUM_ITERATIONS * threads.length; i++) {
                    allItemsInStack = itemsInStack.remove(stack.pop());
                    if (!allItemsInStack)
                        break;
                }
                expect(allItemsInStack && itemsInStack.isEmpty() && stack.pop() == null);
            } catch (InterruptedException e) {}
        });
    }
}
