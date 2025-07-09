package tests;

import structures.ConcurrentBST;

import java.util.Arrays;
import java.util.List;

public class AtomicBSTTest extends AbstractTest {
    private ConcurrentBST bst;

    public static void main(String[] args) {
        new AtomicBSTTest().runTests();
    }

    @Override
    public void beforeEach() {
        bst = new ConcurrentBST();
    }

    private void addBagOfNumbersToBST() {
        bst.add(12);
        bst.add(13);
        bst.add(6);
        bst.add(8);
        bst.add(4);
        bst.add(99);
    }

    private void runTests() {
        it("should not allow duplicate insertions", () -> {
            bst.add(12);
            bst.add(12);
            expect(bst.toArrayList().equals(List.of(12)));
        });

        it("should not allow non-existent removals", () -> {
            bst.add(12);
            bst.remove(8);
            expect(bst.toArrayList().equals(List.of(12)));
        });

        it("should be empty if all keys are deleted", () -> {
            bst.add(12);
            bst.add(8);
            bst.remove(12);
            bst.remove(8);
            expect(bst.toArrayList().isEmpty());
        });

        it("should insert items in order of key value", () -> {
            addBagOfNumbersToBST();
            expect(bst.toArrayList().equals(Arrays.asList(4, 6, 8, 12, 13, 99)));
        });

        it("should process deletion of HEAD node without loss of other nodes", () -> {
            addBagOfNumbersToBST();
            bst.remove(4);
            expect(bst.toArrayList().equals(Arrays.asList(6, 8, 12, 13, 99)));
            bst.remove(6);
            expect(bst.toArrayList().equals(Arrays.asList(8, 12, 13, 99)));
        });

        it("should process deletion of INTERMEDIATE node without loss of other nodes", () -> {
            addBagOfNumbersToBST();
            bst.remove(12);
            expect(bst.toArrayList().equals(Arrays.asList(4, 6, 8, 13, 99)));
            bst.remove(8);
            expect(bst.toArrayList().equals(Arrays.asList(4, 6, 13, 99)));
        });

        it("should process deletion of LEAF node without loss of other nodes", () -> {
            addBagOfNumbersToBST();
            bst.remove(8);
            expect(bst.toArrayList().equals(Arrays.asList(4, 6, 12, 13, 99)));
            bst.remove(99);
            expect(bst.toArrayList().equals(Arrays.asList(4, 6, 12, 13)));
        });

        it("should be able to find items in the list", () -> {
            addBagOfNumbersToBST();
            expect(bst.search(4));
            expect(bst.search(6));
            expect(bst.search(8));
            expect(bst.search(12));
            expect(bst.search(13));
            expect(bst.search(99));
        });

        it("should not be able to find items not in the list ", () -> {
            addBagOfNumbersToBST();
            bst.remove(12);
            expect(!bst.search(12));
            bst.remove(13);
            expect(!bst.search(13));
        });
    }
}
