package clammy.command;

/**
 * Stores a recognized command type and the text following its keyword.
 *
 * @param type Recognized command type.
 * @param arguments Text following the command keyword.
 */
public record ParsedCommand(CommandType type, String arguments) {
}
