package views.cli.commands;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandTest {

    @Test
    void incorrectNumberOfArguments() {
        String expected = """
                ! Incorrect number of arguments
                ! Expected 3 but received 1
                """;
        assertEquals(Command.incorrectNumberOfArguments(3, 1), expected);
    }
}