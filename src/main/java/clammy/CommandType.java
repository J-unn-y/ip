package clammy;

/** Identifies a command supported by Clammy. */
public enum CommandType {
    TODO, DEADLINE, EVENT, LIST, MARK, UNMARK, BYE, UNKNOWN;

    /**
     * Returns the command type represented by a keyword.
     *
     * @param keyword First word of a user command.
     * @return Matching command type, or {@link #UNKNOWN} when none matches.
     */
    public static CommandType fromKeyword(String keyword) {
        try {
            return valueOf(keyword.toUpperCase());
        } catch (IllegalArgumentException exception) {
            return UNKNOWN;
        }
    }
}
