/**
 * BasicTryWithResources.java
 * --------------------------
 * Demonstrates the fundamental syntax of try-with-resources.
 *
 * KEY IDEA
 *   Any object whose class implements java.lang.AutoCloseable
 *   can be declared inside the parentheses of `try (...)`,
 *   and Java will call .close() automatically when the block exits —
 *   whether it exits normally OR because an exception was thrown.
 *
 * ORDER OF CLOSE
 *   Resources are closed in REVERSE order of declaration
 *   (last declared = first closed), like a stack.
 *
 * Compile: javac BasicTryWithResources.java
 * Run    : java BasicTryWithResources
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class BasicTryWithResources {

    public static void main(String[] args) throws IOException {

        // Use a temp file so the demo is self-contained.
        String path = System.getProperty("java.io.tmpdir") + "/demo_twr.txt";

        // ---------------------------------------------------------
        // 1. WRITE a file using try-with-resources (single resource)
        // ---------------------------------------------------------
        // PrintWriter implements AutoCloseable -> auto .close() at block end.
        try (PrintWriter pw = new PrintWriter(new FileWriter(path))) {
            pw.println("Line 1");
            pw.println("Line 2");
            pw.println("Line 3");
            // No finally { pw.close(); } needed — TWR does it.
        }

        // ---------------------------------------------------------
        // 2. READ the file back (single resource)
        // ---------------------------------------------------------
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println("read: " + line);
            }
        }

        // ---------------------------------------------------------
        // 3. MULTIPLE resources in one try — closed in REVERSE order
        // ---------------------------------------------------------
        // Both reader and writer close automatically when the block ends.
        // The writer (declared second) closes FIRST, then the reader.
        try (BufferedReader in  = new BufferedReader(new FileReader(path));
             PrintWriter  out = new PrintWriter(new FileWriter(path + ".copy"))) {

            String line;
            while ((line = in.readLine()) != null) {
                out.println(line.toUpperCase());
            }
        }

        // Verify the copy
        System.out.println("\n--- Copy contents ---");
        try (BufferedReader br = new BufferedReader(new FileReader(path + ".copy"))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println("copy: " + line);
            }
        }

        System.out.println("\nDone. No explicit close() calls were needed.");
    }
}