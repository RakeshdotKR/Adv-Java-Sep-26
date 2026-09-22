import java.util.*;
class Employee {
	int id;
	String name;
	Employee(int id, String name) { this.id = id; this.name = name; }
	@Override
	public boolean equals(Object o) {
    	if (this == o) return true;
    	if (!(o instanceof Employee)) return false;
    	Employee e = (Employee) o;
    	return id == e.id && Objects.equals(name, e.name);
	}
	@Override
	public int hashCode() { return Objects.hash(id, name); }
	@Override
	public String toString() { return id + ":" + name; }
}
public class P2 {
	public static void main(String[] args) {
    	Set<Employee> set = new HashSet<>();
    	set.add(new Employee(1, "Alice"));
    	set.add(new Employee(1, "Alice")); // duplicate (equals+hashCode)
    	set.add(new Employee(2, "Bob"));
    	System.out.println("Set size: " + set.size()); // 2
    	System.out.println(set);
    	
		Map<Employee, String> map = new HashMap<>();
    	map.put(new Employee(1, "Alice"), "HR");
    	System.out.println("Lookup: " + map.get(new Employee(1, "Alice"))); // HR
	}
}
