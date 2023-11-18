package views.cli.io;

public interface Input {

    /**
     * Gets input from the end-user.
     * @param prompt will be sent first before the input is fetched. no extra characters will be added to prompt.
     * @return a string with the user input. The prompt is NOT part of this input.
     */
    String input(String prompt);
}
