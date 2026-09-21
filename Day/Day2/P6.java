// Enum Implementing Interface
// ================================================================
// Demonstrates an enum implementing an interface and exposing behavior through each constant.
interface RunnableTask {
    void execute();
}

enum Task implements RunnableTask {
    START {
        @Override public void execute() { System.out.println("Starting process..."); }
    },
    PROCESS {
        @Override public void execute() { System.out.println("Processing data..."); }
    },
    STOP {
        @Override public void execute() { System.out.println("Stopping process..."); }
    }
}

public class P6 {
    public static void main(String[] args) {
        for (Task task : Task.values()) {
            System.out.println("Task: " + task);
            task.execute();
        }
    }
}