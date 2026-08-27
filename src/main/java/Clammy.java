import java.util.Scanner;

/**
 * A simple chatbot that stores tasks entered during the current session.
 */
public class Clammy {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = "____________________________________________________________\n";
        String startMessage = "Hello! I'm Clammy.\nWhat can I do for you?\n";
        String endMessage = "Bye. Hope to see you again soon!\n";
        String[] tasks = new String[100];
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
                for (int i = 0; i < taskCount; i++) {
                    System.out.println(" " + (i + 1) + ". " + tasks[i]);
                }
                System.out.print(line);
            } else {
                tasks[taskCount] = command;
                taskCount++;
                System.out.println(line + " added: " + command + "\n" + line);
            }
        }
    }
}
