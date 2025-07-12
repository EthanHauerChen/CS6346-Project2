package structures;

import common.AbstractConcurrentNode;

import java.util.ArrayList;

public class ConcurrentBST {
    private Node beforeRoot = new Node(null);

    public boolean search(int key) {
        try {
            beforeRoot.enterQueueAsReader();
            return recurseAndSearch(beforeRoot, key) != null;
        } catch (InterruptedException e) { throw new RuntimeException(e); }
    }

    public void add(int key) {
        try {
            beforeRoot.enterQueueAsWriter();
            recurseAndAdd(beforeRoot, key);
        } catch (InterruptedException e) { throw new RuntimeException(e); }
    }

    public void remove(int key) {
        try {
            beforeRoot.enterQueueAsWriter();
            beforeRoot = recurseAndRemove(beforeRoot, key);
        } catch (InterruptedException e) { throw new RuntimeException(e); }
    }

    public ArrayList<Integer> toArrayList() {
        ArrayList<Integer> list = new ArrayList<>();
        traverse(beforeRoot.right, list);
        return list;
    }

    private void traverse(Node current, ArrayList<Integer> list) {
        if (current == null) return;
        traverse(current.left, list);
        list.add(current.key);
        traverse(current.right, list);
    }

    private Node recurseAndSearch(Node current, int key) throws InterruptedException {
        if (current.key == null || current.key < key) { // goes right
            if (current.right == null) return current.exitRead(null);
            current.right.enterQueueAsReader();
            current.decrementReadCountAndWait();
            return recurseAndSearch(current.right, key);
        }

        if (current.key > key) { // goes left
           if (current.left == null) return current.exitRead(null);
           current.left.enterQueueAsReader();
           current.decrementReadCountAndWait();
           return recurseAndSearch(current.left, key);
        }

        return current.exitRead(current);
    }

    private Node recurseAndAdd(Node current, int key) throws InterruptedException {
        if (current.key == null || current.key < key) { // goes right
            if (current.right == null) {
                current.right = new Node(key);
                return current.exitWrite(current.right);
            }
            current.right.enterQueueAsWriter();
            current.rwMutex.release();
            return recurseAndAdd(current.right, key);
        }

        if (current.key > key) { // goes left
            if (current.left == null) {
                current.left = new Node(key);
                return current.exitWrite(current.left);
            }
            current.left.enterQueueAsWriter();
            current.rwMutex.release();
            return recurseAndAdd(current.left, key);
        }

        return current.exitWrite(null);
    }

    private Node recurseAndRemove(Node current, int key) throws InterruptedException {
        if (current == null) return null;

        if (current.key == null || current.key < key) { // goes right
            if (current.right != null) current.right.enterQueueAsWriter();
            current.right = recurseAndRemove(current.right, key);
            return current.exitWrite(current);
        }

        if (current.key > key) { // goes left
            if (current.left != null) current.left.enterQueueAsWriter();
            current.left = recurseAndRemove(current.left, key);
            return current.exitWrite(current);
        }

        //at this point, we know current != null, and that current.key == key

        if (current.left == null) {
            return current.exitWrite(current.right);
        }

        if (current.right == null) {
            return current.exitWrite(current.left);
        }

        // find node with the smallest key in right subtree
        current.right.enterQueueAsReader();
        Node temp = findMinFromRight(current.right);

        // remove node with the smallest key in right subtree
        current.key = temp.key;
        current.right.enterQueueAsWriter();
        current.right = recurseAndRemove(current.right, temp.key);

        return current.exitWrite(current);
    }

    private Node findMinFromRight(Node node) throws InterruptedException {
        while (node.left != null) {
            node.left.enterQueueAsReader();
            node.decrementReadCountAndWait();
            node = node.left;
        }
        node.decrementReadCountAndWait();
        return node;
    }

    private static class Node extends AbstractConcurrentNode {
        protected Node left;
        protected Node right;

        public Node(Integer key) {
            super(key);
        }
    }
}
