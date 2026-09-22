import java.util.*;
public class P1 {
	public static void main(String[] args) {
    	// ---------- LIST ----------
    	List<String> myArrayList = new ArrayList<>();
    	myArrayList.add("Apple");
    	myArrayList.add("Banana");
    	myArrayList.add("Cherry");
    	myArrayList.add(1, "Blueberry"); // insert at index
    	System.out.println("ArrayList: " + myArrayList);
    	System.out.println("get(2): " + myArrayList.get(2)); // O(1)
    	
        List<String> linkedList = new LinkedList<>();
    	linkedList.add("Dog");
    	linkedList.addFirst("Cat");   // Deque method
    	linkedList.addLast("Elephant");
    	System.out.println("LinkedList: " + linkedList);
    	
        // ---------- SET ----------
    	Set<String> hashSet = new HashSet<>();
    	hashSet.add("Zebra");
    	hashSet.add("Apple");
    	hashSet.add("Mango");
    	hashSet.add("Apple"); // duplicate ignored
    	System.out.println("HashSet (no order): " + hashSet);
    	
        Set<String> linkedHashSet = new LinkedHashSet<>();
    	linkedHashSet.add("Zebra");
    	linkedHashSet.add("Apple");
    	linkedHashSet.add("Mango");
    	System.out.println("LinkedHashSet (insertion order): " + linkedHashSet);
    	
        Set<String> treeSet = new TreeSet<>();
    	treeSet.add("Zebra");
    	treeSet.add("Apple");
    	treeSet.add("Mango");
    	System.out.println("TreeSet (sorted): " + treeSet);
    	
        // ---------- MAP ----------
    	Map<Integer, String> hashMap = new HashMap<>();
    	hashMap.put(3, "Three");
    	hashMap.put(1, "One");
    	hashMap.put(2, "Two");
    	hashMap.put(1, "ONE"); // overwrites
    	System.out.println("HashMap: " + hashMap);
    	
        Map<Integer, String> linkedHashMap = new LinkedHashMap<>();
    	linkedHashMap.put(3, "Three");
    	linkedHashMap.put(1, "One");
    	linkedHashMap.put(2, "Two");
    	System.out.println("LinkedHashMap (insertion order): " + linkedHashMap);
    	
        Map<Integer, String> treeMap = new TreeMap<>();
    	treeMap.put(3, "Three");
    	treeMap.put(1, "One");
    	treeMap.put(2, "Two");
    	System.out.println("TreeMap (sorted by key): " + treeMap);
	}
}
