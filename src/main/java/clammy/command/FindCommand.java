package clammy.command;

import clammy.storage.Storage;
import clammy.task.TaskList;
import clammy.ui.Ui;

/**
 * Displays tasks whose descriptions contain the search text.
 */
public class FindCommand extends Command {
    private final String keyword;

    /**
     * Creates a search command with nonblank text validated by the parser.
     *
     * @param keyword Text to match in task descriptions, ignoring case.
     */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    /**
     * Displays matching tasks without changing the task list or saved data.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showMatchingTasks(tasks.findTasks(keyword));
    }
}
