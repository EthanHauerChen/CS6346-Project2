import common.Tester;
import structures.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Tester tester = new Tester(5, 8, 100000, 100);
        Tester.Result result;
        int totalIterations = 8 * 100000;

        System.out.println("------------KEY SPACE SIZE 10^2------------");
        System.out.println("------------Concurrent Linked List------------");
        result = tester.testReadDominated(new ConcurrentLinkedList());
        System.out.println("average execution time (read dominated) in ms: " + result.getAverageExecutionTime());
        System.out.println("average throughput (read dominated) in operations/ms: " + result.getThroughtput(totalIterations));

        result = tester.testWriteDominated(new ConcurrentLinkedList());
        System.out.println("average execution time (write dominated) in ms: " + result.getAverageExecutionTime());
        System.out.println("average throughput (write dominated) operations/ms: " + result.getThroughtput(totalIterations));

        System.out.println("------------Concurrent Binary Search Tree------------");
        result = tester.testReadDominated(new ConcurrentBST());
        System.out.println("average execution time (read dominated) in ms: " + result.getAverageExecutionTime());
        System.out.println("average throughput (read dominated) operations/ms: " + result.getThroughtput(totalIterations));

        result = tester.testWriteDominated(new ConcurrentBST());
        System.out.println("average execution time (write dominated) in ms: " + result.getAverageExecutionTime());
        System.out.println("average throughput (write dominated) operations/ms: " + result.getThroughtput(totalIterations));

        System.out.println("------------Concurrent Stack------------");
        result = tester.testWriteDominated(new ConcurrentStack());
        System.out.println("average execution time (write dominated) in ms: " + result.getAverageExecutionTime());
        System.out.println("average throughput (write dominated) operations/ms: " + result.getThroughtput(totalIterations));


        tester = new Tester(5, 8, 100000, 10000);

        System.out.println("\n\n------------KEY SPACE SIZE 10^4------------");
        System.out.println("------------Concurrent Linked List------------");
        result = tester.testReadDominated(new ConcurrentLinkedList());
        System.out.println("average execution time (read dominated) in ms: " + result.getAverageExecutionTime());
        System.out.println("average throughput (read dominated) in operations/ms: " + result.getThroughtput(totalIterations));

        result = tester.testWriteDominated(new ConcurrentLinkedList());
        System.out.println("average execution time (write dominated) in ms: " + result.getAverageExecutionTime());
        System.out.println("average throughput (write dominated) operations/ms: " + result.getThroughtput(totalIterations));

        System.out.println("------------Concurrent Binary Search Tree------------");
        result = tester.testReadDominated(new ConcurrentBST());
        System.out.println("average execution time (read dominated) in ms: " + result.getAverageExecutionTime());
        System.out.println("average throughput (read dominated) operations/ms: " + result.getThroughtput(totalIterations));

        result = tester.testWriteDominated(new ConcurrentBST());
        System.out.println("average execution time (write dominated) in ms: " + result.getAverageExecutionTime());
        System.out.println("average throughput (write dominated) operations/ms: " + result.getThroughtput(totalIterations));

        System.out.println("------------Concurrent Stack------------");
        result = tester.testWriteDominated(new ConcurrentStack());
        System.out.println("average execution time (write dominated) in ms: " + result.getAverageExecutionTime());
        System.out.println("average throughput (write dominated) operations/ms: " + result.getThroughtput(totalIterations));
    }
}
