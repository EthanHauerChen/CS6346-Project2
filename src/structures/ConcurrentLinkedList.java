package structures;

import common.AbstractConcurrentNode;
import common.IListLikeDataStructure;

import java.util.ArrayList;

public class ConcurrentLinkedList implements IListLikeDataStructure {
    private final Node anchor = new Node(null);

    public String toString() {
        return this.toArrayList().toString();
    }

    public ArrayList<Integer> toArrayList() {
        try {
            this.anchor.enterQueueAsReader();
            return this.anchor.toArrayList(new ArrayList<>());
        } catch (InterruptedException e) { throw new RuntimeException(e); }
    }

    public boolean search(int key) {
        try {
            this.anchor.enterQueueAsReader();
            return this.anchor.recurseAndSearch(key) != null;
        } catch (InterruptedException e) { throw new RuntimeException(e); }
    }

    public boolean add(int key) {
        try {
            this.anchor.enterQueueAsWriter();
            return this.anchor.recurseAndAdd(key) != null;
        } catch (InterruptedException e) { throw new RuntimeException(e); }
    }

    public boolean remove(int key) {
        try {
            this.anchor.enterQueueAsWriter();
            return this.anchor.recurseAndRemove(key) != null;
        } catch (InterruptedException e) { throw new RuntimeException(e); }
    }

    private static class Node extends AbstractConcurrentNode {
        private Node next = null;

        public Node(Integer key) {
            super(key);
        }

        private ArrayList<Integer> toArrayList(ArrayList<Integer> list) throws InterruptedException {
            if (this.next == null) return exitRead(list);
            this.next.enterQueueAsReader();
            list.add(this.next.key);
            this.decrementReadCountAndWait();
            return this.next.toArrayList(list);
        }

        private Node recurseAndSearch(int key) throws InterruptedException {
            if (this.next == null) return exitRead(null);
            this.next.enterQueueAsReader();
            if (this.next.key > key) return exitRead(next.exitRead(null));
            if (this.next.key == key) return exitRead(next.exitRead(this.next));
            this.decrementReadCountAndWait();
            return this.next.recurseAndSearch(key);
        }

        private Node recurseAndAdd(int key) throws InterruptedException {
            if (this.next == null) return this.exitWrite(this.addNext(key));
            this.next.enterQueueAsWriter();
            if (this.next.key > key) return exitWrite(next.exitWrite(this.addBeforeNext(key)));
            if (this.next.key == key) return exitWrite(next.exitWrite(null));
            rwMutex.release();
            return this.next.recurseAndAdd(key);
        }

        private Node recurseAndRemove(int key) throws InterruptedException {
            if (this.next == null) return this.exitWrite(null);
            this.next.enterQueueAsWriter();
            if (this.next.key > key) return exitWrite(next.exitWrite(null));
            if (this.next.key == key) return exitWrite(next.exitWrite(this.removeNext()));
            rwMutex.release();
            return this.next.recurseAndRemove(key);
        }

        private Node addNext(int key) {
            this.next = new Node(key);
            return this.next;
        }

        private Node addBeforeNext(int key) {
            Node newNode = new Node(key);
            newNode.next = this.next;
            this.next = newNode;
            return newNode;
        }

        private Node removeNext() {
            Node nextNode = this.next;
            this.next = nextNode.next;
            return nextNode;
        }
    }
}
