/**
 * ClassicIOBasics.java
 * --------------------
 * Demonstrates the CLASSIC (pre-Java 7) java.io.File API.
 *
 * CHARACTERISTICS OF CLASSIC I/O
 *   - File is just a path handle — it's also used for directory ops.
 *   - Many methods return boolean for failure; no exceptions thrown
 *     for common cases (createNewFile, mkdir, delete, renameTo).
 *   - Poor error reporting: "false" tells you nothing about WHY.
 *   - No support for symbolic links, file attributes, or atomic moves.
 *   - Path separator is baked in via File.separator.
 *
 * Compile: javac ClassicIOBasics.java
 * Run    : java ClassicIOBasics
 */

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ClassicIOBasics {

    public static void main(String[] args) throws IOException {

        // --- 1. Build a path from a string + separators ---
        String base = System.getProperty("java.io.tmpdir");
        File dir = new File(base, "classic_io_demo");
        File file = new File(dir, "notes.txt");

        // --- 2. Create a directory (returns boolean — no exception) ---
        if (dir.mkdir()) {
            System.out.println("Created directory: " + dir.getAbsolutePath());
        } else if (dir.exists()) {
            System.out.println("Directory already exists: " + dir.getAbsolutePath());
        } else {
            System.out.println("Failed to create directory (no reason given!)");
        }

        // --- 3. Create a file (returns boolean — no exception) ---
        boolean created = file.createNewFile();  // throws IOException only for real I/O errors
        System.out.println("createNewFile returned: " + created
                + " (false if already exists)");

        // --- 4. Write using classic streams ---
        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            pw.println("Classic I/O line 1");
            pw.println("Classic I/O line 2");
        }

        // --- 5. Query file metadata with File methods ---
        System.out.println("\n--- Metadata (classic) ---");
        System.out.println("Name        : " + file.getName());
        System.out.println("Absolute    : " + file.getAbsolutePath());
        System.out.println("Exists      : " + file.exists());
        System.out.println("Is file     : " + file.isFile());
        System.out.println("Is directory: " + file.isDirectory());
        System.out.println("Length      : " + file.length() + " bytes");
        System.out.println("Last modified (ms): " + file.lastModified());

        // --- 6. List directory contents (no filter, no lazy stream) ---
        System.out.println("\n--- Directory listing (classic) ---");
        String[] names = dir.list();          // returns null on error
        if (names != null) {
            for (String n : names) {
                System.out.println("  " + n);
            }
        }

        // --- 7. List with filename filter (still eager, still String[]) ---
        System.out.println("\n--- Filtered listing (*.txt) ---");
        String[] txtFiles = dir.list((d, name) -> name.endsWith(".txt"));
        if (txtFiles != null) {
            for (String n : txtFiles) {
                System.out.println("  " + n);
            }
        }

        // --- 8. Cleanup ---
        System.out.println("\nDeleted file   : " + file.delete());
        System.out.println("Deleted dir    : " + dir.delete());

        System.out.println("\nDone (classic I/O).");
    }
}