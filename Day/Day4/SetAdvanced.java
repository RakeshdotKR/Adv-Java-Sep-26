import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Demonstrates advanced Set topics:
 *   - EnumSet (specialized, extremely fast for enum keys)
 *   - CopyOnWriteArraySet (thread-safe, read-heavy workloads)
 *   - ConcurrentSkipListSet (thread-safe, sorted)
 *   - Set algebra: union, intersection, difference, subset checks
 *   - Immutable sets via Set.of()
 */
public class SetAdvanced {

    /** Enum representing days of the week for EnumSet demo. */
    enum Day { MON, TUE, WED, THU, FRI, SAT, SUN }

    /** Demonstrates EnumSet — the fastest Set implementation for enum types. */
    public static void demonstrateEnumSet() {
        System.out.println("========== EnumSet ==========");

        // EnumSet is backed by a bit-vector — extremely memory-efficient and fast.
        // All elements must be of the SAME enum type.
        EnumSet<Day> weekdays = EnumSet.of(Day.MON, Day.TUE, Day.WED, Day.THU, Day.FRI);
        EnumSet<Day> weekend = EnumSet.of(Day.SAT, Day.SUN);

        System.out.println("Weekdays: " + weekdays);
        System.out.println("Weekend : " + weekend);

        // allOf / noneOf / range / complementOf
        EnumSet<Day> allDays = EnumSet.allOf(Day.class);
        EnumSet<Day> noDays  = EnumSet.noneOf(Day.class);
        EnumSet<Day> midWeek = EnumSet.range(Day.TUE, Day.THU);
        EnumSet<Day> notWeekend = EnumSet.complementOf(weekend);

        System.out.println("All days      : " + allDays);
        System.out.println("None of       : " + noDays);
        System.out.println("Range TUE-THU : " + midWeek);
        System.out.println("Complement of weekend: " + notWeekend);

        // EnumSet iterates in the enum's declared order — not insertion order
        EnumSet<Day> shuffled = EnumSet.of(Day.FRI, Day.MON, Day.WED);
        System.out.println("Declared-order iteration: " + shuffled);
    }

    /** Demonstrates CopyOnWriteArraySet — thread-safe for read-heavy usage. */
    public static void demonstrateCopyOnWriteArraySet() {
        System.out.println("\n========== CopyOnWriteArraySet ==========");

        // Backed by CopyOnWriteArrayList. Every mutation copies the array.
        // Iterators work on a snapshot and never throw ConcurrentModificationException.
        // Great for listener/subscriber registries; bad for write-heavy workloads.
        CopyOnWriteArraySet<String> subscribers = new CopyOnWriteArraySet<>();
        subscribers.add("Alice");
        subscribers.add("Bob");
        subscribers.add("Charlie");
        subscribers.add("Alice"); // duplicate — ignored

        // Safe iteration while modifying
        for (String s : subscribers) {
            System.out.println("Notifying: " + s);
            if (s.equals("Alice")) {
                subscribers.add("David"); // safe — works on snapshot
            }
        }
        System.out.println("After iteration: " + subscribers);
        // Note: David was NOT visited in the loop above.
    }

    /** Demonstrates ConcurrentSkipListSet — thread-safe, sorted Set. */
    public static void demonstrateConcurrentSkipListSet() {
        System.out.println("\n========== ConcurrentSkipListSet ==========");

        // A skip-list based sorted set — equivalent of TreeSet but thread-safe.
        // Does NOT allow null elements.
        ConcurrentSkipListSet<Integer> scores = new ConcurrentSkipListSet<>();
        scores.add(88);
        scores.add(42);
        scores.add(95);
        scores.add(70);
        scores.add(42); // duplicate ignored

        System.out.println("Sorted concurrent set: " + scores);

        // NavigableSet methods available — headSet, tailSet, higher, lower, etc.
        System.out.println("first(): " + scores.first());
        System.out.println("last() : " + scores.last());
        System.out.println("headSet(80): " + scores.headSet(80));
        System.out.println("tailSet(80): " + scores.tailSet(80));
    }

