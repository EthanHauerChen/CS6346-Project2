package structures;

import java.util.concurrent.locks.*;
import common.IListLikeDataStructure;

import java.util.HashSet;

public class ConcurrentStack implements IListLikeDataStructure {
    Node top;
    Object lock = new Object();
    private class Node {
        private Integer value;
        private Node below;
        public Node(int value, Node below) {
            this.value = value;
            this.below = below;
        }

        public Integer getValue() { return value; }
    }

    public void push(int value) {
        synchronized(lock) {
            if (top != null) {
                Node n = new Node(value, top);
                top = n;
            }
            else {
                top = new Node(value, null);
            }
        }
    }

    public Integer pop() {
        synchronized(lock) {
            Node value = top;
            if (value == null) return null;
            if (top.below == null) top = null;
            else top = top.below;

            return value.getValue();
        }
    }

    public boolean search(int key) { return false; } //no peek operation implemented, so search only exists because this class implements IListLikeDataStructure

    public boolean add(int key) {
        this.push(key);
        return true;
    }

    public boolean remove(int key) { //pop operation does not use a key
        return pop() != null;
    }

    //not required, exists only for convenience during testing
    public void clear() { top = null; }

    public static void main(String[] args) throws InterruptedException {
        ConcurrentStack stack = new ConcurrentStack();

        //test if correct number of ints placed in stack
        Thread[] threads = new Thread[16];
        int NUM_ITERATIONS = 10000;
        for (int i = 0; i < 16; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < NUM_ITERATIONS; j++) {
                    stack.push(0);
                }
            });
        }
        for (int i = 0; i < 16; i++) threads[i].start();
        for (int i = 0; i < 16; i++) threads[i].join();

        int count = 0;
        while(stack.pop() != null) count++;

        System.out.println(count);

        //test if correct number of ints popped from stack. stack should be empty at the end
        stack.clear();
        for (int i = 0; i < NUM_ITERATIONS * threads.length; i++) stack.push(0);
        for (int i = 0; i < 15; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < NUM_ITERATIONS; j++) {
                    stack.pop();
                }
            });
        }
        threads[threads.length-1] = new Thread(() -> {
            for (int i = 0; i < NUM_ITERATIONS-1; i++) stack.pop();
        });
        //at this point, stack should have 1 element left
        for (int i = 0; i < 16; i++) threads[i].start();
        for (int i = 0; i < 16; i++) threads[i].join();

        //should print "true" if num of stack pops is correct
        System.out.println(stack.pop() != null && stack.pop() == null);

        //test if all items were placed in the stack
        HashSet<Integer> itemsInStack = new HashSet<>();
        
        for (int i = 0; i < NUM_ITERATIONS * threads.length; i++) itemsInStack.add(i);
        stack.clear();
        for (int i = 0; i < 16; i++) {
            int tempI = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < NUM_ITERATIONS; j++) {
                    stack.push(tempI * NUM_ITERATIONS + j);
                }
            });
        }
        //at this point, stack should contain all integers from 0 to NUM_ITERATIONS*threads.length
        for (int i = 0; i < 16; i++) threads[i].start();
        for (int i = 0; i < 16; i++) threads[i].join();

        boolean allItemsInStack = true;
        for (int i = 0; i < NUM_ITERATIONS * threads.length; i++) {
            allItemsInStack = itemsInStack.remove(stack.pop());
            if (!allItemsInStack) {
                System.out.println("Item popped off stack that shouldn't exist in the stack");
                break;
            }
        }
        System.out.println("All items on stack are accounted for and stack is empty: " + (allItemsInStack && itemsInStack.isEmpty() && stack.pop() == null));
    }
}
