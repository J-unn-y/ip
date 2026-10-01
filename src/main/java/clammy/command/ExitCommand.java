package clammy.command;

import clammy.storage.Storage;
import clammy.task.TaskList;
import clammy.ui.Ui;

/**
 * Displays a farewell and signals that the application should exit.
 */
public class ExitCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showGoodbye();
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
