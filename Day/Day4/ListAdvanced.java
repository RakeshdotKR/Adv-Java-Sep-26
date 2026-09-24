import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Demonstrates advanced List topics:
 *   - Stack (LIFO, legacy)
 *   - CopyOnWriteArrayList (thread-safe, best for read-heavy)
 *   - Sorting with Comparable / Comparator
 *   - Iteration: for-each, Iterator, ListIterator
 *   - Immutable lists via List.of()
 */
public class ListAdvanced {

    /** Demonstrates Stack — LIFO structure extending Vector. */
    public static void demonstrateStack() {
        System.out.println("========== Stack ==========");

        // Stack extends Vector, so it inherits all Vector methods.
        // Prefer ArrayDeque for modern stack usage — but Stack is shown here for completeness.
        Stack<String> stack = new Stack<>();

        stack.push("Bottom");
        stack.push("Middle");
        stack.push("Top");
        System.out.println("Stack: " + stack);

        // peek returns top without removing
        System.out.println("Peek  : " + stack.peek());

        // pop removes and returns top
        System.out.println("Pop   : " + stack.pop());
        System.out.println("After pop: " + stack);

        // search returns 1-based distance from top, or -1 if not found
        System.out.println("Search 'Bottom': " + stack.search("Bottom"));
        System.out.println("Search 'Top'   : " + stack.search("Top")); // already popped -> -1
    }

    /** Demonstrates CopyOnWriteArrayList — thread-safe for read-heavy workloads. */
    public static void demonstrateCopyOnWrite() {
        System.out.println("\n========== CopyOnWriteArrayList ==========");

        // Every mutation creates a fresh copy of the internal array.
        // Great when reads >> writes (e.g., listener lists).
        // Iterators never throw ConcurrentModificationException.
        CopyOnWriteArrayList<String> listeners = new CopyOnWriteArrayList<>();
        listeners.add("Listener-A");
        listeners.add("Listener-B");
        listeners.add("Listener-C");

        // Simulating a read while another thread modifies
        for (String listener : listeners) {
            System.out.println("Notifying: " + listener);
            if (listener.equals("Listener-A")) {
                // Safe: iterator works on a snapshot, not the live list
                listeners.add("Listener-D");
            }
        }
        System.out.println("After iteration: " + listeners);
        // Note: Listener-D was NOT visited during the loop (snapshot semantics)
    }

    /** Demonstrates sorting using Comparable and Comparator. */
    public static void demonstrateSorting() {
        System.out.println("\n========== Sorting ==========");

        List<String> names = new ArrayList<>(List.of("Charlie", "Alice", "Bob", "David"));

        // Natural ordering (String implements Comparable)
        Collections.sort(names);
        System.out.println("Natural order: " + names);

        // Custom Comparator — reverse alphabetical
        names.sort(Comparator.reverseOrder());
        System.out.println("Reversed     : " + names);

        // Comparator by length, then natural order as tie-breaker
        names.sort(Comparator.comparingInt(String::length).thenComparing(Comparator.naturalOrder()));
        System.out.println("By length    : " + names);

        // Sorting numbers
        List<Integer> nums = new ArrayList<>(List.of(5, 1, 9, 3, 7));
        Collections.sort(nums);
        System.out.println("Sorted nums  : " + nums);
    }

    /** Demonstrates the different iteration styles over a List. */
    public static void demonstrateIteration() {
        System.out.println("\n========== Iteration Styles ==========");

        List<String> colors = new ArrayList<>(List.of("Red", "Green", "Blue"));

        // 1. Classic for-loop (indexed — only for List)
        System.out.print("Classic for : ");
        for (int i = 0; i < colors.size(); i++) {
            System.out.print(colors.get(i) + " ");
        }
        System.out.println();

        // 2. Enhanced for-each
        System.out.print("For-each    : ");
        for (String c : colors) {
            System.out.print(c + " ");
        }
        System.out.println();

        // 3. Iterator — allows safe removal during iteration
        System.out.print("Iterator    : ");
        Iterator<String> it = colors.iterator();
        while (it.hasNext()) {
            String c = it.next();
            System.out.print(c + " ");
            if (c.equals("Green")) it.remove(); // safe removal
        }
        System.out.println("\nAfter iterator remove: " + colors);

        // 4. ListIterator — bidirectional, supports set() and add()
        System.out.print("ListIterator forward : ");
        ListIterator<String> lit = colors.listIterator();
        while (lit.hasNext()) {
            System.out.print(lit.next() + " ");
        }
        System.out.print("\nListIterator backward: ");
        while (lit.hasPrevious()) {
            System.out.print(lit.previous() + " ");
        }
        System.out.println();

        // 5. forEach with lambda (Java 8+)
        System.out.print("forEach lambda: ");
        colors.forEach(c -> System.out.print(c + " "));
        System.out.println();

        // 6. Stream API (Java 8+)
        System.out.print("Stream       : ");
        colors.stream().forEach(c -> System.out.print(c + " "));
        System.out.println();
    }

    /** Demonstrates creating immutable lists. */
    public static void demonstrateImmutableList() {
        System.out.println("\n========== Immutable Lists ==========");

        // List.of() creates a truly immutable list (Java 9+)
        List<String> immutable = List.of("A", "B", "C");
        System.out.println("Immutable: " + immutable);

        try {
            immutable.add("D"); // will throw
        } catch (UnsupportedOperationException e) {
            System.out.println("Cannot modify List.of() list: " + e.getClass().getSimpleName());
        }

        // Collections.unmodifiableList wraps an existing list (read-only view)
        List<String> mutable = new ArrayList<>(List.of("X", "Y"));
        List<String> readOnly = Collections.unmodifiableList(mutable);
        mutable.add("Z"); // still allowed through original reference
        System.out.println("Read-only view sees update: " + readOnly);

        try {
            readOnly.add("W");
        } catch (UnsupportedOperationException e) {
            System.out.println("Cannot modify unmodifiableList: " + e.getClass().getSimpleName());
        }
    }

    public static void main(String[] args) {
        demonstrateStack();
        demonstrateCopyOnWrite();
        demonstrateSorting();
        demonstrateIteration();
        demonstrateImmutableList();
    }
}