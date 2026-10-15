import java.util.Scanner;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;

public class Calculator {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        ArrayList<Double> answers = new ArrayList<Double>();

        System.out.println("Welcome to Neo's Calculator!");
        System.out.print("Pick a number type (int, float, or double): ");
        String type = input.nextLine();

        System.out.print("Enter your first number: ");
        String first = input.nextLine();
        System.out.print("Enter your second number: ");
        String second = input.nextLine();

        double num1 = 0;
        double num2 = 0;

        // Convert the numbers based on what the user picked
        try {
            if (type.equalsIgnoreCase("int")) {
                num1 = Integer.parseInt(first);
                num2 = Integer.parseInt(second);
            } else if (type.equalsIgnoreCase("float")) {
                num1 = Float.parseFloat(first);
                num2 = Float.parseFloat(second);
            } else if (type.equalsIgnoreCase("double")) {
                num1 = Double.parseDouble(first);
                num2 = Double.parseDouble(second);
            } else {
                System.out.println("Please pick int, float, or double.");
                input.close();
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Oops, that's not a number. Try again!");
            input.close();
            return;
        }

        if (num2 == 0) {
            System.out.println("You can't divide by zero!");
            input.close();
            return;
        }

        // Do the math and put the answers in an ArrayList
        answers.add(num1 + num2);
        answers.add(num1 - num2);
        answers.add(num1 / num2);
        answers.add(num1 * num2);

        System.out.println("\nHere are your answers:");
        for (int i = 0; i < answers.size(); i++) {
            System.out.println(answers.get(i));
        }

        // Save all four math problems to a text file
        try {
            FileWriter file = new FileWriter("calculations.txt");
            file.write(num1 + " + " + num2 + " = " + answers.get(0) + "\n");
            file.write(num1 + " - " + num2 + " = " + answers.get(1) + "\n");
            file.write(num1 + " / " + num2 + " = " + answers.get(2) + "\n");
            file.write(num1 + " * " + num2 + " = " + answers.get(3) + "\n");
            file.close();
            System.out.println("Saved the calculations to calculations.txt!");
        } catch (IOException e) {
            System.out.println("Something went wrong saving the file.");
        }

        input.close();
    }
}
