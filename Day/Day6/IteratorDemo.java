import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.ConcurrentModificationException;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * =====================================================================
 *  Iterator & ListIterator — Complete Demonstration (single class)
 * =====================================================================
 *
 * ---------------------------------------------------------------
 *  Iterator<E>  (java.util.Iterator)
 * ---------------------------------------------------------------
 *  - Universal cursor for ALL Collection types (List, Set, Queue).
 *  - UNIDIRECTIONAL — only forward traversal.
 *  - Methods: hasNext(), next(), remove(), forEachRemaining().
 *  - The ONLY safe way to modify a collection during iteration.
 *
 * ---------------------------------------------------------------
 *  ListIterator<E>  (extends Iterator<E>)
 * ---------------------------------------------------------------
 *  - Available ONLY for List implementations.
 *  - BIDIRECTIONAL — forward AND backward.
 *  - Extra methods: hasPrevious(), previous(), nextIndex(),
 *    previousIndex(), set(E), add(E).
 *  - Cursor sits BETWEEN elements (not on them).
 * =====================================================================
 */
public class IteratorDemo {

    // =================================================================
    //  1. Basic Iterator — forward only
    // =================================================================
    static void demoBasicIterator() {
        System.out.println("========== 1. Basic Iterator (forward only) ==========");

        List<String> colors = new ArrayList<>(List.of("Red", "Green", "Blue", "Yellow"));

        // Cursor starts BEFORE the first element
        Iterator<String> it = colors.iterator();
        while (it.hasNext()) {
            System.out.println("  Visiting: " + it.next());
        }
        System.out.println("After loop, hasNext(): " + it.hasNext());
    }

    // =================================================================
    //  2. Iterator.remove() — safe removal during iteration
    // =================================================================
    static void demoIteratorRemove() {
        System.out.println("\n========== 2. Iterator.remove() ==========");

        List<Integer> nums = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        System.out.println("Original: " + nums);

        Iterator<Integer> it = nums.iterator();
        while (it.hasNext()) {
            if (it.next() % 2 == 0) it.remove();   // SAFE
        }
        System.out.println("After removing evens: " + nums);

        // PITFALL: remove() before next()
        Iterator<Integer> bad = nums.iterator();
        try {
            bad.remove();
        } catch (IllegalStateException e) {
            System.out.println("remove() before next() -> IllegalStateException");
        }

        // PITFALL: remove() twice in a row
        Iterator<Integer> bad2 = nums.iterator();
        bad2.next();
        bad2.remove();
        try {
            bad2.remove();
        } catch (IllegalStateException e) {
            System.out.println("remove() twice in a row -> IllegalStateException");
        }
    }

    // =================================================================
    //  3. forEachRemaining — bulk consume
    // =================================================================
    static void demoForEachRemaining() {
        System.out.println("\n========== 3. Iterator.forEachRemaining ==========");

        List<String> fruits = new ArrayList<>(List.of("Apple", "Banana", "Cherry"));
        Iterator<String> it = fruits.iterator();
        if (it.hasNext()) it.next();            // skip first
        it.forEachRemaining(f -> System.out.println("  Remaining: " + f));
    }

    // =================================================================
    //  4. ListIterator — forward traversal with index
    // =================================================================
    static void demoListIteratorForward() {
        System.out.println("\n========== 4. ListIterator — Forward ==========");

        List<String> cities = new ArrayList<>(List.of("Delhi", "Mumbai", "Chennai"));
        ListIterator<String> lit = cities.listIterator();

        while (lit.hasNext()) {
            int idx = lit.nextIndex();
            String c = lit.next();
            System.out.println("  [" + idx + "] = " + c);
        }
    }

    // =================================================================
    //  5. ListIterator — backward traversal
    // =================================================================
    static void demoListIteratorBackward() {
        System.out.println("\n========== 5. ListIterator — Backward ==========");

        List<String> cities = new ArrayList<>(List.of("Delhi", "Mumbai", "Chennai"));
        ListIterator<String> lit = cities.listIterator(cities.size()); // start at end

        while (lit.hasPrevious()) {
            int idx = lit.previousIndex();
            String c = lit.previous();
            System.out.println("  [" + idx + "] = " + c);
        }
    }

    // =================================================================
    //  6. ListIterator — bidirectional walk in one pass
    // =================================================================
    static void demoListIteratorBidirectional() {
        System.out.println("\n========== 6. ListIterator — Bidirectional ==========");

        List<String> letters = new ArrayList<>(List.of("A", "B", "C", "D"));
        ListIterator<String> lit = letters.listIterator();

        System.out.println("Forward: " + lit.next() + " " + lit.next());
        System.out.println("nextIndex=" + lit.nextIndex()+ ", previousIndex=" + lit.previousIndex());
        System.out.println("Back   : " + lit.previous());
        System.out.println("  nextIndex=" + lit.nextIndex()+ ", previousIndex=" + lit.previousIndex());
    }

