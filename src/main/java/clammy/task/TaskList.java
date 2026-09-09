package clammy.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import clammy.exception.TaskNotFoundException;

/** Stores tasks and translates user-facing task numbers into list indexes. */
public class TaskList {
    private final List<Task> tasks = new ArrayList<>();

    /** Adds a task to the end of the list. */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Returns the task identified by a one-based task number.
     *
     * @param taskNumber Task number shown to the user.
     * @return Task with the supplied number.
     * @throws TaskNotFoundException If the number does not identify a stored task.
     */
    public Task getTask(int taskNumber) throws TaskNotFoundException {
        if (!hasTaskNumber(taskNumber)) {
            throw new TaskNotFoundException();
        }
        return tasks.get(taskNumber - 1);
    }

    /** Returns whether a one-based number identifies a stored task. */
    public boolean hasTaskNumber(int taskNumber) {
        return taskNumber >= 1 && taskNumber <= tasks.size();
    }

    /** Returns the number of stored tasks. */
    public int size() {
        return tasks.size();
    }

    /** Returns an unmodifiable view of tasks in insertion order. */
    public List<Task> asList() {
        return Collections.unmodifiableList(tasks);
    }
}
