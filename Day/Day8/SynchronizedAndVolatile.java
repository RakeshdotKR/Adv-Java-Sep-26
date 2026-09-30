/*
 * Demonstrates synchronized and volatile.
 *
 * synchronized:
 * - Provides mutual exclusion.
 * - Also provides memory visibility around lock acquisition/release.
 *
 * volatile:
 * - Makes updates visible across threads.
 * - Does NOT make compound operations such as count++ atomic.
 */
public class SynchronizedAndVolatile {

    private int synchronizedCounter = 0;

    // A volatile flag is suitable for simple shared state.
    private volatile boolean running = true;

    // Only one thread at a time can execute this method for this object.
    public synchronized void incrementCounter() {
        synchronizedCounter++;
    }

    public void doWork() {
        // Because running is volatile, another thread can see its update.
        while (running) {
            // Simulate work.
        }

        System.out.println("Worker noticed running=false");
    }

    public void stopWork() {
        running = false;
    }

    public static void main(String[] args) throws InterruptedException {

        SynchronizedAndVolatile demo =
                new SynchronizedAndVolatile();

        // Multiple threads safely update the counter.
        Runnable incrementTask = () -> {
            for (int i = 0; i < 10_000; i++) {
                demo.incrementCounter();
            }
        };

        Thread t1 = new Thread(incrementTask, "Counter-1");
        Thread t2 = new Thread(incrementTask, "Counter-2");

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("Counter using synchronized: "
                + demo.synchronizedCounter);

        // Demonstrate volatile visibility.
        Thread worker = new Thread(demo::doWork, "Worker");
        worker.start();

        Thread.sleep(100);

        // Signals the worker to stop.
        demo.stopWork();

        worker.join();

        System.out.println("Volatile flag example completed.");
    }
}
