// PROGRAM 8: DeadlockAvoidance.java
// ==============================================================================

/*
 * Demonstrates a simple strategy for avoiding deadlocks.
 *
 * Deadlock example concept:
 *
 * Thread A: lock A -> waits for B
 * Thread B: lock B -> waits for A
 *
 * Prevention:
 * Always acquire multiple locks in the same global order.
 */
public class DeadlockAvoidance {

    private final Object lockA = new Object();
    private final Object lockB = new Object();

    // Both operations acquire lockA before lockB.
    public void operationOne() {
        synchronized (lockA) {
            synchronized (lockB) {
                System.out.println("Operation one completed.");
            }
        }
    }

    public void operationTwo() {
        synchronized (lockA) {
            synchronized (lockB) {
                System.out.println("Operation two completed.");
            }
        }
    }

    public static void main(String[] args)
            throws InterruptedException {

        DeadlockAvoidance demo = new DeadlockAvoidance();

        Thread t1 = new Thread(demo::operationOne);
        Thread t2 = new Thread(demo::operationTwo);

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        /*
         * Since both methods use the same lock acquisition order,
         * there is no circular lock dependency.
         */
        System.out.println("Both operations completed without deadlock.");
    }
}
