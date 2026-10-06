/**
 * CustomAutoCloseable.java
 * ------------------------
 * Shows how to write your OWN resource class that participates
 * in try-with-resources by implementing AutoCloseable.
 *
 * THE CONTRACT
 *   public interface AutoCloseable {
 *       void close() throws Exception;
 *   }
 *
 * Closeable (java.io) refines it to:
 *   public interface Closeable extends AutoCloseable {
 *       void close() throws IOException;   // narrower checked exception
 *   }
 *
 * Prefer Closeable when your close() only throws IOException,
 * because callers don't have to catch the broad Exception.
 *
 * Compile: javac CustomAutoCloseable.java
 * Run    : java CustomAutoCloseable
 */

public class CustomAutoCloseable {

    /** A resource that can be opened and closed, tracked with a flag. */
    static class DatabaseConnection implements AutoCloseable {

        private final String name;
        private boolean open;

        public DatabaseConnection(String name) {
            this.name = name;
            this.open = true;
            System.out.println("  [open]  " + name);
        }

        /** Simulated work. Fails if the resource was already closed. */
        public void query(String sql) {
            if (!open) {
                throw new IllegalStateException("Connection is closed: " + name);
            }
            System.out.println("  [query] " + name + " -> " + sql);
        }

        /**
         * Called automatically by try-with-resources.
         * Marked @Override to enforce the AutoCloseable contract.
         */
        @Override
        public void close() {
            if (open) {
                open = false;
                System.out.println("  [close] " + name);
            }
        }
    }

    public static void main(String[] args) {

        // ---------------------------------------------------------
        // 1. Normal exit — close() called automatically
        // ---------------------------------------------------------
        System.out.println("--- 1. Normal exit ---");
        try (DatabaseConnection db = new DatabaseConnection("db-main")) {
            db.query("SELECT * FROM users");
        }  // close() fires here
        System.out.println("  after try block\n");

        // ---------------------------------------------------------
        // 2. Exception inside the block — close() STILL fires
        // ---------------------------------------------------------
        System.out.println("--- 2. Exception inside block ---");
        try (DatabaseConnection db = new DatabaseConnection("db-tx")) {
            db.query("INSERT INTO logs VALUES (1)");
            throw new RuntimeException("Simulated failure");
            // db.close() runs BEFORE the RuntimeException propagates.
        } catch (RuntimeException e) {
            System.out.println("  caught: " + e.getMessage());
        }
        System.out.println();

        // ---------------------------------------------------------
        // 3. Multiple custom resources — reverse close order
        // ---------------------------------------------------------
        System.out.println("--- 3. Multiple resources ---");
        try (DatabaseConnection primary = new DatabaseConnection("primary");
             DatabaseConnection replica = new DatabaseConnection("replica")) {
            primary.query("BEGIN");
            replica.query("SELECT COUNT(*) FROM orders");
        }  // replica closes first, then primary
        System.out.println("  done");
    }
}