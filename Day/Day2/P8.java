// Sealed Interfaces
// ================================================================
// Java 17+ example: demonstrates a sealed interface restricting which classes may implement it.
sealed interface PaymentMethod permits CreditCard, DebitCard, PayPal {
    void processPayment(double amount);
}

final class CreditCard implements PaymentMethod {
    @Override public void processPayment(double amount) {
        System.out.println("Processing credit card payment: $" + amount);
    }
}

final class DebitCard implements PaymentMethod {
    @Override public void processPayment(double amount) {
        System.out.println("Processing debit card payment: $" + amount);
    }
}

final class PayPal implements PaymentMethod {
    @Override public void processPayment(double amount) {
        System.out.println("Processing PayPal payment: $" + amount);
    }
}

public class P8 {
    public static void main(String[] args) {
        PaymentMethod[] methods = {
            new CreditCard(),
            new DebitCard(),
            new PayPal()
        };

        for (PaymentMethod method : methods) {
            method.processPayment(150.00);
        }
    }
}
