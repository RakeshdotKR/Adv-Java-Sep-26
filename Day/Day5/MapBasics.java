import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Demonstrates the basic Map implementations in Java:
 *   - HashMap       : Unordered, O(1) average get/put, backed by hash table.
 *   - LinkedHashMap : Insertion-ordered (or access-ordered) with a doubly-linked list.
 *   - TreeMap       : Sorted by keys (Red-Black tree), O(log n) get/put.
 *
 * The Map interface maps KEYS to VALUES.
 *   - Keys are UNIQUE (no duplicates).
 *   - Values may repeat.
 *   - One null key allowed in HashMap/LinkedHashMap (not in TreeMap/Hashtable).
 */
public class MapBasics {

    /** Demonstrates HashMap behavior. */
    public static void demonstrateHashMap() {
        System.out.println("========== HashMap ==========");

        // HashMap — order of keys is NOT guaranteed.
        // put() returns the PREVIOUS value for that key, or null if none.
        Map<String, Integer> ages = new HashMap<>();

        System.out.println("put Alice=30 -> previous: " + ages.put("Alice", 30));   // null
        System.out.println("put Bob=25   -> previous: " + ages.put("Bob", 25));     // null
        System.out.println("put Carol=28 -> previous: " + ages.put("Carol", 28));   // null
        System.out.println("put Alice=31 -> previous: " + ages.put("Alice", 31));   // 30 (overwritten)

        System.out.println("Map: " + ages);

        // get() returns null if key is absent
        System.out.println("get('Bob')     : " + ages.get("Bob"));
        System.out.println("get('Unknown') : " + ages.get("Unknown"));

        // getOrDefault avoids null checks
        System.out.println("getOrDefault('Unknown', 0): " + ages.getOrDefault("Unknown", 0));

        // containsKey / containsValue
        System.out.println("containsKey('Alice')   : " + ages.containsKey("Alice"));
        System.out.println("containsValue(25)      : " + ages.containsValue(25));

        // putIfAbsent — only inserts if key is not present (or mapped to null)
        ages.putIfAbsent("Bob", 99);       // ignored — Bob exists
        ages.putIfAbsent("Dave", 40);      // inserted
        System.out.println("After putIfAbsent: " + ages);

        // compute / computeIfAbsent / computeIfPresent / merge
        ages.compute("Alice", (k, v) -> v + 1);              // increment
        ages.computeIfAbsent("Eve", k -> 22);                // insert only if absent
        ages.computeIfPresent("Bob", (k, v) -> v * 2);       // update only if present
        ages.merge("Carol", 5, Integer::sum);                // add 5 to existing
        System.out.println("After compute family: " + ages);

        // replace / replaceAll
        ages.replace("Dave", 41);
        System.out.println("After replace('Dave', 41): " + ages);

        // remove(key) returns removed value (or null)
        System.out.println("remove('Eve') -> " + ages.remove("Eve"));
        // remove(key, value) removes only if matches
        System.out.println("remove('Bob', 999) -> " + ages.remove("Bob", 999)); // false — no match
        System.out.println("remove('Bob', 50)  -> " + ages.remove("Bob", 50));  // true — matches
        System.out.println("After removals: " + ages);

        // size / isEmpty / clear
        System.out.println("Size: " + ages.size() + ", isEmpty? " + ages.isEmpty());

        // HashMap allows ONE null key and MULTIPLE null values
        ages.put(null, 100);
        ages.put("NullValue", null);
        System.out.println("After null key/value: " + ages);
    }

