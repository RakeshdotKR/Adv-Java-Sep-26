package Day.Day1;
abstract class Animal {

	protected String name;

	protected int age;

	public Animal(String name, int age) {

    	this.name = name;

    	this.age = age;

	}

	public void sleep() {

        System.out.println(name + " is sleeping");

	}

	public abstract void makeSound();

}

interface Swimmable {

	void swim();

	default void dive() {

        System.out.println("Diving underwater");

	}

}

class Fish extends Animal implements Swimmable {

	public Fish(String name, int age) {

    	super(name, age);

	}

	@Override

	public void makeSound() {

        System.out.println("Fish makes bubbles");

	}

	@Override

	public void swim() {

        System.out.println(name + " is swimming");

	}

}
public interface MyInterface {

	void requiredMethod();

	default void optionalMethod() {

        System.out.println("Default implementation");

	}

}

interface Vehicle {

	void start();  // Abstract method

	default void stop() {

        System.out.println("Vehicle is stopping");

	}

	default void honk() {

        System.out.println("Beep beep!");

	}

}

class Car implements Vehicle {

	@Override

	public void start() {

        System.out.println("Car engine started");

	}

}

class Bike implements Vehicle {

	@Override

	public void start() {

    	System.out.println("Bike engine started");

	}

	@Override

	public void honk() {

        System.out.println("Bike horn: Ting ting!");

	}

}