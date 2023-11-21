package views.cli.commands;

import org.junit.jupiter.api.Test;
import views.cli.ViewCommandLine;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CommandsTest {

    @Test
    void parse() {
        Optional<Command> result = Commands.parse(new String[]{CommandHelp.commandString}, null);
        assertTrue(result.isPresent());
        assertTrue(result.get() instanceof CommandHelp);
    }

    @Test
    void testAllConstructorsPresent() {
        for (Commands command : Commands.values()) {
            assertDoesNotThrow(() -> command.commandClass.getConstructor(), "Forgot to implement empty constructor " + command.commandClass.getName());
            assertDoesNotThrow(() -> command.commandClass.getConstructor(String[].class, ViewCommandLine.class), "Forgot to implement constructor with parameters in " + command.commandClass.getName());
        }
    }
}