    /** Demonstrates LinkedHashMap — insertion order or access order. */
    public static void demonstrateLinkedHashMap() {
        System.out.println("\n========== LinkedHashMap ==========");

        // 1) Insertion-order LinkedHashMap (default)
        Map<String, Integer> insertionOrder = new LinkedHashMap<>();
        insertionOrder.put("One", 1);
        insertionOrder.put("Two", 2);
        insertionOrder.put("Three", 3);
        insertionOrder.put("Four", 4);
        insertionOrder.put("Two", 22); // update does NOT change order
        System.out.println("Insertion order: " + insertionOrder);

        // 2) Access-order LinkedHashMap — get() moves entry to the end.
        // Constructor: new LinkedHashMap<>(initialCapacity, loadFactor, accessOrder=true)
        // Used to build LRU caches.
        LinkedHashMap<String, Integer> lru = new LinkedHashMap<>(16, 0.75f, true);
        lru.put("A", 1);
        lru.put("B", 2);
        lru.put("C", 3);
        lru.put("D", 4);
        System.out.println("Before access: " + lru);

        lru.get("B");   // B moves to the end (most recently used)
        lru.get("A");   // A moves to the end
        System.out.println("After get(B), get(A): " + lru);

        // LRU eviction pattern using removeEldestEntry
        LinkedHashMap<String, Integer> lruCache = new LinkedHashMap<>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, Integer> eldest) {
                return size() > 3; // keep at most 3 entries
            }
        };
        lruCache.put("X", 1);
        lruCache.put("Y", 2);
        lruCache.put("Z", 3);
        lruCache.put("W", 4); // triggers eviction of least-recently-used
        System.out.println("LRU cache (max 3): " + lruCache);
    }

    /** Demonstrates TreeMap — sorted keys and NavigableMap methods. */
    public static void demonstrateTreeMap() {
        System.out.println("\n========== TreeMap ==========");

        // TreeMap keeps keys sorted (natural order OR a provided Comparator).
        // Keys must be mutually Comparable and NON-NULL.
        TreeMap<Integer, String> ranks = new TreeMap<>();
        ranks.put(3, "Bronze");
        ranks.put(1, "Gold");
        ranks.put(2, "Silver");
        ranks.put(5, "Platinum");
        ranks.put(4, "Diamond");
        System.out.println("Sorted by key: " + ranks);

        // NavigableMap methods
        System.out.println("firstKey()    : " + ranks.firstKey());
        System.out.println("lastKey()     : " + ranks.lastKey());
        System.out.println("higherKey(3)  : " + ranks.higherKey(3));   // > 3
        System.out.println("lowerKey(3)   : " + ranks.lowerKey(3));    // < 3
        System.out.println("ceilingKey(3) : " + ranks.ceilingKey(3));  // >= 3
        System.out.println("floorKey(3)   : " + ranks.floorKey(3));    // <= 3

        // headMap / tailMap / subMap — views over ranges
        System.out.println("headMap(3)      : " + ranks.headMap(3));      // keys < 3
        System.out.println("tailMap(3)      : " + ranks.tailMap(3));      // keys >= 3
        System.out.println("subMap(2,5)     : " + ranks.subMap(2, 5));    // [2,5)
        System.out.println("descendingMap() : " + ranks.descendingMap());

        // pollFirstEntry / pollLastEntry — remove and return entry
        System.out.println("pollFirstEntry: " + ranks.pollFirstEntry());
        System.out.println("pollLastEntry : " + ranks.pollLastEntry());
        System.out.println("After polls   : " + ranks);

        // Custom comparator — reverse order by key
        TreeMap<String, Integer> reversed = new TreeMap<>((a, b) -> b.compareTo(a));
        reversed.put("Apple", 1);
        reversed.put("Mango", 2);
        reversed.put("Banana", 3);
        System.out.println("Reverse-sorted keys: " + reversed);
    }

    /**
     * Shows how key identity is decided via equals() and hashCode().
     */
    public static void demonstrateEqualsHashCode() {
        System.out.println("\n========== equals() / hashCode() contract ==========");

        Map<Person, String> roles = new HashMap<>();
        roles.put(new Person("Alice", 30), "Engineer");
        roles.put(new Person("Bob", 25), "Designer");
        roles.put(new Person("Alice", 30), "Manager"); // same key — replaces value

        System.out.println("Map size (expected 2): " + roles.size());
        System.out.println("Map content: " + roles);
    }

    /** Simple class used to demonstrate equals/hashCode behavior for keys. */
    static class Person {
        String name;
        int age;

        Person(String name, int age) { this.name = name; this.age = age; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Person)) return false;
            Person p = (Person) o;
            return age == p.age && name.equals(p.name);
        }

        @Override
        public int hashCode() {
            // Must be consistent with equals()
            return 31 * name.hashCode() + age;
        }

        @Override
        public String toString() { return name + "(" + age + ")"; }
    }

    public static void main(String[] args) {
        demonstrateHashMap();
        demonstrateLinkedHashMap();
        demonstrateTreeMap();
        demonstrateEqualsHashCode();
    }
}