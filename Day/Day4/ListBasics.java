import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

/**
 * Demonstrates the basic List implementations in Java:
 *   - ArrayList   : Resizable array, fast random access, slow inserts in middle
 *   - LinkedList  : Doubly-linked list, fast inserts/deletes, slow random access
 *   - Vector      : Legacy, synchronized (thread-safe) version of ArrayList
 *
 * The List interface guarantees ORDERED, INDEXED, and ALLOW-DUPLICATES behavior.
 */
public class ListBasics {

    /** Demonstrates ArrayList behavior. */
    public static void demonstrateArrayList() {
        System.out.println("========== ArrayList ==========");

        // ArrayList is backed by a dynamic array. Default initial capacity = 10.
        // Generic type parameter <String> ensures type-safety.
        List<String> fruits = new ArrayList<>();

        // add(E) appends to the end — O(1) amortized
        fruits.add("Apple");
        fruits.add("Banana");
        fruits.add("Cherry");

        // add(int index, E) inserts at position, shifting elements right — O(n)
        fruits.add(1, "Blueberry");
        System.out.println("After adds: " + fruits);

        // get(int) is O(1) because of array backing
        System.out.println("Element at index 2: " + fruits.get(2));

        // set(int, E) replaces element at given index
        fruits.set(0, "Avocado");
        System.out.println("After set index 0: " + fruits);

        // remove(int) removes by INDEX; remove(Object) removes by VALUE
        fruits.remove(1);              // removes "Blueberry"
        fruits.remove("Cherry");       // removes by value
        System.out.println("After removals: " + fruits);

        // contains uses equals() — O(n)
        System.out.println("Contains 'Apple'? " + fruits.contains("Apple"));

        // size() returns number of elements
        System.out.println("Size: " + fruits.size());

        // subList returns a VIEW (backed by original list) — modifications reflect
        List<String> sub = fruits.subList(0, 1);
        System.out.println("Sublist(0,1): " + sub);

        // clear() removes all elements
        fruits.clear();
        System.out.println("After clear, isEmpty? " + fruits.isEmpty());
    }

    /** Demonstrates LinkedList behavior. */
    public static void demonstrateLinkedList() {
        System.out.println("\n========== LinkedList ==========");

        // LinkedList implements both List and Deque interfaces.
        // Internally a doubly-linked list — no array resizing, but no fast index access.
        LinkedList<Integer> numbers = new LinkedList<>();

        numbers.add(10);
        numbers.add(20);
        numbers.add(30);
        System.out.println("Initial: " + numbers);

        // LinkedList-specific methods (from Deque):
        numbers.addFirst(5);      // O(1)
        numbers.addLast(40);      // O(1)
        System.out.println("After addFirst/addLast: " + numbers);

        // peek/getFirst returns head without removing
        System.out.println("First element: " + numbers.getFirst());
        System.out.println("Last element : " + numbers.getLast());

        // removeFirst/removeLast remove head/tail in O(1)
        numbers.removeFirst();
        numbers.removeLast();
        System.out.println("After removeFirst/removeLast: " + numbers);

        // get(int) is O(n) for LinkedList (unlike ArrayList)
        System.out.println("Element at index 1 (O(n)): " + numbers.get(1));

        // LinkedList can also be used as a Queue (FIFO) or Stack (LIFO)
        numbers.offer(99);   // adds to tail (queue-style)
        System.out.println("After offer(99): " + numbers);
        System.out.println("Poll (removes head): " + numbers.poll());
        System.out.println("After poll: " + numbers);
    }

    /** Demonstrates Vector (legacy, synchronized). */
    public static void demonstrateVector() {
        System.out.println("\n========== Vector ==========");

        // Vector is synchronized — every method is thread-safe, but slower.
        // Prefer ArrayList for single-threaded; prefer CopyOnWriteArrayList
        // or Collections.synchronizedList for modern multi-threaded use.
        Vector<Double> prices = new Vector<>();

        prices.add(19.99);
        prices.add(5.49);
        prices.add(100.00);
        System.out.println("Vector: " + prices);

        // capacity() is unique to Vector — shows internal array size
        System.out.println("Capacity: " + prices.capacity()); // default 10
        System.out.println("Size    : " + prices.size());

        // elementAt() is the legacy equivalent of get()
        System.out.println("elementAt(1): " + prices.elementAt(1));

        // firstElement / lastElement
        System.out.println("First: " + prices.firstElement());
        System.out.println("Last : " + prices.lastElement());
    }

    /**
     * Compares ArrayList vs LinkedList performance on indexed access.
     * (Simple illustration — not a rigorous benchmark.)
     */
    public static void comparePerformance() {
        System.out.println("\n========== ArrayList vs LinkedList (get) ==========");

        List<Integer> arrayList = new ArrayList<>();
        List<Integer> linkedList = new LinkedList<>();

        for (int i = 0; i < 100_000; i++) {
            arrayList.add(i);
            linkedList.add(i);
        }

        long start = System.nanoTime();
        for (int i = 0; i < 100_000; i++) arrayList.get(i);
        long arrayTime = System.nanoTime() - start;

        start = System.nanoTime();
        for (int i = 0; i < 100_000; i++) linkedList.get(i);
        long linkedTime = System.nanoTime() - start;

        System.out.println("ArrayList  get loop: " + arrayTime / 1_000_000 + " ms");
        System.out.println("LinkedList get loop: " + linkedTime / 1_000_000 + " ms");
        System.out.println("(ArrayList is typically much faster for random access)");
    }

    public static void main(String[] args) {
        demonstrateArrayList();
        demonstrateLinkedList();
        demonstrateVector();
        comparePerformance();
    }
}