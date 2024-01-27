package views.cli.io;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class InputStandardTest {

    private final PrintStream stdout = System.out;
    private final InputStream stdin = System.in;

    private final PipedOutputStream mockStdoutData = new PipedOutputStream();
    private final PipedOutputStream mockStdinData = new PipedOutputStream();

    private final PrintStream mockStdout = new PrintStream(mockStdoutData);
    private final InputStream mockStdin = new PipedInputStream(mockStdinData);

    private final Reader mockStdoutReader = new InputStreamReader(new PipedInputStream(mockStdoutData));
    private final PrintWriter mockStdinWriter = new PrintWriter(new OutputStreamWriter(mockStdinData));

    InputStandardTest() throws IOException {
    }

    @BeforeEach
    void setUp() {
        System.setOut(mockStdout);
        System.setIn(mockStdin);
    }

    @AfterEach
    void tearDown() {
        System.setIn(mockStdin);
        System.setOut(mockStdout);
    }

    @Test
    void input() throws IOException {
        Input input = new InputStandard();
        String prompt = "foo";
        String userInput = "bar";
        mockStdinWriter.println(userInput);
        mockStdinWriter.flush();
        String result = input.input(prompt);
        assertEquals(result, userInput);
        StringBuilder output = new StringBuilder();
        while (mockStdoutReader.ready()) {
            output.append((char) mockStdoutReader.read());
        }
        assertEquals(output.toString(), prompt);
    }
}