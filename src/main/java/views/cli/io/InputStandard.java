package views.cli.io;

import java.util.Scanner;

public class InputStandard implements Input {

    private final Scanner scanner = new Scanner(System.in);
    /**
     * Gets input from the end-user.
     *
     * @param prompt will be sent first before the input is fetched. no extra characters will be added to prompt.
     * @return a string with the user input. The prompt is NOT part of this input.
     */
    @Override
    public String input(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
}
