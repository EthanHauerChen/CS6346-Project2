package structures;

import java.util.concurrent.locks.*;

public class ConcurrentStack {
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
    }
}
