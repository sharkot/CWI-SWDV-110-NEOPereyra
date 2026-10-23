import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class ToDoListManager {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<String> tasks = new ArrayList<>();
        boolean running = true;

        System.out.println("Welcome to your To-do List Manager!");

        while (running) {
            System.out.println("\nCommands: add, remove, list, done, sort, move, quit");
            System.out.print("Enter a command: ");
            String input = scanner.nextLine().trim();
            String[] parts = input.split("\\s+");
            String command = parts[0].toLowerCase();

            switch (command) {
                case "add":
                    System.out.print("What task do you want to add? ");
                    String task = scanner.nextLine().trim();
                    if (task.isEmpty()) {
                        System.out.println("Please enter a task first.");
                    } else {
                        tasks.add(task);
                        System.out.println("Task added!");
                    }
                    break;

                case "list":
                    if (tasks.isEmpty()) {
                        System.out.println("Your to-do list is empty.");
                    } else {
                        System.out.println("Your tasks:");
                        for (int i = 0; i < tasks.size(); i++) {
                            System.out.println((i + 1) + ". " + tasks.get(i));
                        }
                    }
                    break;

                case "remove":
                case "done":
                    try {
                        int number;
                        if (parts.length > 1) {
                            number = Integer.parseInt(parts[1]);
                        } else {
                            System.out.print("Enter the task number: ");
                            number = Integer.parseInt(scanner.nextLine().trim());
                        }

                        if (number < 1 || number > tasks.size()) {
                            System.out.println("That task number doesn't exist. Try another one!");
                        } else {
                            String removedTask = tasks.remove(number - 1);
                            if (command.equals("done")) {
                                System.out.println("Nice job! Completed: " + removedTask);
                            } else {
                                System.out.println("Removed: " + removedTask);
                            }
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Please enter a valid task number.");
                    }
                    break;

                case "sort":
                    Collections.sort(tasks);
                    System.out.println("Tasks sorted alphabetically!");
                    break;

                case "move":
                    try {
                        int from;
                        int to;
                        if (parts.length >= 3) {
                            from = Integer.parseInt(parts[1]);
                            to = Integer.parseInt(parts[2]);
                        } else {
                            System.out.print("Task number to move: ");
                            from = Integer.parseInt(scanner.nextLine().trim());
                            System.out.print("Move it to number: ");
                            to = Integer.parseInt(scanner.nextLine().trim());
                        }
                        move(tasks, from, to);
                    } catch (NumberFormatException e) {
                        System.out.println("Please enter valid task numbers.");
                    }
                    break;

                case "quit":
                    running = false;
                    System.out.println("Goodbye! See you next time!");
                    break;

                default:
                    System.out.println("I don't know that command. Please try again!");
            }
        }
        scanner.close();
    }

    public static void move(List<String> tasks, int indexToMoveFrom, int indexToMoveTo) {
        if (indexToMoveFrom < 1 || indexToMoveFrom > tasks.size()
                || indexToMoveTo < 1 || indexToMoveTo > tasks.size()) {
            System.out.println("That task number doesn't exist. Try another one!");
        } else {
            String task = tasks.remove(indexToMoveFrom - 1);
            tasks.add(indexToMoveTo - 1, task);
            System.out.println("Task moved!");
        }
    }
}
