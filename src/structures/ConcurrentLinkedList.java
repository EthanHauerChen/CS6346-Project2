package structures;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class ConcurrentLinkedList {
    private final Node anchor = new Node(null);



    public String toString() {
        return this.toArrayList().toString();
    }

    public int size() {
        try {
            this.anchor.enterQueueAsReader();
            return this.anchor.size(0);
        } catch (InterruptedException e) { throw new RuntimeException(e); }
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

    public void add(int key) {
        try {
            this.anchor.enterQueueAsWriter();
            this.anchor.recurseAndAdd(key);
        } catch (InterruptedException e) { throw new RuntimeException(e); }
    }

    public void remove(int key) {
        try {
            this.anchor.enterQueueAsWriter();
            this.anchor.recurseAndRemove(key);
        } catch (InterruptedException e) { throw new RuntimeException(e); }
    }

    private static class Node {
        private final Semaphore rwQueue = new Semaphore(Integer.MAX_VALUE);
        private final Semaphore rwMutex = new Semaphore(1);
        private final Semaphore rcMutex = new Semaphore(1);
        private final Integer key;
        private int readCount = 0;
        private Node next = null;

        public Node(Integer key) {
            this.key = key;
        }

        private int size(int count) throws InterruptedException {
            if (this.next == null) return count;
            this.next.enterQueueAsReader();
            this.decrementReadCountAndWait();
            return this.next.size(count + 1);
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

        private void enterQueueAsReader() throws InterruptedException {
            this.rwQueue.acquire();
            this.rwQueue.release();
            this.incrementReadCountAndWait();
        }

        private void enterQueueAsWriter() throws InterruptedException {
            this.rwQueue.acquire();
            this.rwMutex.acquire();
            this.rwQueue.release();
        }

        private <T> T exitRead(T object) throws InterruptedException {
            this.decrementReadCountAndWait();
            return object;
        }

        private <T> T exitWrite(T object) {
            rwMutex.release();
            return object;
        }

        private void incrementReadCountAndWait() throws InterruptedException {
            this.rcMutex.acquire();
            this.readCount++;
            if (readCount == 1) this.rwMutex.acquire();
            this.rcMutex.release();
        }

        private void decrementReadCountAndWait() throws InterruptedException {
            this.rcMutex.acquire();
            this.readCount--;
            if (readCount == 0) this.rwMutex.release();
            this.rcMutex.release();
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
