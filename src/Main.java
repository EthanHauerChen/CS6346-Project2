import common.Tester;
import structures.ConcurrentBST;
import structures.ConcurrentLinkedList;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Tester tester = new Tester(5, 8, 100000, 10000);
        Tester.Result result;

        result = tester.testReadDominated(new ConcurrentLinkedList());
        System.out.println(result.getAverageExecutionTime());

        result = tester.testWriteDominated(new ConcurrentLinkedList());
        System.out.println(result.getAverageExecutionTime());

        result = tester.testReadDominated(new ConcurrentBST());
        System.out.println(result.getAverageExecutionTime());

        result = tester.testWriteDominated(new ConcurrentBST());
        System.out.println(result.getAverageExecutionTime());
    }
}