    // =================================================================
    //  7. ListIterator.set() — replace during iteration
    // =================================================================
    static void demoListIteratorSet() {
        System.out.println("\n========== 7. ListIterator.set() ==========");

        List<String> names = new ArrayList<>(List.of("alice", "bob", "charlie"));
        System.out.println("Before: " + names);

        ListIterator<String> lit = names.listIterator();
        while (lit.hasNext()) {
            lit.set(lit.next().toUpperCase());
        }
        System.out.println("After : " + names);

        // PITFALL: set() before next()
        try {
            names.listIterator().set("X");
        } catch (IllegalStateException e) {
            System.out.println("set() before next() -> IllegalStateException");
        }
    }

    // =================================================================
    //  8. ListIterator.add() — insert during iteration
    // =================================================================
    static void demoListIteratorAdd() {
        System.out.println("\n========== 8. ListIterator.add() ==========");

        List<String> items = new ArrayList<>(List.of("One", "Three", "Five"));
        System.out.println("Before: " + items);

        ListIterator<String> lit = items.listIterator();
        while (lit.hasNext()) {
            String s = lit.next();
            if (s.equals("One"))   lit.add("Two");
            if (s.equals("Three")) lit.add("Four");
        }
        System.out.println("After : " + items);
    }

    // =================================================================
    //  9. ListIterator starting at a given index
    // =================================================================
    static void demoListIteratorStartAtIndex() {
        System.out.println("\n========== 9. ListIterator starting at index ==========");

        List<String> list = new ArrayList<>(List.of("A", "B", "C", "D", "E"));
        ListIterator<String> lit = list.listIterator(2); // cursor before "C"

        while (lit.hasNext()) {
            System.out.println("  [" + lit.nextIndex() + "] " + lit.next());
        }
    }

    // =================================================================
    //  10. Iterator on LinkedList + descendingIterator()
    // =================================================================
    static void demoOnLinkedList() {
        System.out.println("\n========== 10. Iterator on LinkedList ==========");

        LinkedList<Integer> ll = new LinkedList<>(List.of(10, 20, 30, 40, 50));

        System.out.print("Iterator forward  : ");
        Iterator<Integer> it = ll.iterator();
        while (it.hasNext()) System.out.print(it.next() + " ");
        System.out.println();

        // descendingIterator — unique to LinkedList / Deque
        System.out.print("Descending        : ");
        Iterator<Integer> dit = ll.descendingIterator();
        while (dit.hasNext()) System.out.print(dit.next() + " ");
        System.out.println();

        System.out.print("ListIterator fwd  : ");
        ListIterator<Integer> lit = ll.listIterator();
        while (lit.hasNext()) System.out.print(lit.next() + " ");

        System.out.print("\nListIterator bwd  : ");
        while (lit.hasPrevious()) System.out.print(lit.previous() + " ");
        System.out.println();
    }

