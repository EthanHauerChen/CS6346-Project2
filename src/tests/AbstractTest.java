package tests;


public abstract class AbstractTest {

    public abstract void beforeEach();

    public void expect(boolean condition) {
        if (!condition) throw new RuntimeException();
    }

    public void it(String description, Runnable callback) {
        beforeEach();
        System.out.print("TEST: " + description + " > ");

        try {
            callback.run();
            System.out.println("SUCCESS");
        } catch (Exception e) {
            System.out.println("FAILURE");
        }
    }
}
