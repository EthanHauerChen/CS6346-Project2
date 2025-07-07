package structures;

public class ConcurrentStack {
    Node top;
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
        if (top != null) {
            Node n = new Node(value, top);
            top = n;
        }
        else {
            top = new Node(value, null);
        }
    }

    public Integer pop() {
        Node value = top;
        if (top.below == null) top = null;
        else top = top.below;

        return value != null ? value.getValue() : null;
    }

    public static void main(String[] args) {
        ConcurrentStack stack = new ConcurrentStack();
        for (int i = 0; i < 10; i++) {
            stack.push(i);
        }
        for (int i = 0; i < 10; i++) {
            System.out.println(stack.pop());
        }
    }
}
