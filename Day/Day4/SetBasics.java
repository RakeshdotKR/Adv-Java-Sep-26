import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Demonstrates the basic Set implementations in Java:
 *   - HashSet        : Unordered, backed by HashMap. Fastest O(1) add/contains.
 *   - LinkedHashSet  : Insertion-ordered (doubly-linked list + hash table).
 *   - TreeSet        : Sorted (Red-Black tree). O(log n) add/contains.
 *
 * The Set interface guarantees NO DUPLICATES and does NOT allow indexed access.
 */
public class SetBasics {

    /** Demonstrates HashSet behavior. */
    public static void demonstrateHashSet() {
        System.out.println("========== HashSet ==========");

        // HashSet uses hashing — order is NOT guaranteed (and may change over time).
        // add() returns true if the element was newly added, false if duplicate.
        Set<String> cities = new HashSet<>();

        System.out.println("add 'Delhi'   -> " + cities.add("Delhi"));    // true
        System.out.println("add 'Mumbai'  -> " + cities.add("Mumbai"));   // true
        System.out.println("add 'Chennai' -> " + cities.add("Chennai"));  // true
        System.out.println("add 'Delhi' again -> " + cities.add("Delhi")); // false — duplicate

        System.out.println("Set content: " + cities);

        // contains() is O(1) average — uses hashCode() + equals()
        System.out.println("Contains 'Mumbai'? " + cities.contains("Mumbai"));
        System.out.println("Contains 'Kolkata'? " + cities.contains("Kolkata"));

        // remove by value
        cities.remove("Chennai");
        System.out.println("After remove 'Chennai': " + cities);

        // size() and isEmpty()
        System.out.println("Size: " + cities.size() + ", isEmpty? " + cities.isEmpty());

        // Null is allowed in HashSet (one null only)
        cities.add(null);
        System.out.println("After adding null: " + cities);
    }

    /** Demonstrates LinkedHashSet — insertion order preserved. */
    public static void demonstrateLinkedHashSet() {
        System.out.println("\n========== LinkedHashSet ==========");

        // LinkedHashSet = HashSet + doubly-linked list preserving insertion order.
        // Slightly slower than HashSet but iteration order is predictable.
        Set<String> languages = new LinkedHashSet<>();

        languages.add("Java");
        languages.add("Python");
        languages.add("Kotlin");
        languages.add("Java");     // ignored — duplicate
        languages.add("Go");

        // Iteration order = insertion order
        System.out.println("Insertion order: " + languages);

        // Re-inserting an existing element does NOT change its position
        languages.add("Python");
        System.out.println("After re-adding Python: " + languages);

        // Removing then re-adding moves it to the end
        languages.remove("Java");
        languages.add("Java");
        System.out.println("After remove + re-add Java: " + languages);
    }

    /** Demonstrates TreeSet — sorted order, NavigableSet operations. */
    public static void demonstrateTreeSet() {
        System.out.println("\n========== TreeSet ==========");

        // TreeSet keeps elements sorted according to natural ordering
        // (elements must implement Comparable) OR a provided Comparator.
        // Does NOT allow null (throws NullPointerException on add).
        TreeSet<Integer> nums = new TreeSet<>();

        nums.add(40);
        nums.add(10);
        nums.add(30);
        nums.add(20);
        nums.add(10);   // duplicate — ignored

        System.out.println("Sorted: " + nums);

        // NavigableSet methods
        System.out.println("first() : " + nums.first());          // smallest
        System.out.println("last()  : " + nums.last());           // largest
        System.out.println("higher(20)  : " + nums.higher(20));   // > 20
        System.out.println("lower(20)   : " + nums.lower(20));    // < 20
        System.out.println("ceiling(25) : " + nums.ceiling(25));  // >= 25
        System.out.println("floor(25)   : " + nums.floor(25));    // <= 25

        // headSet / tailSet / subSet — by default INCLUSIVE of boundary on head/tail
        System.out.println("headSet(30) : " + nums.headSet(30));  // < 30
        System.out.println("tailSet(30) : " + nums.tailSet(30));  // >= 30
        System.out.println("subSet(10,40): " + nums.subSet(10, 40)); // [10,40)

        // pollFirst / pollLast — remove and return
        System.out.println("pollFirst : " + nums.pollFirst());
        System.out.println("pollLast  : " + nums.pollLast());
        System.out.println("After polls: " + nums);

        // descendingSet returns a reverse-order VIEW
        System.out.println("Descending view: " + nums.descendingSet());

        // Try adding null to show it throws (uncomment to test)
        // nums.add(null); // NullPointerException

        // TreeSet with a custom Comparator: reverse order
        TreeSet<String> reverseNames = new TreeSet<>((a, b) -> b.compareTo(a));
        reverseNames.add("Alice");
        reverseNames.add("Charlie");
        reverseNames.add("Bob");
        System.out.println("Reverse-sorted names: " + reverseNames);
    }

    /**
     * Shows how duplicate detection depends on equals() and hashCode().
     */
    public static void demonstrateEqualsHashCode() {
        System.out.println("\n========== equals() / hashCode() contract ==========");

        Set<Person> people = new HashSet<>();
        people.add(new Person("Alice", 30));
        people.add(new Person("Bob", 25));
        people.add(new Person("Alice", 30)); // duplicate — same equals/hashCode

        System.out.println("Set size (expected 2): " + people.size());
        System.out.println("Set content: " + people);
    }

    /** Simple class used to demonstrate equals/hashCode behavior. */
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
        demonstrateHashSet();
        demonstrateLinkedHashSet();
        demonstrateTreeSet();
        demonstrateEqualsHashCode();
    }
}