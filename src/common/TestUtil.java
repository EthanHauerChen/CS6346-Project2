package common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.IntStream;

public class TestUtil {

    public static ArrayList<Integer> getListOfNumbers(int lower, int upper) {
        return new ArrayList<>(IntStream.range(lower, upper).boxed().toList());
    }

    public static <T> ArrayList<T> getShuffledList(ArrayList<T> list) {
        ArrayList<T> shuffledList = new ArrayList<>(list);
        Collections.shuffle(shuffledList);
        return shuffledList;
    }

    public static ArrayList<Integer>[] splitNumbers(ArrayList<Integer> numbers, float proportion) {
        if (proportion < 0 || proportion > 1) throw new RuntimeException("invalid proportion");
        int splitMark = (int) (numbers.size() * proportion);
        ArrayList<Integer> first = new ArrayList<>(numbers.subList(0, splitMark));
        ArrayList<Integer> second = new ArrayList<>(numbers.subList(splitMark, numbers.size()));
        return new ArrayList[]{first, second};
    }

    public static void runThreads(Thread[] threads) {
        for (Thread thread : threads) { thread.start(); }
    }

    public static void joinThreads(Thread[] threads) throws InterruptedException {
        for (Thread thread : threads) { thread.join(); }
    }
}
