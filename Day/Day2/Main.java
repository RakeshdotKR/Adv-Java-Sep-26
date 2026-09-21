// Demonstrates a generic class that can safely store different reference types.
class Box<T> {
    private T value;

    public void set(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }
}

public class Main {
    public static void main(String[] args) {
        Box<String> stringBox = new Box<>();
        stringBox.set("Hello");

        Box<Integer> integerBox = new Box<>();
        integerBox.set(42);

        System.out.println(stringBox.get());
        System.out.println(integerBox.get());
    }
}