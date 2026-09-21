// Sealed Classes
// ================================================================
// Java 17+ example: demonstrates a sealed class and its restricted hierarchy.
sealed abstract class Shape permits Circle, Rectangle, Triangle {
    public abstract double area();
}

final class Circle extends Shape {
    private final double radius;

    Circle(double radius) {
        this.radius = radius;
    }

    @Override public double area() {
        return Math.PI * radius * radius;
    }
}

final class Rectangle extends Shape {
    private final double width;
    private final double height;

    Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }

    @Override public double area() {
        return width * height;
    }
}

final class Triangle extends Shape {
    private final double base;
    private final double height;

    Triangle(double base, double height) {
        this.base = base;
        this.height = height;
    }

    @Override public double area() {
        return 0.5 * base * height;
    }
}

public class P7 {
    public static void main(String[] args) {
        Shape[] shapes = {
            new Circle(3),
            new Rectangle(4, 5),
            new Triangle(4, 6)
        };

        for (Shape shape : shapes) {
            System.out.println("Area: " + shape.area());
        }
    }
}