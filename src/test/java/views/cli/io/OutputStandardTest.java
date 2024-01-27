package views.cli.io;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class OutputStandardTest {
    private final PrintStream stdout = System.out;

    private final PipedOutputStream mockStdoutData = new PipedOutputStream();

    private final PrintStream mockStdout = new PrintStream(mockStdoutData);

    private final Reader mockStdoutReader = new InputStreamReader(new PipedInputStream(mockStdoutData));

    OutputStandardTest() throws IOException {
    }

    @BeforeEach
    void setup() {
        System.setOut(mockStdout);
    }

    @AfterEach
    void tearDown() {
        System.setOut(stdout);
    }

    @Test
    void print() throws IOException {
        String body = "foo";
        Output output = new OutputStandard();
        output.print(body);
        StringBuilder outputBuilder = new StringBuilder();
        while (mockStdoutReader.ready()) {
            outputBuilder.append((char) mockStdoutReader.read());
        }
        assertEquals(outputBuilder.toString(), body);
    }
}