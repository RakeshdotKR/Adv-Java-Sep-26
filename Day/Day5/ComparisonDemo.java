import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * =====================================================================
 *  Comparable vs Comparator — Complete Single-File Demonstration
 * =====================================================================
 *
 * ---------------------------------------------------------------
 *  Comparable  (java.lang.Comparable)
 * ---------------------------------------------------------------
 *  - Implemented BY the class itself.
 *  - Defines the "natural ordering" of the class.
 *  - Only ONE natural ordering per class (single abstract method compareTo).
 *  - Used automatically by:
 *        Collections.sort(list)
 *        list.sort(null)
 *        new TreeSet<>()  / new TreeMap<>()
 *        Arrays.sort(array)
 *  - Package: java.lang -> no import needed.
 *
 * ---------------------------------------------------------------
 *  Comparator  (java.util.Comparator)
 * ---------------------------------------------------------------
 *  - Implemented by a SEPARATE class, lambda, or method reference.
 *  - Defines an EXTERNAL / alternate ordering.
 *  - You can have MANY comparators for the same class.
 *  - Passed explicitly to sort or TreeSet/TreeMap/PriorityQueue constructors.
 *  - Package: java.util -> import required.
 *  - Has useful static factory methods:
 *        Comparator.comparing(...)
 *        Comparator.comparingInt(...) / comparingDouble(...)
 *        Comparator.naturalOrder() / reverseOrder()
 *        Comparator.nullsFirst() / nullsLast()
 *        Comparator.thenComparing(...)
 *        .reversed()
 * =====================================================================
 */
public class ComparisonDemo {

    // =================================================================
    //  Domain class: Student
    // =================================================================
    /**
     * Domain class used to demonstrate Comparable vs Comparator.
     *
     * Natural ordering (Comparable): by rollNo ascending.
     * Alternates (Comparator constants): by name, marks DESC, chained, etc.
     */
    static class Student implements Comparable<Student> {

        private final int    rollNo;
        private final String name;
        private final double marks;

        Student(int rollNo, String name, double marks) {
            this.rollNo = rollNo;
            this.name   = name;
            this.marks  = marks;
        }

        int    getRollNo() { return rollNo; }
        String getName()   { return name;   }
        double getMarks()  { return marks;  }

        // -------------------------------------------------------------
        // NATURAL ORDERING: by rollNo ascending.
        //
        // Contract of compareTo:
        //   negative  -> this < other
        //   zero      -> this == other
        //   positive  -> this > other
        //
        // Use Integer.compare / Double.compare to avoid overflow bugs
        // (do NOT use "a - b" for ints with large magnitudes).
        // -------------------------------------------------------------
        @Override
        public int compareTo(Student other) {
            return Integer.compare(this.rollNo, other.rollNo);
        }

