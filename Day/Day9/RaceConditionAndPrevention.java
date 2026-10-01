// PROGRAM 7: RaceConditionAndPrevention.java
// ==============================================================================

/*
 * Demonstrates how synchronization prevents a race condition.
 *
 * count++ is not automatically atomic because it involves:
 * 1. Read
 * 2. Modify
 * 3. Write
 *
 * Two threads can interleave those operations and lose updates.
 */
public class RaceConditionAndPrevention {

    private int safeCounter = 0;

    /*
     * synchronized protects the complete read-modify-write operation.
     */
    public synchronized void incrementSafely() {
        safeCounter++;
    }

    public static void main(String[] args)
            throws InterruptedException {

        RaceConditionAndPrevention demo = new RaceConditionAndPrevention();

        Runnable task = () -> {
            for (int i = 0; i < 10_000; i++) {
                demo.incrementSafely();
            }
        };

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        // Expected: 20,000 because the increment is synchronized.
        System.out.println("Safe counter: " + demo.safeCounter);

        System.out.println("Key point: shared mutable state needs appropriate "+ "coordination.");
    }
}