//package org.td024.console.util;

//import org.td024.exception.InvalidInputException;

//import java.math.BigDecimal;
//import java.util.InputMismatchException;
//import java.util.Scanner;

/*public final class ConsoleReader {
    private static final Scanner scanner = new Scanner(System.in);

    public static String readLine() {
        return scanner.nextLine();
    }

    public static String readLine(String prompt) {
        System.out.print(prompt);
        return readLine();
    }

    public static int readInt() throws InvalidInputException {
        int value;

        try {
            value = scanner.nextInt();
        } catch (InputMismatchException e) {
            throw new InvalidInputException("Invalid input!");
        }

        scanner.nextLine();
        return value;
    }

    public static int readInt(String prompt) throws InvalidInputException {
        System.out.print(prompt);
        return readInt();
    }

    public static BigDecimal readBigDecimal() throws InvalidInputException {
        BigDecimal value;
        try {
            value = scanner.nextBigDecimal();
        } catch (Exception e) {
            throw new InvalidInputException("Invalid input!");
        }
        scanner.nextLine();
        return value;
    }

    public static BigDecimal readBigDecimal(String prompt) throws InvalidInputException {
        System.out.print(prompt);
        return readBigDecimal();
    }
}*/
