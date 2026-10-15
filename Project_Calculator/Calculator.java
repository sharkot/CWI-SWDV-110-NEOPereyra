import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

public class Calculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Double> results = new ArrayList<>();

        System.out.println("Welcome to Neo's Calculator!");
        System.out.print("Choose a number type (int, float, or double): ");
        String type = scanner.nextLine().trim().toLowerCase();

        System.out.print("Enter the first number: ");
        String firstInput = scanner.nextLine();
        System.out.print("Enter the second number: ");
        String secondInput = scanner.nextLine();

        double num1;
        double num2;

        try {
            switch (type) {
                case "int":
                    num1 = Integer.parseInt(firstInput);
                    num2 = Integer.parseInt(secondInput);
                    break;
                case "float":
                    num1 = Float.parseFloat(firstInput);
                    num2 = Float.parseFloat(secondInput);
                    break;
                case "double":
                    num1 = Double.parseDouble(firstInput);
                    num2 = Double.parseDouble(secondInput);
                    break;
                default:
                    System.out.println("Please choose int, float, or double.");
                    return;
            }
        } catch (NumberFormatException e) {
            System.out.println("That's not a valid number. Try running the program again.");
            return;
        }

        if (!Double.isFinite(num1) || !Double.isFinite(num2)) {
            System.out.println("Please enter regular, finite numbers.");
            return;
        }

        if (num2 == 0) {
            System.out.println("You can't divide by zero. Try another second number.");
            return;
        }

        results.add(num1 + num2);
        results.add(num1 - num2);
        results.add(num1 / num2);
        results.add(num1 * num2);

        String[] symbols = {"+", "-", "/", "*"};

        try (FileWriter writer = new FileWriter("calculations.txt")) {
            System.out.println("\nYour results:");

            for (int i = 0; i < results.size(); i++) {
                String line = num1 + " " + symbols[i] + " " + num2 + " = " + results.get(i);
                System.out.println(line);
                writer.write(line + System.lineSeparator());
            }

            System.out.println("\nSaved all four calculations to calculations.txt!");
        } catch (IOException e) {
            System.out.println("Couldn't save the file: " + e.getMessage());
        }

        scanner.close();
    }
}
