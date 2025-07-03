package tests;

import structures.ConcurrentLinkedList;

import java.util.Arrays;
import java.util.List;

public class AtomicLinkedListTest extends AbstractTest {

    private ConcurrentLinkedList list;

    public static void main(String[] args) {
        new AtomicLinkedListTest().runTests();
    }

    @Override
    public void beforeEach() {
        list = new ConcurrentLinkedList();
    }

    private void addBagOfNumbersToList() {
        list.add(12);
        list.add(24);
        list.add(8);
        list.add(6);
    }

    private void runTests() {
        it("should not allow duplicate insertions", () -> {
            list.add(12);
            list.add(12);
            expect(list.toArrayList().equals(List.of(12)));
        });

        it("should not allow non-existent removals", () -> {
            list.add(12);
            list.remove(8);
            expect(list.toArrayList().equals(List.of(12)));
        });

        it("should be empty if all keys are deleted", () -> {
            list.add(12);
            list.add(8);
            list.remove(12);
            list.remove(8);
            expect(list.toArrayList().isEmpty());
        });

        it("should insert items in order of key value", () -> {
            addBagOfNumbersToList();
            expect(list.toArrayList().equals(Arrays.asList(6, 8, 12, 24)));
        });

        it("should process deletion of HEAD node without loss of other nodes", () -> {
            addBagOfNumbersToList();
            list.remove(6);
            expect(list.toArrayList().equals(Arrays.asList(8, 12, 24)));
            list.remove(8);
            expect(list.toArrayList().equals(Arrays.asList(12, 24)));
        });

        it("should process deletion of INTERMEDIATE node without loss of other nodes", () -> {
            addBagOfNumbersToList();
            list.remove(12);
            expect(list.toArrayList().equals(Arrays.asList(6, 8, 24)));
            list.remove(8);
            expect(list.toArrayList().equals(Arrays.asList(6, 24)));
        });

        it("should process deletion of TAIL node without loss of other nodes", () -> {
            addBagOfNumbersToList();
            list.remove(24);
            expect(list.toArrayList().equals(Arrays.asList(6, 8, 12)));
            list.remove(12);
            expect(list.toArrayList().equals(Arrays.asList(6, 8)));
        });

        it("should be able to find items in the list", () -> {
            addBagOfNumbersToList();
            expect(list.search(6));
            expect(list.search(8));
            expect(list.search(12));
            expect(list.search(24));
        });

        it("should not be able to find items not in the list ", () -> {
            addBagOfNumbersToList();
            list.remove(12);
            expect(!list.search(12));
            list.remove(24);
            expect(!list.search(24));
        });
    }
}
