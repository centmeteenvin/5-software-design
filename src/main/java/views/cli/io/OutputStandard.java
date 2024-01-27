package views.cli.io;

public class OutputStandard implements Output{
    /**
     * sends the content to the output, NO EXTRA end-line characters are appended.
     *
     * @param content the text that will be sent to the output.
     */
    @Override
    public void print(String content) {
        System.out.print(content);
    }
}
