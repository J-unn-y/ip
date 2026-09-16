package clammy;

import java.io.IOException;

import clammy.command.CommandHandler;
import clammy.exception.ClammyException;
import clammy.storage.Storage;
import clammy.task.TaskList;
import clammy.ui.Ui;

/**
 * Coordinates Clammy's user interface and command handling.
 */
public class Clammy {
    private final Ui ui;
    private final Storage storage;

    /** Creates Clammy with its console UI and local task storage. */
    public Clammy() {
        this.ui = new Ui();
        this.storage = new Storage();
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
        TaskList tasks;
        try {
            tasks = storage.load();
        } catch (IOException exception) {
            ui.showError("Could not load data/clammy.txt. " + exception.getMessage()
                    + "\nExisting data was left unchanged. Fix the file or folder and restart Clammy.");
            return;
        }
        CommandHandler commandHandler = new CommandHandler(tasks, storage);
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
