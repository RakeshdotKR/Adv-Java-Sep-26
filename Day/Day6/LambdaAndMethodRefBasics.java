import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Demonstrates:
 *   Example 1 - Lambda expressions with different functional interfaces.
 *   Example 2 - All four kinds of method references (::).
 */
public class LambdaAndMethodRefBasics {

    // A simple class used later for constructor reference (ClassName::new)
    static class Person {
        String name;
        Person(String name) {
            this.name = name;
            System.out.println("  [Person created: " + name + "]");
        }
        public String getName() { return name; }

        // A static method (for static method reference)
        static String greet(String who) {
            return "Hello, " + who + "!";
        }

        // An instance method (for unbound instance method reference)
        public String shout() {
            return name.toUpperCase() + "!!!";
        }
    }

    // ---------------- Example 1: Lambda expressions ----------------
    static void example1_lambdas() {
        System.out.println("=== Example 1: Lambda expressions ===");

        // Lambda implementing Function<T,R>:  T -> R
        Function<Integer, Integer> square = x -> x * x;
        System.out.println("square(5)          = " + square.apply(5));

        // Lambda implementing Predicate<T>:  T -> boolean
        Predicate<Integer> isEven = n -> n % 2 == 0;
        System.out.println("isEven(4)          = " + isEven.test(4));
        System.out.println("isEven(7)          = " + isEven.test(7));

        // Lambda implementing Consumer<T>:  T -> void (side-effect only)
        Consumer<String> printer = s -> System.out.println("  consumed: " + s);
        printer.accept("lambda");

        // Lambda implementing Supplier<T>:  () -> T  (no input)
        Supplier<Double> randomVal = () -> Math.random();
        System.out.println("supplied random    = " + randomVal.get());

        // Lambda implementing BiFunction<T,U,R>:  (T,U) -> R
        BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b;
        System.out.println("add(3,4)           = " + add.apply(3, 4));

        // Lambda with block body (multiple statements) and explicit return
        Function<String, String> decorate = s -> {
            String trimmed = s.trim();
            return "<<" + trimmed + ">>";
        };
        System.out.println("decorate('  hi ')  = " + decorate.apply("  hi "));
    }

    // ---------------- Example 2: All four method-reference kinds ----------------
    static void example2_methodReferences() {
        System.out.println("\n=== Example 2: Method references (::) ===");

        // (a) Static method reference:  ClassName::staticMethod
        //     Lambda equivalent:  s -> Person.greet(s)
        Function<String, String> greeter = Person::greet;
        System.out.println("static ref        -> " + greeter.apply("World"));

        // (b) Bound instance method reference:  instance::method
        //     The instance is fixed ("target"); remaining args come from lambda params.
        Person alice = new Person("Alice");
        Supplier<String> aliceName = alice::getName; // () -> alice.getName()
        System.out.println("bound ref         -> " + aliceName.get());

        // (c) Unbound instance method reference:  ClassName::instanceMethod
        //     The first lambda parameter becomes the receiver.
        //     Lambda equivalent:  p -> p.shout()
        Function<Person, String> shouter = Person::shout;
        System.out.println("unbound ref       -> " + shouter.apply(alice));

        // (d) Constructor reference:  ClassName::new
        //     Lambda equivalent:  name -> new Person(name)
        Function<String, Person> personFactory = Person::new;
        Person bob = personFactory.apply("Bob");
        System.out.println("ctor ref          -> created " + bob.getName());

        // Bonus: method reference on a static utility in JDK
        //   Integer::parseInt   ==  s -> Integer.parseInt(s)
        Function<String, Integer> parser = Integer::parseInt;
        System.out.println("Integer::parseInt -> " + parser.apply("42"));
    }

    public static void main(String[] args) {
        example1_lambdas();
        example2_methodReferences();
    }
}
