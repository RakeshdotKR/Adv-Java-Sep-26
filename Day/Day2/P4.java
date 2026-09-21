// Wildcards and PECS
// ================================================================
// Demonstrates unbounded, upper-bounded and lower-bounded wildcards using PECS.
import java.util.*;

public class P4 {
    static void printList(List<?> list) {
        for (Object item : list) {
            System.out.println(item);
        }
    }

    static double sum(List<? extends Number> numbers) {
        double total = 0;
        for (Number number : numbers) {
            total += number.doubleValue();
        }
        return total;
    }

    static void addIntegers(List<? super Integer> destination) {
        destination.add(10);
        destination.add(20);
    }

    static <T> void copy(List<? super T> destination, List<? extends T> source) {
        for (T item : source) {
            destination.add(item);
        }
    }

    public static void main(String[] args) {
        List<String> names = Arrays.asList("Alice", "Bob");
        printList(names);

        List<Integer> numbers = Arrays.asList(1, 2, 3);
        System.out.println("Sum: " + sum(numbers));

        List<Number> destination = new ArrayList<>();
        addIntegers(destination);
        System.out.println(destination);

        List<Integer> source = Arrays.asList(4, 5, 6);
        List<Number> copied = new ArrayList<>();
        copy(copied, source);
        System.out.println(copied);
    }
}
