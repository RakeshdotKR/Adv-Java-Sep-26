/**
 * NioDirectoryStreamExample.java
 * ------------------------------
 * Demonstrates DirectoryStream — NIO.2's scalable way to iterate over a
 * directory WITHOUT loading every entry into memory.
 *
 * WHEN TO USE DirectoryStream
 *   - Iterating millions of files: memory stays flat.
 *   - Simple iteration + filtering by glob or a custom predicate.
 *   - Works on very large directories where Files.list() might still be OK,
 *     but DirectoryStream gives explicit control over iteration.
 *
 * WHEN NOT TO USE
 *   - If you need nested traversal, use Files.walk / Files.walkFileTree.
 *   - If you need a Stream<Path> for chaining, use Files.list (Java 8+).
 *
 * NOTES
 *   - DirectoryStream is Iterable<Path> and AutoCloseable.
 *   - It must be used inside try-with-resources.
 *   - It supports glob syntax in newDirectoryStream(dir, "*.txt").
 *
 * Compile: javac NioDirectoryStreamExample.java
 * Run    : java NioDirectoryStreamExample
 */

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class NioDirectoryStreamExample {

    public static void main(String[] args) throws IOException {

        // --- 1. Build a small demo directory with a few files ---
        Path work = Files.createTempDirectory("ds_demo");
        System.out.println("Work dir: " + work);

        Files.writeString(work.resolve("a.txt"),   "alpha");
        Files.writeString(work.resolve("b.txt"),   "bravo");
        Files.writeString(work.resolve("c.log"),   "charlie");
        Files.writeString(work.resolve("d.txt"),   "delta");
        Files.writeString(work.resolve("readme"),  "no extension");

        // --- 2. Plain iteration (no filter) ---
        System.out.println("\n--- All entries (no filter) ---");
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(work)) {
            for (Path p : ds) {
                System.out.println("  " + p.getFileName());
            }
        }

        // --- 3. Glob filter: *.txt ---
        System.out.println("\n--- Glob filter: *.txt ---");
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(work, "*.txt")) {
            for (Path p : ds) {
                System.out.println("  " + p.getFileName());
            }
        }

        // --- 4. Glob with multiple patterns (brace expansion) ---
        System.out.println("\n--- Glob filter: *.{txt,log} ---");
        try (DirectoryStream<Path> ds =
                     Files.newDirectoryStream(work, "*.{txt,log}")) {
            for (Path p : ds) {
                System.out.println("  " + p.getFileName());
            }
        }

        // --- 5. Custom predicate filter (functional interface) ---
        // DirectoryStream.Filter<Path> is a @FunctionalInterface.
        System.out.println("\n--- Custom predicate: size > 5 bytes ---");
        DirectoryStream.Filter<Path> bigFiles =
                p -> Files.isRegularFile(p) && Files.size(p) > 5;
        try (DirectoryStream<Path> ds =
                     Files.newDirectoryStream(work, bigFiles)) {
            for (Path p : ds) {
                System.out.println("  " + p.getFileName()
                        + " (" + Files.size(p) + " bytes)");
            }
        }

        // --- 6. Non-recursive limitation: subdirectories are just entries ---
        Path sub = Files.createDirectory(work.resolve("subdir"));
        Files.writeString(sub.resolve("nested.txt"), "inside");

        System.out.println("\n--- Non-recursive: 'subdir' is an entry, not descended ---");
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(work)) {
            for (Path p : ds) {
                System.out.println("  " + p.getFileName()
                        + (Files.isDirectory(p) ? "  [DIR]" : ""));
            }
        }

        // --- 7. Cleanup ---
        Files.deleteIfExists(sub.resolve("nested.txt"));
        Files.deleteIfExists(sub);
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(work)) {
            for (Path p : ds) Files.delete(p);
        }
        Files.deleteIfExists(work);

        System.out.println("\nCleaned up. Done.");
    }
}