        // equals/hashCode are for HashMap/HashSet semantics. Kept consistent
        // with compareTo (same key = rollNo) to avoid surprises in TreeSet.
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Student)) return false;
            Student s = (Student) o;
            return rollNo == s.rollNo;
        }

        @Override
        public int hashCode() {
            return Integer.hashCode(rollNo);
        }

        @Override
        public String toString() {
            return String.format("Student[%d, %s, %.1f]", rollNo, name, marks);
        }

        // =============================================================
        // COMPARATOR CONSTANTS — external / alternate orderings.
        // Each is a reusable, named sorting strategy.
        // =============================================================

        /** Sort by name alphabetically (A -> Z). */
        static final Comparator<Student> BY_NAME =
                Comparator.comparing(Student::getName);

        /** Sort by marks, HIGHEST first (descending). */
        static final Comparator<Student> BY_MARKS_DESC =
                Comparator.comparingDouble(Student::getMarks).reversed();

        /** Sort by roll number ascending (same as natural order, explicit). */
        static final Comparator<Student> BY_ROLL =
                Comparator.comparingInt(Student::getRollNo);

        /** Reverse natural ordering: rollNo DESC. */
        static final Comparator<Student> BY_ROLL_DESC =
                Comparator.<Student>naturalOrder().reversed();

        /**
         * Chained multi-level ordering.
         * Primary:   marks DESCENDING
         * Secondary: name ASCENDING (tie-breaker)
         * Tertiary:  rollNo ASCENDING (final tie-breaker)
         */
        static final Comparator<Student> BY_MARKS_THEN_NAME_THEN_ROLL =
                Comparator.comparingDouble(Student::getMarks).reversed()
                          .thenComparing(Student::getName)
                          .thenComparingInt(Student::getRollNo);

        /** Name length ascending, then alphabetical. */
        static final Comparator<Student> BY_NAME_LENGTH =
                Comparator.comparingInt((Student s) -> s.getName().length())
                          .thenComparing(Student::getName);

        /** Null-safe name ordering (nulls first). */
        static final Comparator<Student> BY_NAME_NULL_SAFE =
                Comparator.nullsFirst(Comparator.comparing(Student::getName));
    }

    // =================================================================
    //  Demonstrations
    // =================================================================
    static class Demos {

        /** Builds a fresh sample list every call (lists are mutable). */
        private static List<Student> sampleStudents() {
            return new ArrayList<>(Arrays.asList(
                    new Student(103, "Charlie", 78.5),
                    new Student(101, "Alice",   92.0),
                    new Student(104, "Dave",    65.0),
                    new Student(102, "Bob",     92.0),   // same marks as Alice
                    new Student(105, "Eve",     88.0)
            ));
        }

        /** 1 & 2. Natural ordering and its reverse. */
        static void demoNaturalOrdering() {
            System.out.println("========== 1. Natural Ordering (Comparable) ==========");

            List<Student> students = sampleStudents();
            System.out.println("Original           : " + students);

            // Collections.sort(list) — uses Student.compareTo (natural ordering by rollNo)
            Collections.sort(students);
            System.out.println("Natural (rollNo ^) : " + students);

            // Reverse via Collections.reverseOrder()
            Collections.sort(students, Collections.reverseOrder());
            System.out.println("Reverse (rollNo v) : " + students);
        }

        /** 3 & 4 & 5. Comparator variants — anonymous class, lambda, method ref. */
        static void demoComparatorVariants() {
            System.out.println("\n========== 2. Comparator Variants ==========");

            List<Student> students = sampleStudents();

            // 3a. Anonymous Comparator class (pre-Java-8 style)
            students.sort(new Comparator<Student>() {
                @Override
                public int compare(Student a, Student b) {
                    return Double.compare(a.getMarks(), b.getMarks());
                }
            });
            System.out.println("Anonymous (marks ^): " + students);

            // 3b. Named Comparator constant
            students.sort(Student.BY_MARKS_DESC);
            System.out.println("BY_MARKS_DESC      : " + students);

            // 4. Inline lambda
            students.sort((a, b) -> a.getName().compareTo(b.getName()));
            System.out.println("Lambda (name ^)    : " + students);

            // 5. Method reference via Comparator.comparing
            students.sort(Comparator.comparing(Student::getName));
            System.out.println("Method ref (name ^): " + students);
        }

        /** 6 & 7. Chained comparators and primitive-specialised comparators. */
        static void demoChainedAndPrimitiveComparators() {
            System.out.println("\n========== 3. Chained & Primitive Comparators ==========");

            List<Student> students = sampleStudents();

            // Chained: marks DESC, then name ASC, then rollNo ASC
            students.sort(Student.BY_MARKS_THEN_NAME_THEN_ROLL);
            System.out.println("Marksv, Name^, Roll^: " + students);

            // comparingInt avoids boxing overhead for int-returning keys
            students.sort(Student.BY_NAME_LENGTH);
            System.out.println("Name length ^       : " + students);

            // comparingDouble for double-returning keys
            students.sort(Comparator.comparingDouble(Student::getMarks));
            System.out.println("Marks ^ (comparingDouble): " + students);
        }

        /** 8. Null-safe comparators. */
        static void demoNullSafeComparators() {
            System.out.println("\n========== 4. Null-Safe Comparators ==========");

            // Students with a null name — demonstrates nullsFirst / nullsLast
            List<Student> students = new ArrayList<>(Arrays.asList(
                    new Student(1, "Alice", 80),
                    new Student(2, null,    70),
                    new Student(3, "Bob",    90),
                    new Student(4, null,     60)
            ));

            // nullsFirst — null names sort to the top
            students.sort(Comparator.comparing(Student::getName, Comparator.nullsFirst(Comparator.naturalOrder())));
            System.out.println("nullsFirst (name): " + students);

            // nullsLast — null names sort to the bottom
            students.sort(Comparator.comparing(Student::getName, Comparator.nullsLast(Comparator.naturalOrder())));
            System.out.println("nullsLast  (name): " + students);

            // nullsFirst then rollNo as tie-breaker
            students.sort(
                    Comparator.comparing(Student::getName, Comparator.nullsFirst(Comparator.naturalOrder()))
                            .thenComparingInt(Student::getRollNo)
            );
            System.out.println("nullsFirst, roll^ : " + students);
        }

        /** 9. Comparator in TreeSet — sorted Set. */
        static void demoTreeSetWithComparator() {
            System.out.println("\n========== 5. TreeSet with Comparator ==========");

            // Natural ordering TreeSet — uses Student.compareTo
            TreeSet<Student> natural = new TreeSet<>(sampleStudents());
            System.out.println("Natural TreeSet    : " + natural);

            // TreeSet with external Comparator — marks DESC
            TreeSet<Student> byMarks = new TreeSet<>(Student.BY_MARKS_DESC);
            byMarks.addAll(sampleStudents());
            System.out.println("Marksv TreeSet     : " + byMarks);

            // TreeSet with chained comparator
            TreeSet<Student> chained = new TreeSet<>(Student.BY_MARKS_THEN_NAME_THEN_ROLL);
            chained.addAll(sampleStudents());
            System.out.println("Chained TreeSet    : " + chained);

            // PITFALL: TreeSet uniqueness is decided by compareTo/compare, NOT equals.
            // Two Students with equal marks but different rollNos will still be
            // treated as duplicates if the comparator returns 0 for equal marks.
            TreeSet<Student> pitfall = new TreeSet<>(
                    Comparator.comparingDouble(Student::getMarks)
            );
            pitfall.add(new Student(1, "Alice", 90));
            pitfall.add(new Student(2, "Bob",   90)); // SAME marks -> considered duplicate!
            System.out.println("Pitfall TreeSet size (expected 1, not 2): " + pitfall.size());
            System.out.println("Contents (only Alice, because marks tie): " + pitfall);
        }

        /** 10. Comparator in TreeMap — sorted by keys. */
        static void demoTreeMapWithComparator() {
            System.out.println("\n========== 6. TreeMap with Comparator ==========");

            // TreeMap sorted by Student keys (natural ordering via compareTo)
            TreeMap<Student, String> natural = new TreeMap<>();
            for (Student s : sampleStudents()) natural.put(s, s.getName());
            System.out.println("Natural TreeMap    : " + natural);

            // TreeMap sorted by external Comparator (marks DESC)
            TreeMap<Student, String> byMarks = new TreeMap<>(Student.BY_MARKS_DESC);
            for (Student s : sampleStudents()) byMarks.put(s, s.getName());
            System.out.println("Marksv TreeMap     : " + byMarks);

            // Reverse natural ordering — rollNo DESC
            TreeMap<Student, String> reverse = new TreeMap<>(Student.BY_ROLL_DESC);
            for (Student s : sampleStudents()) reverse.put(s, s.getName());
            System.out.println("Rollv TreeMap      : " + reverse);

            // PITFALL: if two keys compare as equal, the second overwrites the first.
            TreeMap<Student, String> pitfall = new TreeMap<>(
                    Comparator.comparingDouble(Student::getMarks)
            );
            pitfall.put(new Student(1, "Alice", 90), "Alice");
            pitfall.put(new Student(2, "Bob",   90), "Bob"); // same marks -> overwrites Alice
            System.out.println("Pitfall TreeMap (size expected 1): " + pitfall.size());
            System.out.println("Contents (Bob overwrote Alice): " + pitfall);
        }

        /** 11. Bonus — Comparator in PriorityQueue (heap ordering). */
        static void demoPriorityQueue() {
            System.out.println("\n========== 7. PriorityQueue with Comparator ==========");

            // PriorityQueue — min-heap by default. Here we override via Comparator
            // so that polling yields students in marks-descending order.
            PriorityQueue<Student> pq =
                    new PriorityQueue<>(Student.BY_MARKS_DESC);

            pq.addAll(sampleStudents());

            System.out.print("Polling in marks v: ");
            while (!pq.isEmpty()) {
                System.out.print(pq.poll() + " ");
            }
            System.out.println();
        }

        /** 12. Min/Max via Comparator. */
        static void demoMinMax() {
            System.out.println("\n========== 8. Min / Max via Comparator ==========");

            List<Student> students = sampleStudents();

            Student minByRoll  = Collections.min(students); // natural (Comparable)
            Student maxByRoll  = Collections.max(students);
            Student minByMarks = Collections.min(students, Student.BY_MARKS_DESC);
            Student maxByMarks = Collections.max(students, Student.BY_MARKS_DESC);

            System.out.println("Min by natural (rollNo): " + minByRoll);
            System.out.println("Max by natural (rollNo): " + maxByRoll);
            System.out.println("Min by marks          : " + minByMarks);
            System.out.println("Max by marks          : " + maxByMarks);
        }

        /** 13. Binary search with Comparator (list must be sorted by same comparator). */
        static void demoBinarySearch() {
            System.out.println("\n========== 9. Binary Search with Comparator ==========");

            List<Student> students = sampleStudents();
            students.sort(Student.BY_NAME); // sort by the SAME comparator used for search

            Student key = new Student(999, "Dave", 0); // rollNo/marks irrelevant — name matters
            int idx = Collections.binarySearch(students, key, Student.BY_NAME);
            System.out.println("Sorted by name     : " + students);
            System.out.println("Search for 'Dave'  : index " + idx
                    + " -> " + (idx >= 0 ? students.get(idx) : "not found"));
        }

        /** 14. Grouping with sorted output via TreeMap + Comparator. */
        static void demoGroupingWithSortedOutput() {
            System.out.println("\n========== 10. Grouping with Sorted Output ==========");

            // Group students by MARKS BUCKET, using a TreeMap sorted by bucket DESC.
            TreeMap<Integer, List<Student>> buckets = new TreeMap<>(Comparator.reverseOrder());

            for (Student s : sampleStudents()) {
                int bucket = (int) (s.getMarks() / 10) * 10; // e.g., 90, 80, 70...
                buckets.computeIfAbsent(bucket, k -> new ArrayList<>()).add(s);
            }

            for (Map.Entry<Integer, List<Student>> e : buckets.entrySet()) {
                System.out.println("Bucket " + e.getKey() + "s: " + e.getValue());
            }
        }

        /** 15. Comparable vs Comparator — side-by-side summary. */
        static void demoSummary() {
            System.out.println("\n========== 11. Comparable vs Comparator — Summary ==========");

            List<Student> students = sampleStudents();

            // ---- Comparable path ----
            // ONE ordering baked into the class. Invoked implicitly.
            Collections.sort(students);
            System.out.println("Comparable natural  : " + students);

            // ---- Comparator path ----
            // MANY orderings, external, reusable. Invoked explicitly.
            students.sort(Student.BY_MARKS_DESC);
            System.out.println("Comparator by marks : " + students);

            students.sort(Student.BY_NAME);
            System.out.println("Comparator by name  : " + students);

            students.sort(Student.BY_NAME_LENGTH);
            System.out.println("Comparator by length: " + students);

            System.out.println("\nKey differences in one line:");
            System.out.println("  Comparable — 'I know how to compare MYSELF' (java.lang, 1 ordering)");
            System.out.println("  Comparator — 'I know how to compare TWO OTHERS' (java.util, N orderings)");
        }
    }

    // =================================================================
    //  Driver
    // =================================================================
    public static void main(String[] args) {
        Demos.demoNaturalOrdering();
        Demos.demoComparatorVariants();
        Demos.demoChainedAndPrimitiveComparators();
        Demos.demoNullSafeComparators();
        Demos.demoTreeSetWithComparator();
        Demos.demoTreeMapWithComparator();
        Demos.demoPriorityQueue();
        Demos.demoMinMax();
        Demos.demoBinarySearch();
        Demos.demoGroupingWithSortedOutput();
        Demos.demoSummary();

        System.out.println("\n========== All Comparable/Comparator demos completed ==========");
    }
}