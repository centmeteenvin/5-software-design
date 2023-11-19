package views.cli.commands;

import controllers.TicketController;
import models.Ticket;
import org.junit.jupiter.api.Test;
import views.cli.ViewCommandLine;
import views.cli.io.Output;

import java.util.List;
import java.util.Optional;

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
        String[] args = new String[]{CommandTicket.commandString};
        CommandTicket commandTicket = new CommandTicket(args, view);
        doNothing().when(output).print(anyString());

        commandTicket.execute();

        verify(output, times(1)).print(Command.incorrectNumberOfArguments(2, 1));

        args = new String[]{CommandTicket.commandString, "foo"};
        commandTicket = new CommandTicket(args, view);

        commandTicket.execute();
        verify(output, times(1)).print("! Command not found, consider consulting {help ticket}\n");

        args = new String[]{CommandTicket.commandString, "create"};
        commandTicket = spy(new CommandTicket(args, view));

        commandTicket.execute();

        verify(commandTicket, times(1)).executeCreate();
    }

    @Test
    void executeCreate() {
        TicketController ticketController = mock(TicketController.class);
        Output output = mock(Output.class);
        ViewCommandLine view = new ViewCommandLine(null, null, null,
                null, ticketController, null,
                null, output);
        String[] args = new String[]{CommandTicket.commandString, "create"};
        CommandTicket command = new CommandTicket(args, view);
        doNothing().when(output).print(anyString());

        command.executeCreate();

        verify(output, times(1)).print(Command.incorrectNumberOfArguments(3, 2));

        args = new String[]{CommandTicket.commandString, "create", "-sf"};
        command = new CommandTicket(args, view);

        command.executeCreate();

        verify(output, times(1)).print("! -sf is not a valid number\n");

        args = new String[]{CommandTicket.commandString, "create", "-100"};
        command = new CommandTicket(args, view);

        command.executeCreate();

        verify(output, times(1)).print("! -100.00 EUR is not a valid cost\n");
        verifyNoInteractions(ticketController);

        args = new String[]{CommandTicket.commandString, "create", "100.5"};
        command = new CommandTicket(args, view);
        Ticket ticket = new Ticket(1L, 100.5, null);
        doReturn(Optional.empty()).when(ticketController).create(null, 100.5, List.of());

        command.executeCreate();

        verify(output, times(1)).print("! Failed to create ticket\n");
        verify(ticketController, times(1)).create(null, 100.5, List.of());

        doReturn(Optional.of(ticket)).when(ticketController).create(null, 100.5, List.of());

        command.executeCreate();

        verify(output, times(1)).print("% Successfully created ticket with cost 100.50 EUR and id 1\n");
        verify(ticketController, times(2)).create(null, 100.5, List.of());

    }

    @Override
    public Command getCommand() {
        return new CommandTicket();
    }
}