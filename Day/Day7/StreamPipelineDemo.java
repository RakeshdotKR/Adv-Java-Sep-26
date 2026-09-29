// Java Stream Pipeline: filter, map, sorted, reduce, collect
// program demonstrating the five key Stream operations in a single pipeline.
// filter: exclude elements which do not match a condition
// map: Transform each element into something else. reg no. 
// sorted: sort the elements
// reduce: combine elements into a single aggregate value. total marks of 100 ==> Avg total marks
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Demonstrates Java Stream API pipeline operations:
 * filter -> map -> sorted -> reduce / collect
 *
 * A Stream pipeline consists of:
 *   1. A SOURCE (e.g., a collection)
 *   2. Zero or more INTERMEDIATE operations (filter, map, sorted) — lazy
 *   3. A TERMINAL operation (reduce, collect, forEach) — triggers execution
 */
public class StreamPipelineDemo {

    // Simple immutable domain object (Java 16+ record)
    record Employee(String name, String department, double salary, int age) {}

    public static void main(String[] args) {

        // ---------- 1. SOURCE: list of employees ----------
        List<Employee> employees = List.of(
                new Employee("Alice",   "Engineering", 95000, 30),
                new Employee("Bob",     "Engineering", 72000, 25),
                new Employee("Charlie", "Marketing",   68000, 28),
                new Employee("Diana",   "Engineering", 110000, 35),
                new Employee("Eve",     "Marketing",   54000, 22),
                new Employee("Frank",   "Engineering", 68000, 41)
        );

        // ============================================================
        // EXAMPLE 1: filter -> map -> sorted -> collect (to List)
        // Goal: names of Engineering employees earning > 70k, sorted by name
        // ============================================================
        List<String> highPaidEngineers = employees.stream()             // SOURCE
                // INTERMEDIATE: keep only Engineering employees (predicate)
                .filter(e -> e.department().equals("Engineering")) //4
                // INTERMEDIATE: keep only those earning more than 70,000
                .filter(e -> e.salary() > 70_000)  //4
                // INTERMEDIATE: transform Employee -> String (name)
                .map(Employee::name)
                // INTERMEDIATE: natural-order sort (alphabetical here)
                .sorted()
                // TERMINAL: collect results into a mutable List
                .collect(Collectors.toList());

        System.out.println("High-paid engineers (sorted by name): " + highPaidEngineers);
        // Expected: [Alice, Diana, Frank]

        // ============================================================
        // EXAMPLE 2: filter -> map -> reduce (sum of all Engineering salaries)
        // ============================================================
        double totalEngineeringPay = employees.stream()
                .filter(e -> e.department().equals("Engineering"))    // keep Engineering
                .mapToDouble(Employee::salary)                        // map to primitive double
                .reduce(0.0, Double::sum);                            // TERMINAL: sum all values

        System.out.println("Total Engineering payroll: " + totalEngineeringPay);
        // Expected: 95000 + 72000 + 110000 + 88000 = 365000.0

        // ============================================================
        // EXAMPLE 3: reduce returning Optional (highest salary)
        // ============================================================
        Optional<Employee> highestPaid = employees.stream()
                .reduce((e1, e2) -> e1.salary() >= e2.salary() ? e1 : e2); // TERMINAL

        highestPaid.ifPresent(e ->
                System.out.println("Highest paid: " + e.name() + " ($" + e.salary() + ")"));
        // Expected: Diana ($110000.0)

        // ============================================================
        // EXAMPLE 4: filter -> sorted (custom comparator) -> collect (to Map)
        // Goal: map of department -> list of employee names, sorted by salary desc
        // ============================================================
        Map<String, List<String>> namesByDept = employees.stream()
                .sorted((a, b) -> Double.compare(b.salary(), a.salary())) // salary DESC
                .collect(Collectors.groupingBy(
                        Employee::department,                              // key mapper
                        Collectors.mapping(Employee::name, Collectors.toList()) // value mapper
                ));

        System.out.println("Employees grouped by department: " + namesByDept);
        // Expected: {Engineering=[Diana, Alice, Frank, Bob], Marketing=[Charlie, Eve]}

        // ============================================================
        // EXAMPLE 5: pipeline with distinct + limit + joining
        // ============================================================
        String deptList = employees.stream()
                .map(Employee::department)       // Employee -> department name
                .distinct()                      // remove duplicates
                .sorted()                        // sort alphabetically
                .collect(Collectors.joining(", ")); // join with comma

        System.out.println("Distinct departments: " + deptList);
        // Expected: Engineering, Marketing

        // ============================================================
        // EXAMPLE 6: reduce to build a comma-separated string (no collect)
        // ============================================================
        String namesCsv = employees.stream()
                .map(Employee::name)
                .reduce("", (acc, name) -> acc.isEmpty() ? name : acc + ", " + name);

        System.out.println("All names: " + namesCsv);
        // Expected: Alice, Bob, Charlie, Diana, Eve, Frank

        // ============================================================
        // EXAMPLE 7: IntStream sum with filter (primitive specialization)
        // ============================================================
        int totalAges = employees.stream()
                .filter(e -> e.age() < 40)      // under 40
                .mapToInt(Employee::age)        // Employee -> int
                .sum();                         // TERMINAL shortcut for reduce

        System.out.println("Total age of employees under 40: " + totalAges);
        // Expected: 30 + 25 + 28 + 22 = 105
    }
}