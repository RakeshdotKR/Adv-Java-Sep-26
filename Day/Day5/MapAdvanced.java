// package Day.Day5;

import java.util.Collections;
import java.util.EnumMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Demonstrates advanced Map topics:
 *   - EnumMap          : fastest Map when keys are enum constants
 *   - WeakHashMap      : entries auto-removed when keys are garbage-collected
 *   - IdentityHashMap  : keys compared by reference (==), not equals()
 *   - ConcurrentHashMap: thread-safe, high-concurrency hash map (no null keys/values)
 *   - ConcurrentSkipListMap : thread-safe sorted map
 *   - Hashtable        : legacy synchronized map (prefer ConcurrentHashMap)
 *   - Iteration styles and immutable maps
 */
public class MapAdvanced {

    /** Enum used for EnumMap demo. */
    enum Day { MON, TUE, WED, THU, FRI, SAT, SUN }

    /** Demonstrates EnumMap — bit-vector backed, extremely fast for enum keys. */
    public static void demonstrateEnumMap() {
        System.out.println("========== EnumMap ==========");

        // EnumMap requires the enum class in its constructor.
        // Iteration order follows the enum's DECLARATION order.
        EnumMap<Day, String> schedule = new EnumMap<>(Day.class);
        schedule.put(Day.FRI, "Team lunch");
        schedule.put(Day.MON, "Sprint planning");
        schedule.put(Day.WED, "Code review");
        schedule.put(Day.SUN, "Rest");

        System.out.println("Schedule: " + schedule);

        // Overwrite
        schedule.put(Day.MON, "Standup + planning");
        System.out.println("After update: " + schedule);

        // getOrDefault / containsKey
        System.out.println("getOrDefault(TUE, 'Free'): " + schedule.getOrDefault(Day.TUE, "Free"));
        System.out.println("containsKey(SAT): " + schedule.containsKey(Day.SAT));

        // EnumMap cannot have null keys — but can have null values
        schedule.put(Day.SAT, null);
        System.out.println("After null value for SAT: " + schedule);
    }

    /** Demonstrates WeakHashMap — entries vanish when keys are GC'd. */
    public static void demonstrateWeakHashMap() throws InterruptedException {
        System.out.println("\n========== WeakHashMap ==========");

        // WeakHashMap stores keys WEAKLY. If no other strong reference to a key
        // exists, the entry can be removed automatically by the garbage collector.
        // Useful for caches, metadata holders, listener registries.
        Map<Object, String> weak = new WeakHashMap<>();

        Object strongKey = new Object();
        Object weakKey   = new Object();

        weak.put(strongKey, "Kept because of strong reference");
        weak.put(weakKey,   "Will likely vanish after GC");
        weak.put("literal", "String literals are interned and stay strong");

        System.out.println("Before GC, size: " + weak.size());

        weakKey = null; // drop strong reference so GC can collect it

        System.gc();
        Thread.sleep(100); // give GC a chance to run

        System.out.println("After GC, size (may have shrunk): " + weak.size());
        System.out.println("Content: " + weak);
    }

    /** Demonstrates IdentityHashMap — keys compared by reference identity. */
    public static void demonstrateIdentityHashMap() {
        System.out.println("\n========== IdentityHashMap ==========");

        // IdentityHashMap uses == (reference equality) instead of equals().
        // Used for graph traversals, serialization frameworks, and cases where
        // equal-but-distinct objects must be tracked separately.

        Map<String, Integer> identity = new IdentityHashMap<>();
        String a = new String("hello");
        String b = new String("hello"); // b.equals(a) is true, but a == b is false

        identity.put(a, 1);
        identity.put(b, 2); // treated as a DIFFERENT key

        System.out.println("IdentityHashMap size (expected 2): " + identity.size());
        System.out.println("identity.get(a) = " + identity.get(a));
        System.out.println("identity.get(b) = " + identity.get(b));

        // Compare with HashMap — both strings are treated as the same key
        Map<String, Integer> normal = new LinkedHashMap<>();
        normal.put(a, 1);
        normal.put(b, 2);
        System.out.println("HashMap size (expected 1): " + normal.size() + " -> " + normal);
    }

