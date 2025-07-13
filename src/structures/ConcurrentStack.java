package structures;

import java.util.concurrent.locks.*;
import common.IListLikeDataStructure;

import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class ConcurrentStack implements IListLikeDataStructure {
    Node top;
    Semaphore mutex = new Semaphore(1);
    private class Node {
        private Integer value;
        private Node below;
        public Node(int value, Node below) {
            this.value = value;
            this.below = below;
        }

        public Integer getValue() { return value; }
    }

    public void push(int value) throws InterruptedException {
        mutex.acquire();
        if (top != null) {
            Node n = new Node(value, top);
            top = n;
        }
        else {
            top = new Node(value, null);
        }
        mutex.release();
    }

    public Integer pop() throws InterruptedException {
        mutex.acquire();
        Node value = top;
        if (value == null) {
            mutex.release();
            return null;
        }
        if (top.below == null) top = null;
        else top = top.below;
        mutex.release();

        return value.getValue();
    }

    public boolean search(int key) { return false; } //no peek operation implemented, so search only exists because this class implements IListLikeDataStructure

    public boolean add(int key) {
        try {
            this.push(key);
            return true;
        } catch (InterruptedException e) { return false; }
    }

    public boolean remove(int key) { //pop operation does not use a key
        try {
            return pop() != null;
        } catch (InterruptedException e) { return false; }
    }

    //not required, exists only for convenience during testing
    public void clear() { top = null; }
}
