package views.cli.io;

public interface Output {
    /**
     * sends the content to the output, NO EXTRA end-line characters are appended.
     * @param content the text that will be sent to the output.
     */
    void print(String content);
}