    /** Demonstrates ConcurrentHashMap — the modern thread-safe map. */
    public static void demonstrateConcurrentHashMap() throws InterruptedException {
        System.out.println("\n========== ConcurrentHashMap ==========");

        // ConcurrentHashMap provides high-concurrency reads and writes
        // (lock-striping, then CAS in Java 8+). Does NOT allow null keys or values.
        ConcurrentHashMap<String, Integer> hits = new ConcurrentHashMap<>();

        hits.put("home", 100);
        hits.put("about", 50);
        hits.put("contact", 25);

        // Atomic compute methods (thread-safe)
        hits.compute("home", (k, v) -> v + 1);
        hits.merge("about", 10, Integer::sum);
        hits.computeIfAbsent("pricing", k -> 0);
        System.out.println("After atomic ops: " + hits);

        // putIfAbsent / replace are atomic
        hits.putIfAbsent("home", 0);         // ignored — already present
        hits.replace("contact", 25, 26);     // replace only if current value matches
        System.out.println("After putIfAbsent/replace: " + hits);

        // Demonstrate concurrent writes from two threads
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) hits.merge("counter", 1, Integer::sum);
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) hits.merge("counter", 1, Integer::sum);
        });
        t1.start(); t2.start();
        t1.join();  t2.join();

        // Should be exactly 2000 — atomic and correct
        System.out.println("counter = " + hits.get("counter") + " (expected 2000)");

        // forEach / search / reduce (parallel-friendly bulk operations)
        System.out.println("--- forEach ---");
        hits.forEach((k, v) -> System.out.println("  " + k + " = " + v));

        // Cannot store nulls
        try {
            hits.put("nullValue", null);
        } catch (NullPointerException e) {
            System.out.println("ConcurrentHashMap rejects null values: NullPointerException");
        }
    }

    /** Demonstrates ConcurrentSkipListMap — thread-safe sorted map. */
    public static void demonstrateConcurrentSkipListMap() {
        System.out.println("\n========== ConcurrentSkipListMap ==========");

        // Equivalent of TreeMap but thread-safe and lock-free-ish (skip list).
        // Does not allow null keys or values.
        ConcurrentSkipListMap<Integer, String> leaderboard = new ConcurrentSkipListMap<>();
        leaderboard.put(3, "Charlie");
        leaderboard.put(1, "Alice");
        leaderboard.put(5, "Eve");
        leaderboard.put(2, "Bob");
        leaderboard.put(4, "Dave");

        System.out.println("Sorted leaderboard: " + leaderboard);
        System.out.println("firstEntry()    : " + leaderboard.firstEntry());
        System.out.println("lastEntry()     : " + leaderboard.lastEntry());
        System.out.println("headMap(3)      : " + leaderboard.headMap(3));
        System.out.println("tailMap(3)      : " + leaderboard.tailMap(3));
        System.out.println("descendingMap() : " + leaderboard.descendingMap());
    }

    /** Demonstrates legacy Hashtable — synchronized but slower. */
    public static void demonstrateHashtable() {
        System.out.println("\n========== Hashtable (legacy) ==========");

        // Hashtable is synchronized at method level, disallows null keys/values.
        // Prefer ConcurrentHashMap for new code.
        java.util.Hashtable<String, Integer> table = new java.util.Hashtable<>();
        table.put("One", 1);
        table.put("Two", 2);
        table.put("Three", 3);

        System.out.println("Hashtable: " + table);

        // Enumerate keys/values — legacy iteration style
        java.util.Enumeration<String> keys = table.keys();
        System.out.print("Keys via Enumeration: ");
        while (keys.hasMoreElements()) {
            System.out.print(keys.nextElement() + " ");
        }
        System.out.println();

        try {
            table.put("NullValue", null);
        } catch (NullPointerException e) {
            System.out.println("Hashtable rejects null: NullPointerException");
        }
    }

    /** Demonstrates iteration styles over a Map. */
    public static void demonstrateIteration() {
        System.out.println("\n========== Map Iteration Styles ==========");

        Map<String, Integer> scores = new LinkedHashMap<>();
        scores.put("Alice", 90);
        scores.put("Bob", 85);
        scores.put("Carol", 95);

        // 1. entrySet — most efficient (get key and value at once)
        System.out.println("--- entrySet ---");
        for (Map.Entry<String, Integer> e : scores.entrySet()) {
            System.out.println("  " + e.getKey() + " -> " + e.getValue());
        }

        // 2. keySet — when you only need keys
        System.out.println("--- keySet ---");
        for (String k : scores.keySet()) {
            System.out.println("  " + k);
        }

        // 3. values — when you only need values
        System.out.println("--- values ---");
        for (Integer v : scores.values()) {
            System.out.println("  " + v);
        }

        // 4. Iterator with entrySet — allows safe removal during iteration
        System.out.println("--- Iterator with remove ---");
        Iterator<Map.Entry<String, Integer>> it = scores.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Integer> e = it.next();
            System.out.println("  considering: " + e);
            if (e.getValue() < 90) it.remove(); // remove Bob
        }
        System.out.println("After iterator removal: " + scores);

        // 5. forEach with lambda (Java 8+)
        System.out.println("--- forEach λ ---");
        scores.forEach((k, v) -> System.out.println("  " + k + " => " + v));

        // 6. Stream API
        System.out.println("--- stream ---");
        scores.entrySet().stream()
              .filter(e -> e.getValue() > 90)
              .forEach(e -> System.out.println("  high scorer: " + e.getKey()));
    }

    /** Demonstrates immutable and unmodifiable maps. */
    public static void demonstrateImmutableMap() {
        System.out.println("\n========== Immutable Maps ==========");

        // Map.of() — up to 10 key/value pairs (Java 9+), rejects null and duplicate keys
        Map<String, Integer> immutable = Map.of("A", 1, "B", 2, "C", 3);
        System.out.println("Map.of(): " + immutable);

        try {
            immutable.put("D", 4);
        } catch (UnsupportedOperationException e) {
            System.out.println("Cannot modify Map.of(): UnsupportedOperationException");
        }

        // Map.ofEntries for more pairs
        Map<String, Integer> many = Map.ofEntries(
                Map.entry("X", 10),
                Map.entry("Y", 20),
                Map.entry("Z", 30)
        );
        System.out.println("Map.ofEntries(): " + many);

        // Collections.unmodifiableMap — read-only VIEW over a live map
        Map<String, Integer> mutable = new LinkedHashMap<>();
        mutable.put("P", 1);
        Map<String, Integer> readOnly = Collections.unmodifiableMap(mutable);
        mutable.put("Q", 2); // still allowed through original reference
        System.out.println("Read-only view sees update: " + readOnly);

        try {
            readOnly.put("R", 3);
        } catch (UnsupportedOperationException e) {
            System.out.println("Cannot modify unmodifiableMap: UnsupportedOperationException");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        demonstrateEnumMap();
        demonstrateWeakHashMap();
        demonstrateIdentityHashMap();
        demonstrateConcurrentHashMap();
        demonstrateConcurrentSkipListMap();
        demonstrateHashtable();
        demonstrateIteration();
        demonstrateImmutableMap();
    }
}
