// package Day.Day11;

public /**
 * UncheckedExample.java  —  Demonstrates UNCHECKED exceptions.
 *
 * An UNCHECKED exception is a subclass of RuntimeException (or Error).
 * The compiler does NOT force you to catch or declare it.
 *
 * These represent programming bugs (bad arguments, null dereferences,
 * illegal state) rather than recoverable conditions.
 *
 * Compile: javac UncheckedExample.java
 * Run    : java UncheckedExample
 */
public class UncheckedExample {

    /** Custom UNCHECKED exception — extends RuntimeException. */
    static class BadInputException extends RuntimeException {
        public BadInputException(String msg) { super(msg); }
    }

    /**
     * Method that may throw unchecked exceptions.
     * Note: no `throws` clause is required — it's optional and unusual.
     */
    static int divide(int a, int b) {
        // ArithmeticException (unchecked) thrown automatically by the JVM.
        return a / b;
    }

    /** Validates input; throws a custom unchecked exception on bad data. */
    static void validateAge(int age) {
        if (age < 0) {
            // No `throws` clause needed — BadInputException is unchecked.
            throw new BadInputException("Age cannot be negative: " + age);
        }
        System.out.println("Age accepted: " + age);
    }

    /** Demonstrates NullPointerException (classic unchecked). */
    static int stringLength(String s) {
        // If s is null, JVM throws NullPointerException here.
        return s.length();
    }

    public static void main(String[] args) {

        // --- CASE 1: ArithmeticException (thrown automatically) ---
        System.out.println("--- Case 1: divide by zero ---");
        try {
            System.out.println("10 / 2 = " + divide(10, 2));
            System.out.println("10 / 0 = " + divide(10, 0));  // throws
        } catch (ArithmeticException e) {
            System.out.println("Caught: " + e.getClass().getSimpleName()
                    + " -> " + e.getMessage());
        }

        // --- CASE 2: Custom unchecked exception ---
        System.out.println("\n--- Case 2: custom unchecked ---");
        try {
            validateAge(25);   // OK
            validateAge(-5);   // throws BadInputException
        } catch (BadInputException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        // --- CASE 3: NullPointerException ---
        System.out.println("\n--- Case 3: NPE ---");
        try {
            System.out.println("Length of 'Java' = " + stringLength("Java"));
            System.out.println("Length of null   = " + stringLength(null));  // throws
        } catch (NullPointerException e) {
            System.out.println("Caught: NullPointerException");
        }

        System.out.println("\nProgram finished cleanly.");
    }
}
 {
    
}
