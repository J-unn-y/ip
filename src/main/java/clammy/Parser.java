package clammy;

import java.util.Locale;

/** Converts user input into structured commands and task values. */
public final class Parser {
    private static final String DEADLINE_SEPARATOR = " /by ";
    private static final String EVENT_FROM_SEPARATOR = " /from ";
    private static final String EVENT_TO_SEPARATOR = " /to ";

    private Parser() {
    }

    /**
     * Separates a command keyword from its arguments.
     *
     * @param input Raw user input.
     * @return Structured command containing its type and arguments.
     */
    public static ParsedCommand parse(String input) {
        String normalizedInput = input.trim();
        if (normalizedInput.isEmpty()) {
            return new ParsedCommand(CommandType.UNKNOWN, "");
        }
        String[] parts = normalizedInput.split("\\s+", 2);
        CommandType commandType = CommandType.fromKeyword(parts[0].toLowerCase(Locale.ROOT));
        String arguments = parts.length == 2 ? parts[1].trim() : "";
        return new ParsedCommand(commandType, arguments);
    }

    /**
     * Creates a task from a parsed add command.
     *
     * @param command Parsed todo, deadline, or event command.
     * @return Task represented by the command.
     * @throws ParseException If a required task field or delimiter is missing.
     */
    public static Task parseTask(ParsedCommand command) throws ParseException {
        return switch (command.type()) {
        case TODO -> parseTodo(command.arguments());
        case DEADLINE -> parseDeadline(command.arguments());
        case EVENT -> parseEvent(command.arguments());
        default -> throw new ParseException("That command does not create a task.");
        };
    }

    /**
     * Returns the task number in a mark or unmark command.
     *
     * @param arguments Text following the command keyword.
     * @return Positive task number supplied by the user.
     * @throws ParseException If the argument is not a positive integer.
     */
    public static int parseTaskNumber(String arguments) throws ParseException {
        try {
            int taskNumber = Integer.parseInt(arguments);
            if (taskNumber < 1) {
                throw new ParseException("Please provide a positive task number.");
            }
            return taskNumber;
        } catch (NumberFormatException exception) {
            throw new ParseException("Please provide a valid task number.");
        }
    }

    private static Todo parseTodo(String description) throws ParseException {
        if (description.isBlank()) {
            throw new ParseException("A todo must have a description.");
        }
        return new Todo(description);
    }

    private static Deadline parseDeadline(String arguments) throws ParseException {
        int byIndex = arguments.indexOf(DEADLINE_SEPARATOR);
        if (byIndex <= 0 || byIndex + DEADLINE_SEPARATOR.length() >= arguments.length()) {
            throw new ParseException("A deadline must follow: deadline DESCRIPTION /by DATE_OR_TIME");
        }
        String description = arguments.substring(0, byIndex).trim();
        String by = arguments.substring(byIndex + DEADLINE_SEPARATOR.length()).trim();
        return new Deadline(description, by);
    }

    private static Event parseEvent(String arguments) throws ParseException {
        int fromIndex = arguments.indexOf(EVENT_FROM_SEPARATOR);
        int toIndex = arguments.indexOf(EVENT_TO_SEPARATOR,
                fromIndex + EVENT_FROM_SEPARATOR.length());
        boolean hasMissingField = fromIndex <= 0
                || toIndex <= fromIndex + EVENT_FROM_SEPARATOR.length()
                || toIndex + EVENT_TO_SEPARATOR.length() >= arguments.length();
        if (hasMissingField) {
            throw new ParseException("An event must follow: event DESCRIPTION /from START /to END");
        }
        String description = arguments.substring(0, fromIndex).trim();
        String from = arguments.substring(fromIndex + EVENT_FROM_SEPARATOR.length(), toIndex).trim();
        String to = arguments.substring(toIndex + EVENT_TO_SEPARATOR.length()).trim();
        return new Event(description, from, to);
    }
}
