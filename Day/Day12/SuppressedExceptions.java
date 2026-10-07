/**
 * SuppressedExceptions.java
 * -------------------------
 * KEY BEHAVIOR: When BOTH the try block AND close() throw,
 * try-with-resources keeps the ORIGINAL exception from the body
 * and attaches close()'s exception as a SUPPRESSED exception.
 *
 * In the old try/finally style, a close() failure would OVERWRITE
 * the body's exception — losing critical debugging info.
 * TWR fixes this by preserving both.
 *
 * API:
 *   Throwable.getSuppressed() -> Throwable[]  (Java 7+)
 *
 * Compile: javac SuppressedExceptions.java
 * Run    : java SuppressedExceptions
 */

public class SuppressedExceptions {

    /** A resource whose close() also throws. */
    static class FaultyResource implements AutoCloseable {

        private final String name;

        public FaultyResource(String name) {
            this.name = name;
            System.out.println("  [open]  " + name);
        }

        /** Simulate work that fails. */
        public void doWork() {
            throw new RuntimeException("Body failure in " + name);
        }

        /** close() ALSO fails — TWR will attach this as suppressed. */
        @Override
        public void close() {
            System.out.println("  [close] " + name + " (throwing!)");
            throw new IllegalStateException("Close failure in " + name);
        }
    }

    public static void main(String[] args) {

        System.out.println("--- Demonstrating suppressed exceptions ---");

        try (FaultyResource fr = new FaultyResource("res-1")) {
            fr.doWork();          // throws RuntimeException
            // fr.close() also throws IllegalStateException
        } catch (Exception e) {
            // The PRIMARY exception is the one from the body.
            System.out.println("\nPrimary exception  : "
                    + e.getClass().getSimpleName() + " -> " + e.getMessage());

            // Any close() failures are attached as "suppressed".
            for (Throwable s : e.getSuppressed()) {
                System.out.println("Suppressed         : "
                        + s.getClass().getSimpleName() + " -> " + s.getMessage());
            }
        }

        System.out.println("\n--- Old try/finally would LOSE the body exception ---");
        // For comparison, emulate the pre-Java-7 pattern:
        try {
            FaultyResource fr = new FaultyResource("res-2");
            try {
                fr.doWork();       // throws RuntimeException
            } finally {
                fr.close();        // throws IllegalStateException — OVERWRITES body
            }
        } catch (Exception e) {
            // Notice: only ONE exception is visible here.
            System.out.println("Only visible exception: "
                    + e.getClass().getSimpleName() + " -> " + e.getMessage());
        }

        System.out.println("\nDone.");
    }
}