/**
 * CheckedExample.java  —  Demonstrates CHECKED exceptions.
 *
 * A CHECKED exception is any subclass of Exception that is NOT
 * a subclass of RuntimeException. The compiler FORCES you to either:
 *   (a) catch it in a try/catch, OR
 *   (b) declare it in the method's `throws` clause.
 *
 * Classic example: java.io.IOException (used below).
 *
 * Compile: javac CheckedExample.java
 * Run    : java CheckedExample
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CheckedExample {

    /**
     * Method that declares a CHECKED exception via `throws`.
     * Callers are now required to handle IOException.
     */
    static String readFirstLine(String path) throws IOException {
        // try-with-resources auto-closes the reader, no explicit close() needed.
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            return br.readLine();
        }
        // No catch here. The IOException propagates to the caller.
    }

    /**
     * Wrapper that catches the IOException and translates it into a
     * domain-specific checked exception (a common pattern in layered apps).
     */
    static String readConfigSafely(String path) throws ConfigException {
        try {
            return readFirstLine(path);
        } catch (IOException e) {
            // Wrap the low-level cause to keep the stack trace chain intact.
            throw new ConfigException("Failed to read config: " + path, e);
        }
    }

    /** Custom CHECKED exception — extends Exception (not RuntimeException). */
    static class ConfigException extends Exception {
        public ConfigException(String msg, Throwable cause) { super(msg, cause); }
    }

    public static void main(String[] args) {

        // --- CASE 1: catch the checked exception directly ---
        System.out.println("--- Case 1: File exists ---");
        try {
            // This file may not exist; IOException is possible.
            String line = readFirstLine("CheckedExample.java");
            System.out.println("First line: " + line);
        } catch (IOException e) {
            System.out.println("IOException caught: " + e.getMessage());
        }

        // --- CASE 2: catch a wrapped checked exception and inspect the cause ---
        System.out.println("\n--- Case 2: File does NOT exist ---");
        try {
            readConfigSafely("definitely-missing-file.txt");
        } catch (ConfigException e) {
            System.out.println("ConfigException: " + e.getMessage());
            // The original IOException is preserved as the cause.
            Throwable cause = e.getCause();
            if (cause != null) {
                System.out.println("  caused by: "
                        + cause.getClass().getSimpleName()
                        + " -> " + cause.getMessage());
            }
        }

        System.out.println("\nProgram finished cleanly.");
    }
}