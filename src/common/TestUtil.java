package common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.IntStream;

public class TestUtil {

    public static ArrayList<Integer> getListOfNumbers(int lower, int upper) {
        return new ArrayList<>(IntStream.range(lower, upper).boxed().toList());
    }

    public static ArrayList<Integer> getShuffledNumbers(ArrayList<Integer> numbers) {
        ArrayList<Integer> shuffledNumbers = new ArrayList<>(numbers);
        Collections.shuffle(shuffledNumbers);
        return shuffledNumbers;
    }

    public static void runThreads(Thread[] threads) {
        for (Thread thread : threads) { thread.start(); }
    }

    public static void joinThreads(Thread[] threads) throws InterruptedException {
        for (Thread thread : threads) { thread.join(); }
    }
}
