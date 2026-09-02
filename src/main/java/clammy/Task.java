package clammy;

/**
 * Represents a general task and whether it has been completed.
 */
public class Task {
    private static int totalTask = 0;

    private final String description;
    private boolean isDone;

    /**
     * Creates an incomplete task with the given description.
     *
     * @param description Description of the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
        totalTask++;
    }

    /**
     * Returns the icon used to display the task's completion status.
     *
     * @return {@code "X"} if the task is done, or a space otherwise.
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Marks this task as completed.
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Marks this task as incomplete.
     */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns the number of tasks created during this program run.
     *
     * @return Number of tasks created.
     */
    public static int getTotalTask() {
        return totalTask;
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
