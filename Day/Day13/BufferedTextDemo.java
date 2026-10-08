/**
 * BufferedTextDemo.java
 * ---------------------
 * Demonstrates BUFFERED CHARACTER STREAMS in Java:
 *   - BufferedWriter  (wraps a Writer, adds an 8 KB memory buffer)
 *   - BufferedReader  (wraps a Reader, reads a block at a time)
 *
 * Why buffers?
 *   A raw FileWriter/FileReader performs one disk call per character.
 *   Buffered versions batch many chars into one syscall -> much faster.
 *
 * How to run:
 *   javac BufferedTextDemo.java
 *   java  BufferedTextDemo
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class BufferedTextDemo {

    public static void main(String[] args) {

        // Use a temp file in the OS temp dir; we'll delete it at the end.
        File file = new File(System.getProperty("java.io.tmpdir"),
                             "buffered_text_demo.txt");

        // -----------------------------------------------------------------
        // 1. WRITE with BufferedWriter
        // -----------------------------------------------------------------
        // try-with-resources auto-closes (and flushes) the writer at the end
        // of the block, even if an exception is thrown.
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

            String[] lines = {
                "Line 1: Hello, buffered world!",
                "Line 2: Buffers reduce syscalls.",
                "Line 3: close() flushes remaining bytes.",
                "Line 4: newLine() is platform independent.",
                "Line 5: Done writing."
            };

            for (String line : lines) {
                writer.write(line);   // write the text
                writer.newLine();     // platform-appropriate line separator
            }

            System.out.println("Wrote " + lines.length + " lines to: " + file);

        } catch (IOException e) {
            // IOException is CHECKED -> must be caught or declared.
            System.err.println("Write failed: " + e.getMessage());
            return;   // cannot continue to read step
        }

        // -----------------------------------------------------------------
        // 2. READ with BufferedReader
        // -----------------------------------------------------------------
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            String line;
            int lineNo = 1;

            // readLine() returns null at end-of-stream.
            // It strips the trailing newline automatically.
            while ((line = reader.readLine()) != null) {
                System.out.printf("  [%d] %s%n", lineNo++, line);
            }

        } catch (IOException e) {
            System.err.println("Read failed: " + e.getMessage());
        }

        // -----------------------------------------------------------------
        // 3. CLEANUP — delete the temp file
        // -----------------------------------------------------------------
        if (file.delete()) {
            System.out.println("\nCleaned up: " + file.getName());
        } else {
            System.out.println("\nCould not delete: " + file.getName());
        }
    }
}