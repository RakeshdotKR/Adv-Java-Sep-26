/**
 * NioPathBasics.java
 * ------------------
 * Introduces java.nio.file.Path — the NIO.2 replacement for java.io.File.
 *
 * KEY DIFFERENCES
 *   - Path is a PURE representation of a path (no I/O methods).
 *   - Paths.get(...) and Path.of(...) are factories.
 *   - Operations that touch the filesystem live in Files (next file).
 *   - Path is immutable; every "modifier" returns a NEW Path.
 *   - Supports symbolic links and platform-independent separators.
 *
 * Compile: javac NioPathBasics.java
 * Run    : java NioPathBasics
 */

import java.nio.file.Path;
import java.nio.file.Paths;

public class NioPathBasics {

    public static void main(String[] args) {

        // --- 1. Creating Paths ---
        Path p1 = Paths.get("/tmp", "demo", "notes.txt");   // preferred
        Path p2 = Path.of("/tmp/demo/notes.txt");           // Java 11+ shortcut
        System.out.println("p1 = " + p1);
        System.out.println("p2 = " + p2);
        System.out.println("equal? " + p1.equals(p2));      // true — both normalized to same path

        // --- 2. Decomposing a Path ---
        System.out.println("\n--- Decomposition ---");
        System.out.println("getFileName()      : " + p1.getFileName());
        System.out.println("getParent()        : " + p1.getParent());
        System.out.println("getRoot()          : " + p1.getRoot());
        System.out.println("getNameCount()     : " + p1.getNameCount());
        for (int i = 0; i < p1.getNameCount(); i++) {
            System.out.println("  name[" + i + "] = " + p1.getName(i));
        }

        // --- 3. Path is IMMUTABLE — each operation returns a new Path ---
        System.out.println("\n--- Immutability ---");
        Path base     = Paths.get("/tmp/demo");
        Path child    = base.resolve("subdir").resolve("file.txt");
        Path sibling  = child.resolveSibling("other.txt");
        Path relative = base.relativize(Paths.get("/tmp/demo/subdir/file.txt"));
        System.out.println("base     = " + base);
        System.out.println("child    = " + child);
        System.out.println("sibling  = " + sibling);
        System.out.println("relative = " + relative);
        System.out.println("base unchanged after operations? " + base.equals(Paths.get("/tmp/demo")));

        // --- 4. Normalizing (resolving "." and "..") ---
        System.out.println("\n--- Normalization ---");
        Path messy = Paths.get("/tmp/./demo/../demo/notes.txt");
        System.out.println("messy     = " + messy);
        System.out.println("normalized= " + messy.normalize());

        // --- 5. Absolute vs relative ---
        System.out.println("\n--- Absolute vs relative ---");
        Path rel = Paths.get("src/main/java");
        System.out.println("isAbsolute (rel) = " + rel.isAbsolute());
        System.out.println("toAbsolutePath() = " + rel.toAbsolutePath());

        // --- 6. Converting between Path and legacy File ---
        System.out.println("\n--- Path <-> File ---");
        java.io.File legacy = p1.toFile();
        Path backToPath    = legacy.toPath();
        System.out.println("File  : " + legacy);
        System.out.println("Path  : " + backToPath);
        System.out.println("round-trip equal? " + p1.equals(backToPath));

        System.out.println("\nDone (Path basics).");
    }
}