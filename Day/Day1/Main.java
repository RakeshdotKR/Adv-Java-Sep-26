package Day.Day1;

interface MathOperations {

	int add(int a, int b);

	static int multiply(int a, int b) {

    	return a * b;

	}

	static int square(int x) {

    	return x * x;

	}

	static int cube(int x) {

    	return multiply(x, square(x));

	}

}

class Calculator implements MathOperations {

	@Override

	public int add(int a, int b) {

    	return a + b;

	}

}

public class Main {

	public static void main(String[] args) {

    	Calculator calc = new Calculator();

    	System.out.println(calc.add(5, 3));  // 8

        System.out.println(MathOperations.multiply(4, 5));  // 20

        System.out.println(MathOperations.square(3));   	// 9

        System.out.println(MathOperations.cube(2));     	// 8

	}

}
