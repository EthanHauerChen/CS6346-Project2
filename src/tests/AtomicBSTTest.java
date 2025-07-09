package tests;

import structures.ConcurrentBST;

public class AtomicBSTTest extends AbstractTest {
    private ConcurrentBST bst;

    public static void main(String[] args) {
        new AtomicBSTTest().runTests();
    }

    @Override
    public void beforeEach() {
        bst = new ConcurrentBST();
    }

    private void runTests() {
        it("test", () -> {
            bst.add(12);
            bst.add(13);
            bst.add(6);
            bst.add(8);
            bst.add(4);
            System.out.println(bst.toArrayList());
        });
    }
}
