/**
 * CloseableVsAutoCloseable.java
 * -----------------------------
 * Compares the two resource interfaces you'll meet most often.
 *
 *   AutoCloseable (java.lang, Java 7+)
 *     - void close() throws Exception
 *     - Broad; allows any checked exception.
 *     - Also used by lock-free / async resources.
 *
 *   Closeable (java.io, Java 5+)
 *     - void close() throws IOException
 *     - Narrower checked exception; part of the I/O hierarchy.
 *     - Its close() is idempotent by contract (safe to call twice).
 *     - Extends AutoCloseable (as of Java 7).
 *
 * RULE OF THUMB
 *   If your close() only throws IOException, implement Closeable.
 *   Otherwise implement AutoCloseable.
 *   Both work identically inside try-with-resources.
 *
 * Compile: javac CloseableVsAutoCloseable.java
 * Run    : java CloseableVsAutoCloseable
 */

import java.io.Closeable;
import java.io.IOException;

public class CloseableVsAutoCloseable {

    // ---------------------------------------------------------
    // Custom class implementing Closeable -> close() throws IOException
    // ---------------------------------------------------------
    static class FileLikeResource implements Closeable {
        private final String name;
        private boolean closed;

        public FileLikeResource(String name) {
            this.name = name;
            System.out.println("  [open]  " + name);
        }

        public void write(String data) throws IOException {
            if (closed) throw new IOException("Already closed: " + name);
            System.out.println("  [write] " + name + " <- " + data);
        }

        /** Idempotent close: safe to call more than once. */
        @Override
        public void close() throws IOException {
            if (!closed) {
                closed = true;
                System.out.println("  [close] " + name);
                // Could throw IOException in real code (e.g. disk full on flush).
            }
        }
    }

    // ---------------------------------------------------------
    // Custom class implementing AutoCloseable -> close() throws Exception
    // ---------------------------------------------------------
    static class NetworkSession implements AutoCloseable {
        private final String host;
        private boolean closed;

        public NetworkSession(String host) {
            this.host = host;
            System.out.println("  [connect] " + host);
        }

        public void send(String msg) {
            if (closed) throw new IllegalStateException("Session closed");
            System.out.println("  [send]    " + host + " -> " + msg);
        }

        /**
         * Here close() performs cleanup that could fail with either
         * an IOException OR a RuntimeException — that's why we use
         * AutoCloseable's broad `throws Exception` signature.
         */
        @Override
        public void close() throws Exception {
            if (!closed) {
                closed = true;
                System.out.println("  [disconnect] " + host);
            }
        }
    }

    public static void main(String[] args) throws Exception {

        // --- Closeable resource ---
        System.out.println("--- Closeable ---");
        try (FileLikeResource r = new FileLikeResource("data.bin")) {
            r.write("hello");
            r.write("world");
        }

        // --- AutoCloseable resource ---
        System.out.println("\n--- AutoCloseable ---");
        try (NetworkSession s = new NetworkSession("api.example.com")) {
            s.send("GET /users");
            s.send("GET /orders");
        }

        // --- Idempotency of close() on Closeable ---
        System.out.println("\n--- Idempotency ---");
        FileLikeResource r = new FileLikeResource("idempotent.bin");
        r.close();      // first close: prints [close]
        r.close();      // second close: no-op (contract says safe)
        System.out.println("  close() called twice — no exception.");

        System.out.println("\nDone.");
    }
}