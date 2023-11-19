package views.cli.commands;

import org.junit.jupiter.api.Test;
import views.cli.ViewCommandLine;
import views.cli.io.Output;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CommandTicketTest extends CommandTest {

    @Test
    void execute() {
        Output output = mock(Output.class);
        ViewCommandLine view = new ViewCommandLine(null, null, null,
                null, null, null,
                null, output);
        String[] args = new String[]{"ticket"};
        CommandTicket commandTicket = new CommandTicket(args, view);
        doNothing().when(output).print(anyString());

        commandTicket.execute();

        verify(output, times(1)).print(Command.incorrectNumberOfArguments(2, 1));

        args = new String[]{"ticket", "foo"};
        commandTicket = new CommandTicket(args, view);

        commandTicket.execute();
        verify(output, times(1)).print("! Command not found, consider consulting {help ticket}\n");
    }

    @Override
    public Command getCommand() {
        return new CommandTicket();
    }
}