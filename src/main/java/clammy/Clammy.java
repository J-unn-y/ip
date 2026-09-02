package clammy;

import java.util.Scanner;

/**
 * Stores and manages tasks entered during the current chatbot session.
 */
public class Clammy {
    private static final int MAX_TASKS = 100;
    private static final String LINE = "____________________________________________________________\n";
    private static final String START_MESSAGE = "Hello! I'm Clammy.\nWhat can I do for you?\n";
    private static final String END_MESSAGE = "Bye. Hope to see you again soon!\n";

    private final Scanner scanner = new Scanner(System.in);
    private final Task[] tasks = new Task[MAX_TASKS];
    private int taskCount = 0;

    /**
     * Runs the chatbot and responds to commands entered through standard input.
     *
     * @param args Command-line arguments supplied to the program.
     */
    public static void main(String[] args) {
        new Clammy().run();
    }

    /**
     * Reads and processes commands until the user exits the chatbot.
     */
    private void run() {
        System.out.println(LINE + START_MESSAGE + LINE);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            boolean shouldContinue = executeCommand(command);
            if (!shouldContinue) {
                break;
            }
        }
    }

    /**
     * Executes a command and indicates whether the chatbot should continue running.
     *
     * @param command Command entered by the user.
     * @return {@code false} when the user exits, and {@code true} otherwise.
     */
    private boolean executeCommand(String command) {
        if (command.equals("bye")) {
            System.out.print(LINE + END_MESSAGE + LINE);
            return false;
        } else if (command.equals("list")) {
            printTaskList();
        } else if (command.startsWith("unmark ")) {
            updateTaskStatus(command, false);
        } else if (command.startsWith("mark ")) {
            updateTaskStatus(command, true);
        } else if (isAddCommand(command)) {
            addTask(command);
        } else {
            System.out.print(LINE + "invalid input\n" + LINE);
        }

        return true;
    }

    /**
     * Prints all tasks in their insertion order.
     */
    private void printTaskList() {
        System.out.print(LINE);
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < taskCount; i++) {
            System.out.println((i + 1) + "." + tasks[i]);
        }
        System.out.print(LINE);
    }

    /**
     * Updates a task's completion status and reports the change.
     *
     * @param command Mark or unmark command containing a task number.
     * @param shouldMarkDone Whether the task should be marked as completed.
     */
    private void updateTaskStatus(String command, boolean shouldMarkDone) {
        String commandPrefix = shouldMarkDone ? "mark " : "unmark ";
        int taskNumber = Integer.parseInt(command.substring(commandPrefix.length()));
        int taskIndex = taskNumber - 1;

        if (shouldMarkDone) {
            tasks[taskIndex].markAsDone();
            System.out.print(LINE
                    + "Nice! I've marked this task as done:\n"
                    + tasks[taskIndex] + "\n"
                    + LINE);
        } else {
            tasks[taskIndex].markAsNotDone();
            System.out.print(LINE
                    + "OK, I've marked this task as not done yet:\n"
                    + tasks[taskIndex] + "\n"
                    + LINE);
        }
    }

    /**
     * Adds the task represented by a command and reports the updated task count.
     *
     * @param command Command representing the task to add.
     */
    private void addTask(String command) {
        Task task = createTask(command);
        tasks[taskCount] = task;
        taskCount++;

        System.out.println(LINE + "Got it. I've added this task:\n" + task);
        System.out.print("Now you have " + taskCount + " task in the list.\n" + LINE);
    }

    /**
     * Returns whether a command represents a supported task type.
     *
     * @param command Command entered by the user.
     * @return {@code true} for todo, deadline, and event commands.
     */
    private static boolean isAddCommand(String command) {
        return command.startsWith("todo ")
                || command.startsWith("deadline ")
                || command.startsWith("event ");
    }

    /**
     * Creates a task by parsing the details from a supported task command.
     *
     * @param command Command containing a todo, deadline, or event.
     * @return Task represented by the command.
     */
    private static Task createTask(String command) {
        if (command.startsWith("todo ")) {
            String description = command.substring("todo ".length());
            return new Todo(description);
        } else if (command.startsWith("deadline ")) {
            String taskDetails = command.substring("deadline ".length());
            String[] descriptionAndBy = taskDetails.split(" /by ", 2);
            String description = descriptionAndBy[0];
            String by = descriptionAndBy[1];
            return new Deadline(description, by);
        } else if (command.startsWith("event ")) {
            String taskDetails = command.substring("event ".length());
            String[] descriptionAndTime = taskDetails.split(" /from ", 2);
            String description = descriptionAndTime[0];
            String[] fromAndTo = descriptionAndTime[1].split(" /to ", 2);
            String from = fromAndTo[0];
            String to = fromAndTo[1];
            return new Event(description, from, to);
        } else {
            throw new IllegalArgumentException("Unsupported task command: " + command);
        }
    }
}
