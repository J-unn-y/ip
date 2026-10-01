package clammy.command;

import clammy.storage.Storage;
import clammy.task.TaskList;
import clammy.ui.Ui;

/**
 * Displays the current tasks without changing saved data.
 */
public class ListCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showTaskList(tasks);
    }
}
