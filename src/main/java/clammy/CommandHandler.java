package clammy;

/** Executes parsed commands against a task list without performing console I/O. */
public class CommandHandler {
    private static final String VALID_COMMANDS = "I don't recognize that command. Valid commands are:\n"
            + "todo DESCRIPTION\n"
            + "deadline DESCRIPTION /by DATE_OR_TIME\n"
            + "event DESCRIPTION /from START /to END\n"
            + "list\n"
            + "mark TASK_NUMBER\n"
            + "unmark TASK_NUMBER\n"
            + "bye";
    private final TaskList taskList;

    /** Creates a command handler for the supplied task list. */
    public CommandHandler(TaskList taskList) {
        this.taskList = taskList;
    }

    /**
     * Executes one user command.
     *
     * @param input Raw user command.
     * @param ui User interface used to display the result.
     * @return {@code true} when Clammy should stop after this command.
     * @throws ClammyException If the command cannot be completed.
     */
    public boolean execute(String input, Ui ui) throws ClammyException {
        ParsedCommand command = Parser.parse(input);
        String message = switch (command.type()) {
        case BYE -> exit(command.arguments());
        case LIST -> listTasks(command.arguments());
        case MARK -> updateTaskStatus(command.arguments(), true);
        case UNMARK -> updateTaskStatus(command.arguments(), false);
        case TODO, DEADLINE, EVENT -> addTask(command);
        case UNKNOWN -> throw new UnknownCommandException(VALID_COMMANDS);
        };
        ui.showMessage(message);
        return command.type() == CommandType.BYE;
    }

    private String exit(String arguments) throws ParseException {
        requireNoArguments(arguments, "bye");
        return "Bye. Hope to see you again soon!";
    }

    private String listTasks(String arguments) throws ParseException {
        requireNoArguments(arguments, "list");
        StringBuilder message = new StringBuilder("Here are the tasks in your list:");
        for (int i = 0; i < taskList.size(); i++) {
            message.append("\n").append(i + 1).append(".").append(taskList.asList().get(i));
        }
        return message.toString();
    }

    private String addTask(ParsedCommand command) throws ParseException {
        Task task = Parser.parseTask(command);
        taskList.add(task);
        String taskLabel = taskList.size() == 1 ? "task" : "tasks";
        String message = "Got it. I've added this task:\n" + task
                + "\nNow you have " + taskList.size() + " " + taskLabel + " in the list.";
        return message;
    }

    private String updateTaskStatus(String arguments, boolean shouldMarkDone)
            throws ClammyException {
        int taskNumber = Parser.parseTaskNumber(arguments);
        Task task = taskList.getTask(taskNumber);
        if (shouldMarkDone) {
            task.markAsDone();
            return "Nice! I've marked this task as done:\n" + task;
        }
        task.markAsNotDone();
        return "OK, I've marked this task as not done yet:\n" + task;
    }

    private static void requireNoArguments(String arguments, String commandWord)
            throws ParseException {
        if (!arguments.isEmpty()) {
            throw new ParseException("The " + commandWord + " command does not take arguments.");
        }
    }
}
