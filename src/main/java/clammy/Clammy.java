package clammy;

import java.util.Scanner;

/**
 * Stores and manages tasks entered during the current chatbot session.
 */
public class Clammy {
    /**
     * Runs the chatbot and responds to commands entered through standard input.
     *
     * @param args Command-line arguments supplied to the program.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = "____________________________________________________________\n";
        String startMessage = "Hello! I'm Clammy.\nWhat can I do for you?\n";
        String endMessage = "Bye. Hope to see you again soon!\n";
        Task[] tasks = new Task[100];
        int taskCount = 0;

        System.out.println(line + startMessage + line);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            if (command.equals("bye")) {
                System.out.print(line + endMessage + line);
                break;
            }

            if (command.equals("list")) {
                System.out.print(line);
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + "." + tasks[i]);
                }
                System.out.print(line);
            } else if (command.startsWith("unmark ")) {
                int taskNumber = Integer.parseInt(command.substring(7));
                int taskIndex = taskNumber - 1;
                tasks[taskIndex].markAsNotDone();
                System.out.print(line
                        + "OK, I've marked this task as not done yet:\n"
                        + tasks[taskIndex] + "\n"
                        + line);
            } else if (command.startsWith("mark ")) {
                int taskNumber = Integer.parseInt(command.substring(5));
                int taskIndex = taskNumber - 1;
                tasks[taskIndex].markAsDone();
                System.out.print(line
                        + "Nice! I've marked this task as done:\n"
                        + tasks[taskIndex] + "\n"
                        + line);
            } else {
                if (command.startsWith("todo ")) {
                    String description = command.substring(5);
                    tasks[taskCount] = new Todo(description);
                } else if (command.startsWith("deadline ")) {
                    String taskDetails = command.substring(9);
                    String[] descriptionAndBy = taskDetails.split(" /by ", 2);
                    String description = descriptionAndBy[0];
                    String by = descriptionAndBy[1];
                    tasks[taskCount] = new Deadline(description, by);
                } else if (command.startsWith("event ")) {
                    String taskDetails = command.substring(6);
                    String[] descriptionAndTime = taskDetails.split(" /from ", 2);
                    String description = descriptionAndTime[0];
                    String[] fromAndTo = descriptionAndTime[1].split(" /to ", 2);
                    String from = fromAndTo[0];
                    String to = fromAndTo[1];
                    tasks[taskCount] = new Event(description, from, to);
                } else {
                    System.out.print(line + "invalid input\n" + line);
                    continue;
                }
                System.out.println(line + "Got it. I've added this task:\n" + tasks[taskCount]);
                System.out.print("Now you have " + Task.getTotalTask() + " task in the list.\n" + line);
                taskCount++;
            }
        }
    }
}
