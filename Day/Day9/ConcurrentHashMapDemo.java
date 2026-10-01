// PROGRAM 5: ConcurrentHashMapDemo.java
// ==============================================================================

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
 * Demonstrates ConcurrentHashMap.
 *
 * It is designed for concurrent access to shared key-value data.
 */
public class ConcurrentHashMapDemo {

    public static void main(String[] args)
            throws InterruptedException {

        ConcurrentHashMap<String, Integer> scores = new ConcurrentHashMap<>();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1_000; i++) {
                scores.merge("Java", 1, Integer::sum);
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1_000; i++) {
                scores.merge("Java", 1, Integer::sum);
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        /*
         * merge() performs the update atomically for the specified key.
         * Therefore the expected value is 2000.
         */
        System.out.println("Java score: " + scores.get("Java"));

        // Other useful atomic operations include compute() and putIfAbsent().
        scores.putIfAbsent("Python", 100);

        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
            System.out.println(
                    entry.getKey() + " -> " + entry.getValue());
        }
    }
}