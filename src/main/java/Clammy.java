import java.util.Scanner;

public class Clammy {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = "____________________________________________________________\n";
        String startMessage = "Hello! I'm Clammy.\nWhat can I do for you?\n";
        String endMessage = "Bye. Hope to see you again soon!\n";

        System.out.println(line + startMessage + line);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            if (command.equals("bye")) {
                System.out.println(line + endMessage + line);
                break;
            }
            System.out.println(line + command + "\n" + line);
        }
    }
}
