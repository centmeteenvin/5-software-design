package views.cli.commands;

import controllers.TicketController;
import org.junit.jupiter.api.Test;
import views.cli.ViewCommandLine;
import views.cli.io.Output;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CommandCalculateTest extends CommandTest {

    @Test
    void execute() {
        Output output = mock(Output.class);
        TicketController ticketController = mock(TicketController.class);
        ViewCommandLine view = new ViewCommandLine(null, null, null,
                null, ticketController, null,
                null, output);
        String[] args = new String[]{CommandCalculate.commandString, "foo"};
        CommandCalculate command = new CommandCalculate(args, view);
        doNothing().when(output).print(anyString());

        command.execute();

        verify(output, times(1)).print(Command.incorrectNumberOfArguments(1, 2));

        args = new String[]{CommandCalculate.commandString};
        command = new CommandCalculate(args, view);

        doNothing().when(ticketController).calculateAll();

        command.execute();

        verify(output, times(1)).print("% Successfully calculated debts");
        verify(ticketController, times(1)).calculateAll();
    }

    @Override
    public Command getCommand() {
        return new CommandCalculate();
    }
}