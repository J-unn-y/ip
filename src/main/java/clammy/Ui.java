package clammy;

import java.util.Scanner;

/** Reads console input and displays Clammy's user-facing messages. */
public class Ui {
    private static final String LINE = "____________________________________________________________";
    private static final String START_MESSAGE = "Hello! I'm Clammy.\nWhat can I do for you?";
    private final Scanner scanner;

    /** Creates a UI connected to standard input. */
    public Ui() {
        this(new Scanner(System.in));
    }

    /** Creates a UI with a supplied scanner, allowing controlled test input. */
    public Ui(Scanner scanner) {
        this.scanner = scanner;
    }

    /** Returns whether another command is available. */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /** Reads and returns the next command. */
    public String readCommand() {
        return scanner.nextLine();
    }

    /** Displays Clammy's greeting. */
    public void showWelcome() {
        System.out.println(LINE + "\n" + START_MESSAGE + "\n" + LINE + "\n");
    }

    /** Displays a message produced by executing a command. */
    public void showMessage(String message) {
        System.out.println(LINE + "\n" + message + "\n" + LINE + "\n");
    }

    /** Displays a parsing error without terminating the application. */
    public void showError(String message) {
        System.out.println(LINE + "\n" + message + "\n" + LINE + "\n");
    }
}
