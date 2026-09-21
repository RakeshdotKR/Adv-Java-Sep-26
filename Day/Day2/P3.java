// Bounded Generics
// ================================================================
// Demonstrates an upper-bounded generic type and a multiple-bound generic type.
class NumberBox<T extends Number> {
    private final T value;

    NumberBox(T value) {
        this.value = value;
    }

    double doubleValue() {
        return value.doubleValue();
    }
}

class ComparableBox<T extends Number & Comparable<T>> {
    private final T value;

    ComparableBox(T value) {
        this.value = value;
    }

    int compareTo(ComparableBox<T> other) {
        return value.compareTo(other.value);
    }
}

public class P3 {
    public static void main(String[] args) {
        NumberBox<Integer> intBox = new NumberBox<>(42);
        System.out.println("Double value: " + intBox.doubleValue());

        ComparableBox<Integer> first = new ComparableBox<>(10);
        ComparableBox<Integer> second = new ComparableBox<>(20);
        System.out.println("Comparison: " + first.compareTo(second));
    }
}