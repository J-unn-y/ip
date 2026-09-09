package clammy;

/**
 * Coordinates Clammy's user interface and command handling.
 */
public class Clammy {
    private final Ui ui;
    private final CommandHandler commandHandler;

    /** Creates Clammy with its console UI and an empty task list. */
    public Clammy() {
        this.ui = new Ui();
        this.commandHandler = new CommandHandler(new TaskList());
    }

    /**
     * Starts the command-line application.
     *
     * @param args Command-line arguments supplied to the program.
     */
    public static void main(String[] args) {
        new Clammy().run();
    }

    /** Reads and executes commands until the user exits or input ends. */
    public void run() {
        ui.showWelcome();
        while (ui.hasNextCommand()) {
            try {
                boolean shouldExit = commandHandler.execute(ui.readCommand(), ui);
                if (shouldExit) {
                    return;
                }
            } catch (ClammyException exception) {
                ui.showError(exception.getMessage());
            }
        }
    }
}
