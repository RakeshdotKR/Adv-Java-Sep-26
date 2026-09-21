class Pair<K, V> {
    private final K key;
    private final V value;

    Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    K getKey() { return key; }
    V getValue() { return value; }
}

class Utils {
    public static <T> T firstElement(java.util.List<T> list) {
        return list.get(0);
    }

    public static <T> void printArray(T[] array) {
        for (T item : array) {
            System.out.println(item);
        }
    }
}

public class P2 {
    public static void main(String[] args) {
        Pair<String, Integer> pair = new Pair<>("Age", 25);
        System.out.println(pair.getKey() + " = " + pair.getValue());

        String[] names = {"Alice", "Bob", "Charlie"};
        Utils.printArray(names);

        java.util.List<String> cities = java.util.Arrays.asList("Bengaluru", "Chennai");
        System.out.println("First city: " + Utils.firstElement(cities));
    }
}