    /** Demonstrates standard set algebra operations. */
    public static void demonstrateSetAlgebra() {
        System.out.println("\n========== Set Algebra ==========");

        Set<Integer> a = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5));
        Set<Integer> b = new HashSet<>(Arrays.asList(4, 5, 6, 7, 8));

        System.out.println("A = " + a);
        System.out.println("B = " + b);

        // UNION — copy A, then addAll(B)
        Set<Integer> union = new HashSet<>(a);
        union.addAll(b);
        System.out.println("Union       A ∪ B = " + union);

        // INTERSECTION — copy A, then retainAll(B)
        Set<Integer> intersection = new HashSet<>(a);
        intersection.retainAll(b);
        System.out.println("Intersection A ∩ B = " + intersection);

        // DIFFERENCE — copy A, then removeAll(B)
        Set<Integer> difference = new HashSet<>(a);
        difference.removeAll(b);
        System.out.println("Difference  A \\ B = " + difference);

        // SYMMETRIC DIFFERENCE — union minus intersection
        Set<Integer> symDiff = new HashSet<>(a);
        symDiff.addAll(b);
        Set<Integer> temp = new HashSet<>(a);
        temp.retainAll(b);
        symDiff.removeAll(temp);
        System.out.println("Symmetric diff     = " + symDiff);

        // SUBSET check
        Set<Integer> small = new HashSet<>(Arrays.asList(1, 2));
        System.out.println("Is {1,2} subset of A? " + a.containsAll(small));
        System.out.println("Is A subset of B?     " + a.containsAll(b));
    }

    /** Demonstrates iteration options for Set. */
    public static void demonstrateIteration() {
        System.out.println("\n========== Iteration Styles ==========");

        Set<String> colors = new LinkedHashSet<>(Arrays.asList("Red", "Green", "Blue"));

        // No indexed for-loop — Set has no get(int)
        System.out.print("For-each    : ");
        for (String c : colors) System.out.print(c + " ");
        System.out.println();

        System.out.print("Iterator    : ");
        Iterator<String> it = colors.iterator();
        while (it.hasNext()) {
            String c = it.next();
            System.out.print(c + " ");
            if (c.equals("Green")) it.remove(); // safe removal
        }
        System.out.println("\nAfter iterator remove: " + colors);

        System.out.print("forEach λ   : ");
        colors.forEach(c -> System.out.print(c + " "));
        System.out.println();

        System.out.print("Stream      : ");
        colors.stream().forEach(c -> System.out.print(c + " "));
        System.out.println();
    }

    /** Demonstrates immutable sets. */
    public static void demonstrateImmutableSet() {
        System.out.println("\n========== Immutable Sets ==========");

        // Set.of() — truly immutable, rejects duplicates and null (Java 9+)
        Set<String> immutable = Set.of("A", "B", "C");
        System.out.println("Immutable: " + immutable);

        try {
            immutable.add("D");
        } catch (UnsupportedOperationException e) {
            System.out.println("Cannot modify Set.of(): " + e.getClass().getSimpleName());
        }

        // Set.of rejects duplicates at construction time
        try {
            Set.of("X", "X");
        } catch (IllegalArgumentException e) {
            System.out.println("Set.of duplicates -> " + e.getClass().getSimpleName());
        }

        // Collections.unmodifiableSet — read-only VIEW over a live set
        Set<String> mutable = new HashSet<>(Arrays.asList("P", "Q"));
        Set<String> readOnly = Collections.unmodifiableSet(mutable);
        mutable.add("R"); // still allowed through original reference
        System.out.println("Read-only view sees update: " + readOnly);
    }

    public static void main(String[] args) {
        demonstrateEnumSet();
        demonstrateCopyOnWriteArraySet();
        demonstrateConcurrentSkipListSet();
        demonstrateSetAlgebra();
        demonstrateIteration();
        demonstrateImmutableSet();
    }
}