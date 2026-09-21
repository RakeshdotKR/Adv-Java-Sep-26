// Enum with Behavior
// ================================================================
// Demonstrates an enum with fields, a constructor, and constant-specific behavior.
enum Operation {
    PLUS("+") {
        @Override double apply(double x, double y) { return x + y; }
    },
    MINUS("-") {
        @Override double apply(double x, double y) { return x - y; }
    },
    TIMES("*") {
        @Override double apply(double x, double y) { return x * y; }
    },
    DIVIDE("/") {
        @Override double apply(double x, double y) {
            if (y == 0) throw new ArithmeticException("Division by zero");
            return x / y;
        }
    };

    private final String symbol;

    Operation(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    abstract double apply(double x, double y);
}

public class P5 {
    public static void main(String[] args) {
        for (Operation operation : Operation.values()) {
            System.out.println(operation.getSymbol() + " -> " + operation.apply(12, 4));
        }
    }
}