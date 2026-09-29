//Java Collectors & Parallel Streams: Complete Verified Guide
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Demonstrates Java Collectors (groupingBy, partitioningBy, joining)
 * and Parallel Stream basics.
 *
 * FIX APPLIED: IntStream.findFirst() / findAny() return OptionalInt
 * (primitive specialization), NOT Optional<Integer>.
 */
public class CollectorsAndParallelDemo {

    // Immutable domain object (JDK 16+)
    record Product(String name, String category, double price, int stock, boolean inStock) {}

    public static void main(String[] args) {

        // ---------- SOURCE ----------
        List<Product> products = List.of(
                new Product("Laptop",    "Electronics", 1200.00, 15, true),
                new Product("Phone",     "Electronics",  800.00,  0, false),
                new Product("Tablet",    "Electronics",  500.00,  8, true),
                new Product("Desk",      "Furniture",    350.00,  4, true),
                new Product("Chair",     "Furniture",    150.00, 25, true),
                new Product("Lamp",      "Furniture",     45.00,  0, false),
                new Product("Notebook",  "Stationery",     5.00, 100, true),
                new Product("Pen",       "Stationery",     2.00, 500, true),
                new Product("Backpack",  "Accessories",  75.00,  10, true)
        );

        System.out.println("========== 1. groupingBy ==========\n");

        // ============================================================
        // 1a. groupingBy: SIMPLE — group products by category
        // Returns: Map<String, List<Product>>
        // ============================================================
        Map<String, List<Product>> byCategory = products.stream()
                .collect(Collectors.groupingBy(Product::category));

        byCategory.forEach((cat, list) ->
                System.out.println(cat + " -> " +
                        list.stream().map(Product::name).collect(Collectors.toList())));

        // ============================================================
        // 1b. groupingBy with DOWNSTREAM collector — count per category
        // Returns: Map<String, Long>
        // ============================================================
        Map<String, Long> countByCategory = products.stream()
                .collect(Collectors.groupingBy(
                        Product::category,
                        Collectors.counting()));

        System.out.println("\nCount per category: " + countByCategory);

        // ============================================================
        // 1c. groupingBy with TWO-ARG — supply a Map implementation
        // ============================================================
        TreeMap<String, List<Product>> sortedByCategory = products.stream()
                .collect(Collectors.groupingBy(
                        Product::category,
                        TreeMap::new,
                        Collectors.toList()));

        System.out.println("\nSorted categories: " + sortedByCategory.keySet());

        // ============================================================
        // 1d. groupingBy + averagingDouble — average price per category
        // ============================================================
        Map<String, Double> avgPriceByCategory = products.stream()
                .collect(Collectors.groupingBy(
                        Product::category,
                        Collectors.averagingDouble(Product::price)));

        System.out.println("\nAverage price per category: " + avgPriceByCategory);

        // ============================================================
        // 1e. groupingBy + summingDouble — total stock value per category
        // ============================================================
        Map<String, Double> stockValueByCategory = products.stream()
                .collect(Collectors.groupingBy(
                        Product::category,
                        Collectors.summingDouble(p -> p.price() * p.stock())));

        System.out.println("\nStock value per category: " + stockValueByCategory);

        System.out.println("\n========== 2. partitioningBy ==========\n");

        // ============================================================
        // 2a. partitioningBy — split into TRUE / FALSE buckets
        // ============================================================
        Map<Boolean, List<Product>> inStockPartition = products.stream()
                .collect(Collectors.partitioningBy(Product::inStock));

        System.out.println("In-stock product names: " +
                inStockPartition.get(true).stream().map(Product::name).collect(Collectors.toList()));
        System.out.println("Out-of-stock names:     " +
                inStockPartition.get(false).stream().map(Product::name).collect(Collectors.toList()));

        // ============================================================
        // 2b. partitioningBy + counting
        // ============================================================
        Map<Boolean, Long> stockCount = products.stream()
                .collect(Collectors.partitioningBy(
                        Product::inStock,
                        Collectors.counting()));

        System.out.println("\nIn stock:  " + stockCount.get(true));
        System.out.println("Out stock: " + stockCount.get(false));

        // ============================================================
        // 2c. partitioningBy + averagingDouble
        // ============================================================
        Map<Boolean, Double> avgPriceByStock = products.stream()
                .collect(Collectors.partitioningBy(
                        Product::inStock,
                        Collectors.averagingDouble(Product::price)));

        System.out.println("\nAvg price of in-stock:     " + avgPriceByStock.get(true));
        System.out.println("Avg price of out-of-stock: " + avgPriceByStock.get(false));

        System.out.println("\n========== 3. joining ==========\n");

        // ============================================================
        // 3a. joining() — no delimiter
        // ============================================================
        String simpleJoin = products.stream()
                .map(Product::name)
                .collect(Collectors.joining());

        System.out.println("Simple join:   " + simpleJoin);

        // ============================================================
        // 3b. joining(delimiter)
        // ============================================================
        String commaJoin = products.stream()
                .map(Product::name)
                .collect(Collectors.joining(", "));

        System.out.println("Comma join:    " + commaJoin);

        // ============================================================
        // 3c. joining(delimiter, prefix, suffix)
        // ============================================================
        String fancyJoin = products.stream()
                .filter(Product::inStock)
                .map(Product::name)
                .collect(Collectors.joining(" | ", "[ ", " ]"));

        System.out.println("Fancy join:    " + fancyJoin);

        System.out.println("\n========== 4. Parallel Streams ==========\n");

        // ============================================================
        // 4a. Sequential vs parallel — same result, different execution
        // ============================================================
        long sumSeq = IntStream.rangeClosed(1, 10_000_000)
                .asLongStream()
                .sum();

        long sumPar = IntStream.rangeClosed(1, 10_000_000)
                .asLongStream()
                .parallel()
                .sum();

        System.out.println("Sequential sum: " + sumSeq);
        System.out.println("Parallel sum:   " + sumPar);
        System.out.println("Both equal?     " + (sumSeq == sumPar));

        // ============================================================
        // 4b. Which threads are used?
        // ============================================================
        System.out.println("\ncommonPool parallelism: " +
                java.util.concurrent.ForkJoinPool.commonPool().getParallelism());

        Set<String> threadNames = IntStream.range(0, 100)
                .parallel()
                .mapToObj(i -> Thread.currentThread().getName())
                .collect(Collectors.toSet());

        System.out.println("Threads used by parallel stream: " + threadNames);

        // ============================================================
        // 4c. findFirst vs findAny on parallel streams
        //     *** FIX: IntStream returns OptionalInt (primitive) ***
        // ============================================================
        OptionalInt first = IntStream.range(0, 1_000_000)
                .parallel()
                .filter(i -> i > 500_000)
                .findFirst();       // deterministic
        System.out.println("\nfindFirst: " + first.orElse(-1));

        OptionalInt any = IntStream.range(0, 1_000_000)
                .parallel()
                .filter(i -> i > 500_000)
                .findAny();         // may return any match
        System.out.println("findAny:   " + any.orElse(-1));

        // ============================================================
        // 4d. Safe parallel collect — use collect(), NOT shared mutable state
        // ============================================================
        Map<String, Long> parCountByCategory = products.parallelStream()
                .collect(Collectors.groupingBy(
                        Product::category,
                        Collectors.counting()));

        System.out.println("\nParallel grouped count: " + parCountByCategory);

        // ============================================================
        // 4e. forEachOrdered vs forEach
        // ============================================================
        System.out.println("\nforEachOrdered (order preserved):");
        IntStream.range(0, 5)
                .parallel()
                .forEachOrdered(i -> System.out.print(i + " "));
        System.out.println();

        System.out.println("forEach (order NOT guaranteed):");
        IntStream.range(0, 5)
                .parallel()
                .forEach(i -> System.out.print(i + " "));
        System.out.println();

        // ============================================================
        // 4f. reduce on parallel streams — MUST be associative
        // ============================================================
        int parProduct = IntStream.rangeClosed(1, 5)   // 1*2*3*4*5 = 120
                .parallel()
                .reduce(1, (a, b) -> a * b);            // associative → safe
        System.out.println("\nParallel product 1..5: " + parProduct);

        // ============================================================
        // 4g. Concurrent collector
        // ============================================================
        ConcurrentMap<String, List<String>> concurrentByCategory = products.parallelStream()
                .collect(Collectors.groupingByConcurrent(
                        Product::category,
                        Collectors.mapping(Product::name, Collectors.toList())));

        System.out.println("\nConcurrent grouping: " + concurrentByCategory);

        // ============================================================
        // 4h. When NOT to use parallel: small datasets
        // ============================================================
        long t0 = System.nanoTime();
        products.stream().map(Product::name).collect(Collectors.toList());
        long t1 = System.nanoTime();
        products.parallelStream().map(Product::name).collect(Collectors.toList());
        long t2 = System.nanoTime();

        System.out.printf("%nSmall stream seq: %d ns, par: %d ns (parallel often SLOWER here)%n",
                (t1 - t0), (t2 - t1));
    }
}