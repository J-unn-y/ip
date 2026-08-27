package clammy;

import java.util.Scanner;

/**
 * Stores tasks entered during the current chatbot session.
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
                System.out.println(line + endMessage + line);
                break;
            }

            if (command.equals("list")) {
                System.out.print(line);
                System.out.println(" Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println(" " + (i + 1) + "." + tasks[i]);
                }
                System.out.print(line);
            } else if (command.startsWith("unmark ")) {
                int taskNumber = Integer.parseInt(command.substring(7));
                int taskIndex = taskNumber - 1;
                tasks[taskIndex].markAsNotDone();
                System.out.println(line
                        + " OK, I've marked this task as not done yet:\n"
                        + "   " + tasks[taskIndex] + "\n"
                        + line);
            } else if (command.startsWith("mark ")) {
                int taskNumber = Integer.parseInt(command.substring(5));
                int taskIndex = taskNumber - 1;
                tasks[taskIndex].markAsDone();
                System.out.println(line
                        + " Nice! I've marked this task as done:\n"
                        + "   " + tasks[taskIndex] + "\n"
                        + line);
            } else {
                tasks[taskCount] = new Task(command);
                taskCount++;
                System.out.println(line + " added: " + command + "\n" + line);
            }
        }
    }
}
