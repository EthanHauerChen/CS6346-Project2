package structures;

import java.util.ArrayList;

public class ConcurrentBST {
    private Node root;

    public boolean search(int key) {
        return this.recurseAndSearch(root, key) != null;
    }

    public void add(int key) {
        if (this.root == null) this.root = new Node(key);
        this.recurseAndAdd(root, key);
    }

    public void remove(int key) {
        root = this.recurseAndRemove(root, key);
    }

    public ArrayList<Integer> toArrayList() {
        ArrayList<Integer> list = new ArrayList<>();
        traverse(root, list);
        return list;
    }

    private void traverse(Node current, ArrayList<Integer> list) {
        if (current == null) return;
        traverse(current.left, list);
        list.add(current.key);
        traverse(current.right, list);
    }

    private Node recurseAndSearch(Node current, int key) {
        if (current == null) return null;
        if (current.key == key) return current;

        if (current.key < key) {
            return recurseAndSearch(current.right, key);

        } else {
            return recurseAndSearch(current.left, key);
        }
    }

    private Node recurseAndAdd(Node current, int key) {
        if (current.key < key) {
            if (current.right != null) return recurseAndAdd(current.right, key);
            current.right = new Node(key);
            return current.right;

        } else if (current.key > key) {
            if (current.left != null) return recurseAndAdd(current.left, key);
            current.left = new Node(key);
            return current.left;
        }

        return null;
    }

    private Node recurseAndRemove(Node current, int key) {
        if (current == null) return null;

        if (key < current.key) {
            current.left = recurseAndRemove(current.left, key);

        } else if (key > current.key) {
            current.right = recurseAndRemove(current.right, key);

        } else {
            if (current.left == null) {
                return current.right;

            } else if (current.right == null) {
                return current.left;

            } else {
                Node temp = findMinFromRight(current.right);
                current.key = temp.key;
                current.right = recurseAndRemove(current.right, temp.key);
            }
        }
        return current;
    }

    private Node findMinFromRight(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private static class Node {
        private Node left;
        private Node right;
        private Integer key;

        public Node(Integer key) {
            this.key = key;
        }
    }
}
