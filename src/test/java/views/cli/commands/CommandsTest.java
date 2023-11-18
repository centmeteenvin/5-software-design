package views.cli.commands;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CommandsTest {

    @Test
    void parse() {
        Optional<Command> result = Commands.parse(new String[]{CommandHelp.commandString}, null, null);
        assertTrue(result.isPresent());
        assertTrue(result.get() instanceof CommandHelp);
    }
}