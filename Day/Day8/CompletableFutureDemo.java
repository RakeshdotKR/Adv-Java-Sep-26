import java.util.concurrent.CompletableFuture;

/*
 * Demonstrates asynchronous pipelines using CompletableFuture.
 */
public class CompletableFutureDemo {

    public static void main(String[] args) {

        // Start an asynchronous computation.
        CompletableFuture<Void> future =
                CompletableFuture.supplyAsync(() -> {
                    System.out.println(
                            "Supplying value on: "
                            + Thread.currentThread().getName());
                    return 10;
                })
                // Transform the previous result.
                .thenApply(value -> value * 2)
                // Consume the final result.
                .thenAccept(result ->
                        System.out.println(
                                "Final result: " + result));

        // Wait for the demonstration pipeline to finish.
        future.join();

        // ------------------------------------------------------------
        // Combining two independent asynchronous computations
        // ------------------------------------------------------------
        CompletableFuture<Integer> first =
                CompletableFuture.supplyAsync(() -> 20);

        CompletableFuture<Integer> second =
                CompletableFuture.supplyAsync(() -> 30);

        CompletableFuture<Integer> combined =
                first.thenCombine(second, Integer::sum);

        System.out.println("Combined result: "
                + combined.join());

        // ------------------------------------------------------------
        // Basic exception recovery
        // ------------------------------------------------------------
        CompletableFuture<Integer> safeFuture =
                CompletableFuture.<Integer>supplyAsync(() -> {
                    throw new IllegalStateException("Something failed");
                })
                .exceptionally(ex -> {
                    System.out.println(
                            "Handled failure: " + ex.getMessage());
                    return -1;
                });

        System.out.println("Recovered result: "
                + safeFuture.join());
    }
}