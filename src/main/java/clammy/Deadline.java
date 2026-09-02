package clammy;

/**
 * Represents a task that has a deadline.
 */
public class Deadline extends Task {
    private final String by;

    /**
     * Creates an incomplete task with a deadline.
     *
     * @param description Description of the task.
     * @param by Date or time by which the task should be completed.
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}
