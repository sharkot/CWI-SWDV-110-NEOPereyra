import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class ToDoListManager {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<String> tasks = new ArrayList<>();

        System.out.println("My To-Do List :)");

        while (true) {
            System.out.print("\nadd, remove, list, done, sort, move, quit: ");
            String input = sc.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("You gotta type something lol");
                continue;
            }

            String[] words = input.split("\\s+");
            String command = words[0].toLowerCase();

            switch (command) {
                case "add":
                    System.out.print("New task: ");
                    String task = sc.nextLine().trim();
                    if (task.isEmpty()) {
                        System.out.println("Can't add nothing 😭");
                    } else {
                        tasks.add(task);
                        System.out.println("Added!");
                    }
                    break;

                case "list":
                    if (tasks.isEmpty()) {
                        System.out.println("No tasks yet, nice.");
                    } else {
                        for (int i = 0; i < tasks.size(); i++) {
                            System.out.println((i + 1) + ". " + tasks.get(i));
                        }
                    }
                    break;

                case "remove":
                case "done":
                    try {
                        String num = words.length > 1 ? words[1] : "";
                        if (num.isEmpty()) {
                            System.out.print("Task number: ");
                            num = sc.nextLine();
                        }
                        int index = Integer.parseInt(num.trim()) - 1;

                        if (index < 0 || index >= tasks.size()) {
                            System.out.println("That task number isn't on the list.");
                        } else {
                            String removed = tasks.remove(index);
                            if (command.equals("done")) {
                                System.out.println("Done with " + removed + ". W!");
                            } else {
                                System.out.println("Removed " + removed);
                            }
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("That's not a number lol");
                    }
                    break;

                case "sort":
                    Collections.sort(tasks);
                    System.out.println("Sorted!");
                    break;

                case "move":
                    try {
                        String fromText = words.length > 1 ? words[1] : "";
                        String toText = words.length > 2 ? words[2] : "";
                        if (fromText.isEmpty()) {
                            System.out.print("Move task #: ");
                            fromText = sc.nextLine();
                        }
                        if (toText.isEmpty()) {
                            System.out.print("Move to #: ");
                            toText = sc.nextLine();
                        }
                        move(tasks, Integer.parseInt(fromText.trim()),
                                Integer.parseInt(toText.trim()));
                    } catch (NumberFormatException e) {
                        System.out.println("Use numbers for move.");
                    }
                    break;

                case "quit":
                    System.out.println("Later!");
                    sc.close();
                    return;

                default:
                    System.out.println("Not a command. Try again!");
            }
        }
    }

    // Task numbers start at 1, but list indexes start at 0.
    public static void move(List<String> tasks, int from, int to) {
        if (from < 1 || from > tasks.size() || to < 1 || to > tasks.size()) {
            System.out.println("That task number isn't on the list.");
            return;
        }

        String task = tasks.remove(from - 1);
        tasks.add(to - 1, task);
        System.out.println("Moved!");
    }
}
