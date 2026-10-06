/**
 * CustomHierarchyExample.java  —  Custom exception hierarchies.
 *
 * Good design uses a BASE class per domain so callers can catch
 * broadly (the base) or narrowly (a specific subclass) as needed.
 *
 * Here:
 *   BankingException (checked)      <- business errors callers must handle
 *     ├── InsufficientFundsException
 *     └── AccountFrozenException
 *
 *   ValidationException (unchecked) <- programming bugs / bad input
 *     ├── NegativeAmountException
 *     └── NullAccountException
 *
 * Compile: javac CustomHierarchyExample.java
 * Run    : java CustomHierarchyExample
 */
public class CustomHierarchyExample {

    // ================= CHECKED hierarchy =================

    /** Base CHECKED exception for all banking-related errors. */
    static class BankingException extends Exception {
        public BankingException(String msg) { super(msg); }
    }

    static class InsufficientFundsException extends BankingException {
        private final double shortfall;
        public InsufficientFundsException(double shortfall) {
            super("Insufficient funds: short by " + shortfall);
            this.shortfall = shortfall;
        }
        public double getShortfall() { return shortfall; }
    }

    static class AccountFrozenException extends BankingException {
        public AccountFrozenException(String id) {
            super("Account " + id + " is frozen");
        }
    }

    // ================= UNCHECKED hierarchy =================

    /** Base UNCHECKED exception for validation errors. */
    static class ValidationException extends RuntimeException {
        public ValidationException(String msg) { super(msg); }
    }

    static class NegativeAmountException extends ValidationException {
        public NegativeAmountException(double amt) {
            super("Amount must be positive, got: " + amt);
        }
    }

    static class NullAccountException extends ValidationException {
        public NullAccountException() {
            super("Account must not be null");
        }
    }

    // ================= DOMAIN CLASS =================

    static class BankAccount {
        private final String id;
        private double balance;
        private boolean frozen;

        public BankAccount(String id, double opening) {
            this.id = id;
            this.balance = opening;
        }

        public void freeze() { this.frozen = true; }
        public double getBalance() { return balance; }
        public String getId() { return id; }

        /**
         * Withdraw money.
         *
         * Unchecked (ValidationException subclasses) — thrown for programming bugs.
         * Checked   (BankingException subclasses)    — thrown for business errors.
         */
        public void withdraw(double amount)
                throws InsufficientFundsException, AccountFrozenException {

            // ---- UNCHECKED: bad argument is a caller bug ----
            if (amount <= 0) {
                throw new NegativeAmountException(amount);
            }

            // ---- CHECKED: business rule violation ----
            if (frozen) {
                throw new AccountFrozenException(id);
            }

            // ---- CHECKED: business rule violation ----
            if (amount > balance) {
                throw new InsufficientFundsException(amount - balance);
            }

            balance -= amount;
        }
    }

    // ================= MAIN =================

    public static void main(String[] args) {

        BankAccount acc = new BankAccount("ACC-1", 100.0);

        // --- 1. Checked exception caught by its SPECIFIC subclass ---
        System.out.println("--- 1. Specific catch ---");
        try {
            acc.withdraw(150.0);
        } catch (InsufficientFundsException e) {
            System.out.println("Specific catch: " + e.getMessage());
            System.out.println("Shortfall: " + e.getShortfall());
        } catch (AccountFrozenException e) {
            System.out.println("Frozen: " + e.getMessage());
        }

        // --- 2. Checked exception caught by the BASE class ---
        System.out.println("\n--- 2. Base-class catch ---");
        try {
            BankAccount frozen = new BankAccount("ACC-2", 500.0);
            frozen.freeze();
            frozen.withdraw(10.0);
        } catch (BankingException e) {   // catches ANY banking error
            System.out.println("Handled as BankingException: " + e.getMessage());
        }

        // --- 3. Unchecked exception caught normally ---
        System.out.println("\n--- 3. Unchecked catch ---");
        try {
            acc.withdraw(-5);
        } catch (ValidationException e) {  // base class of custom unchecked
            System.out.println("Validation failed: " + e.getMessage());
        }

        // --- 4. Recovery pattern — catch specific, try a smaller amount ---
        System.out.println("\n--- 4. Recovery ---");
        try {
            acc.withdraw(70.0);   // OK: balance goes 100 -> 30
            System.out.println("Withdrew 70, balance = " + acc.getBalance());
            acc.withdraw(50.0);   // Fails: only 30 left
        } catch (InsufficientFundsException e) {
            System.out.println("First attempt failed: " + e.getMessage());
            System.out.println("Retrying with 20...");
            try {
                acc.withdraw(20.0);
                System.out.println("Retry succeeded, balance = " + acc.getBalance());
            } catch (BankingException inner) {
                System.out.println("Retry failed: " + inner.getMessage());
            }
        } catch (AccountFrozenException e) {
            System.out.println("Frozen: " + e.getMessage());
        }

        System.out.println("\nProgram finished cleanly.");
    }
}