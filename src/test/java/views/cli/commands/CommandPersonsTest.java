package views.cli.commands;

import org.junit.jupiter.api.Test;
import views.cli.ViewCommandLine;
import views.cli.io.Output;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

class CommandPersonsTest {

    @Test
    void execute() {
        String[] args = new String[]{CommandHelp.commandString};
        Output output = mock(Output.class);
        ViewCommandLine view = new ViewCommandLine(
                null, null, null,
                null, null, null,
                null, output);
        Command command = (new CommandPersons(args, view));
    }
}