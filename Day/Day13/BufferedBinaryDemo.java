/**
 * BufferedBinaryDemo.java
 * -----------------------
 * Demonstrates BUFFERED BYTE STREAMS in Java:
 *   - BufferedOutputStream  (wraps OutputStream)
 *   - BufferedInputStream   (wraps InputStream)
 *
 * Also compares RAW vs BUFFERED file copy performance so you can see
 * the difference for yourself.
 *
 * How to run:
 *   javac BufferedBinaryDemo.java
 *   java  BufferedBinaryDemo
 */

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BufferedBinaryDemo {

    public static void main(String[] args) throws IOException {

        File dir = new File(System.getProperty("java.io.tmpdir"),"buffered_binary_demo");
        if (!dir.exists() && !dir.mkdirs()) {
            System.err.println("Cannot create temp dir");
            return;
        }

        File source = new File(dir, "source.bin");
        File rawCopy = new File(dir, "copy_raw.bin");
        File bufCopy = new File(dir, "copy_buffered.bin");

        // -----------------------------------------------------------------
        // 1. CREATE a ~2 MB source file using a buffered output stream
        // -----------------------------------------------------------------
        byte[] chunk = new byte[8192];                    // 8 KB of zeros
        try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(source))) {
            for (int i = 0; i < 256; i++) {               // 256 * 8 KB = 2 MB
                out.write(chunk);
            }
        }
        System.out.println("Created source: " + source.length() + " bytes\n");

        // -----------------------------------------------------------------
        // 2. RAW COPY — 1 byte per read() -> 1 syscall per byte (very slow)
        // -----------------------------------------------------------------
        long t1 = System.nanoTime();
        copyRaw(source, rawCopy);
        long t2 = System.nanoTime();
        System.out.printf("Raw copy      : %d ms%n", (t2 - t1) / 1_000_000);

        // -----------------------------------------------------------------
        // 3. BUFFERED COPY — 8 KB per read() -> ~256 syscalls total
        // -----------------------------------------------------------------
        long t3 = System.nanoTime();
        copyBuffered(source, bufCopy);
        long t4 = System.nanoTime();
        System.out.printf("Buffered copy : %d ms%n", (t4 - t3) / 1_000_000);

        // -----------------------------------------------------------------
        // 4. VERIFY both copies are the same size as the source
        // -----------------------------------------------------------------
        System.out.println("\nSizes match source?");
        System.out.println("  raw      : " + (rawCopy.length() == source.length()));
        System.out.println("  buffered : " + (bufCopy.length() == source.length()));

        // -----------------------------------------------------------------
        // 5. READ BACK a few bytes to prove BufferedInputStream works
        // -----------------------------------------------------------------
        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(source))) {
            byte[] head = new byte[16];
            int n = in.read(head);            // reads up to 16 bytes
            System.out.println("\nRead " + n + " bytes from source header.");
        }

        // -----------------------------------------------------------------
        // 6. CLEANUP
        // -----------------------------------------------------------------
        deleteRecursive(dir);
        System.out.println("\nCleaned up: " + dir.getName());
    }

    /**
     * Naive copy: one byte at a time. Slow because each read() is a syscall.
     * Still correct — just inefficient.
     */
    private static void copyRaw(File src, File dst) throws IOException {
        try (InputStream  in  = new FileInputStream(src);
             OutputStream out = new FileOutputStream(dst)) {
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
        }
    }

    /**
     * Buffered copy: reads/writes in chunks via a buffered wrapper.
     * Uses an explicit byte[] so each read() fills the buffer in one go.
     */
    private static void copyBuffered(File src, File dst) throws IOException {
        try (InputStream  in  = new BufferedInputStream(new FileInputStream(src));
             OutputStream out = new BufferedOutputStream(new FileOutputStream(dst))) {
            byte[] buf = new byte[8192];
            int n;
            while ((n = in.read(buf)) != -1) {
                out.write(buf, 0, n);         // write exactly what we read
            }
        }
    }

    /** Recursively delete a file or directory. */
    private static void deleteRecursive(File f) {
        if (f.isDirectory()) {
            File[] kids = f.listFiles();
            if (kids != null) {
                for (File k : kids) deleteRecursive(k);
            }
        }
        f.delete();
    }
}
