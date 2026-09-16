package Day.Day1;

public interface A {

	default void show() {

        System.out.println("From A");

	}

}

interface B {

	default void show() {

        System.out.println("From B");

	}

}

class C implements A, B {

	@Override

	public void show() {

    	// Or: B.super.show();

	}

} 