    // =================================================================
    //  11. Fail-fast — ConcurrentModificationException
    // =================================================================
    static void demoFailFast() {
        System.out.println("\n========== 11. Fail-Fast Iterator ==========");

        // WRONG: modify list directly during for-each -> CME
        List<String> list = new ArrayList<>(List.of("A", "B", "C", "D"));
        try {
            for (String s : list) {
                if (s.equals("B")) list.add("X");
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("add() during for-each -> ConcurrentModificationException");
        }

        // CORRECT: use Iterator.remove()
        List<String> list2 = new ArrayList<>(List.of("A", "B", "C", "D"));
        Iterator<String> it = list2.iterator();
        while (it.hasNext()) {
            if (it.next().equals("B")) it.remove();
        }
        System.out.println("After Iterator.remove(): " + list2);
    }

    // =================================================================
    //  12. Fail-safe — CopyOnWriteArrayList snapshot semantics
    // =================================================================
    static void demoFailSafe() {
        System.out.println("\n========== 12. Fail-Safe Iterator ==========");

        CopyOnWriteArrayList<String> cow =
                new CopyOnWriteArrayList<>(List.of("A", "B", "C"));

        for (String s : cow) {
            System.out.println("  Visiting: " + s);
            if (s.equals("B")) cow.add("X");   // safe — snapshot
        }
        System.out.println("After loop: " + cow + " (X not visited)");

        // remove() on COW iterator is unsupported
        Iterator<String> it = cow.iterator();
        it.next();
        try {
            it.remove();
        } catch (UnsupportedOperationException e) {
            System.out.println("COW iterator does not support remove()");
        }
    }

    // =================================================================
    //  13. Spliterator — split-and-traverse cursor (Java 8+)
    // =================================================================
    static void demoSpliterator() {
        System.out.println("\n========== 13. Spliterator ==========");

        List<String> list = new ArrayList<>(
                List.of("One", "Two", "Three", "Four", "Five"));

        Spliterator<String> sp = list.spliterator();
        System.out.println("Characteristics: " + sp.characteristics());

        System.out.print("tryAdvance one: ");
        sp.tryAdvance(System.out::println);
        System.out.println("Remaining: " + sp.estimateSize());

        System.out.print("Rest: ");
        sp.forEachRemaining(s -> System.out.print(s + " "));
        System.out.println();

        // trySplit — split the source for parallel work
        Spliterator<String> sp2 = list.spliterator();
        Spliterator<String> half = sp2.trySplit();
        System.out.println("Left half : " + (half != null ? half.estimateSize() : 0));
        System.out.println("Right half: " + sp2.estimateSize());
    }

    // =================================================================
    //  14. Enumeration — legacy pre-Iterator cursor
    // =================================================================
    static void demoEnumeration() {
        System.out.println("\n========== 14. Enumeration (legacy) ==========");

        Vector<String> v = new Vector<>(List.of("Alpha", "Beta", "Gamma"));

        Enumeration<String> e = v.elements();
        System.out.print("Enumeration: ");
        while (e.hasMoreElements()) System.out.print(e.nextElement() + " ");
        System.out.println();

        System.out.print("Iterator   : ");
        Iterator<String> it = v.iterator();
        while (it.hasNext()) System.out.print(it.next() + " ");
        System.out.println();
    }

    // =================================================================
    //  15. Iterating over a Map
    // =================================================================
    static void demoMapIteration() {
        System.out.println("\n========== 15. Map Iteration ==========");

        Map<String, Integer> scores = new java.util.LinkedHashMap<>();
        scores.put("Alice", 90);
        scores.put("Bob",   85);
        scores.put("Carol", 95);

        System.out.println("--- entrySet ---");
        Iterator<Map.Entry<String, Integer>> itEntry = scores.entrySet().iterator();
        while (itEntry.hasNext()) {
            Map.Entry<String, Integer> e = itEntry.next();
            System.out.println("  " + e.getKey() + " -> " + e.getValue());
        }

        System.out.println("--- keySet ---");
        Iterator<String> itKey = scores.keySet().iterator();
        while (itKey.hasNext()) System.out.println("  " + itKey.next());

        System.out.println("--- values ---");
        Iterator<Integer> itVal = scores.values().iterator();
        while (itVal.hasNext()) System.out.println("  " + itVal.next());

        // Safe removal via entrySet iterator
        Iterator<Map.Entry<String, Integer>> rem = scores.entrySet().iterator();
        while (rem.hasNext()) {
            if (rem.next().getValue() < 90) rem.remove();
        }
        System.out.println("After removing < 90: " + scores);
    }

    // =================================================================
    //  16. ConcurrentHashMap — weakly consistent iterator
    // =================================================================
    static void demoConcurrentMapIterator() {
        System.out.println("\n========== 16. ConcurrentHashMap Iterator ==========");

        ConcurrentHashMap<String, Integer> chm = new ConcurrentHashMap<>();
        chm.put("A", 1);
        chm.put("B", 2);
        chm.put("C", 3);

        // Never throws CME; may or may not reflect concurrent modifications
        Iterator<Map.Entry<String, Integer>> it = chm.entrySet().iterator();
        int count = 0;
        while (it.hasNext()) {
            Map.Entry<String, Integer> e = it.next();
            System.out.println("  " + e.getKey() + "=" + e.getValue());
            if (++count == 1) chm.put("D", 4);
        }
        System.out.println("Final map: " + chm);
    }

    // =================================================================
    //  17. Common pitfalls
    // =================================================================
    static void demoPitfalls() {
        System.out.println("\n========== 17. Common Pitfalls ==========");

        // P1: next() past end -> NoSuchElementException
        List<String> l1 = new ArrayList<>(List.of("X"));
        Iterator<String> it1 = l1.iterator();
        it1.next();
        try {
            it1.next();
        } catch (NoSuchElementException e) {
            System.out.println("next() past end -> NoSuchElementException");
        }

        // P2: previous() before start -> NoSuchElementException
        List<String> l2 = new ArrayList<>(List.of("Y"));
        ListIterator<String> lit = l2.listIterator();
        try {
            lit.previous();
        } catch (NoSuchElementException e) {
            System.out.println("previous() before start -> NoSuchElementException");
        }

        // P3: remove() after add() -> IllegalStateException
        List<String> l3 = new ArrayList<>(List.of("Z"));
        ListIterator<String> lit3 = l3.listIterator();
        lit3.next();
        lit3.add("NEW");
        try {
            lit3.remove();
        } catch (IllegalStateException e) {
            System.out.println("remove() after add() -> IllegalStateException");
        }
    }

    // =================================================================
    //  Run all demos (called by IteratorMain)
    // =================================================================
    static void runAll() {
        demoBasicIterator();
        demoIteratorRemove();
        demoForEachRemaining();
        demoListIteratorForward();
        demoListIteratorBackward();
        demoListIteratorBidirectional();
        demoListIteratorSet();
        demoListIteratorAdd();
        demoListIteratorStartAtIndex();
        demoOnLinkedList();
        demoFailFast();
        demoFailSafe();
        demoSpliterator();
        demoEnumeration();
        demoMapIteration();
        demoConcurrentMapIterator();
        demoPitfalls();
    }

    public static void main(String[] args) {
        runAll();
    }
}