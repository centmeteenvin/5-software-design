package views.cli.commands;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

abstract class CommandTest {

    @Test
    void incorrectNumberOfArguments() {
        String expected = """
                ! Incorrect number of arguments
                ! Expected 3 but received 1
                """;
        assertEquals(Command.incorrectNumberOfArguments(3, 1), expected);
    }

    @Test
    void testDescriptions() {
        assertFalse(getCommand().description().isEmpty());
    }

    public abstract Command getCommand();
}