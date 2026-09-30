// PROGRAM 1: ThreadLifecycleRunnableCallable.java
// ==============================================================================

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/*
 * Demonstrates thread lifecycle basics and Runnable vs Callable.
 */
public class ThreadLifecycleRunnableCallable {

    public static void main(String[] args) throws Exception {

        // Runnable represents work that does not return a value.
        Runnable task = () -> {
            System.out.println("Runnable executing on: "
                    + Thread.currentThread().getName());
        };

        Thread thread = new Thread(task, "Runnable-Thread");

        // NEW: created but not started.
        System.out.println("Before start: " + thread.getState());

        thread.start();
        thread.join();

        // TERMINATED: run() has completed.
        System.out.println("After completion: " + thread.getState());

        // Callable can return a value and throw checked exceptions.
        Callable<Integer> calculation = () -> {
            Thread.sleep(100);
            return 10 * 20;
        };

        /*
         * FutureTask can execute a Callable using a normal Thread
         * and allows the result to be retrieved later.
         */
        FutureTask<Integer> futureTask = new FutureTask<>(calculation);
        Thread callableThread =
                new Thread(futureTask, "Callable-Thread");

        callableThread.start();

        // get() waits until the Callable completes.
        System.out.println("Callable result: " + futureTask.get());
        System.out.println("Callable thread state: "
                + callableThread.getState());
    }
}
