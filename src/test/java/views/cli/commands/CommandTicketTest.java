package views.cli.commands;

import controllers.TicketController;
import database.Database;
import exceptions.notFoundExceptions.CategoryNotFoundException;
import exceptions.notFoundExceptions.PersonNotFoundException;
import exceptions.notFoundExceptions.TicketNotFoundException;
import models.Person;
import models.Ticket;
import models.TicketCategory;
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

        args = new String[]{CommandTicket.commandString, "get"};
        commandTicket = spy(new CommandTicket(args, view));

        commandTicket.execute();

        verify(commandTicket, times(1)).executeGet();

        args = new String[]{CommandTicket.commandString, "add"};
        commandTicket = spy(new CommandTicket(args, view));

        commandTicket.execute();

        verify(commandTicket, times(1)).executeAdd();

        args = new String[]{CommandTicket.commandString, "set"};
        commandTicket = spy(new CommandTicket(args, view));

        commandTicket.execute();

        verify(commandTicket, times(1)).executeSetWeight();
    }

    @Test
    void executeCreate() throws PersonNotFoundException, CategoryNotFoundException {
        TicketController ticketController = mock(TicketController.class);
        Output output = mock(Output.class);
        //noinspection unchecked
        Database<TicketCategory> categoryDb = (Database<TicketCategory>) mock(Database.class);
        ViewCommandLine view = new ViewCommandLine(null, null, categoryDb,
                null, ticketController, null,
                null, output);
        String[] args = new String[]{CommandTicket.commandString, "create"};
        CommandTicket command = new CommandTicket(args, view);
        doNothing().when(output).print(anyString());

        command.executeCreate();

        verify(output, times(1)).print(Command.incorrectNumberOfArguments(4, 2));

        args = new String[]{CommandTicket.commandString, "create", "-sf"};
        command = new CommandTicket(args, view);

        command.executeCreate();

        verify(output, times(1)).print(Command.incorrectNumberOfArguments(4, 3));

        args = new String[]{CommandTicket.commandString, "create", "-sf", "1"};
        command = new CommandTicket(args, view);

        command.executeCreate();
        verify(output, times(1)).print("! -sf is not a valid number\n");

        args = new String[]{CommandTicket.commandString, "create", "-100", "1"};
        command = new CommandTicket(args, view);

        command.executeCreate();

        verify(output, times(1)).print("! -100.00 EUR is not a valid cost\n");
        verifyNoInteractions(ticketController);

        args = new String[]{CommandTicket.commandString, "create", "100.5", "1"};
        command = new CommandTicket(args, view);
        doThrow(CategoryNotFoundException.class).when(ticketController).create(1L, 100.5, List.of());

        command.executeCreate();

        verify(output, times(1)).print("! Category does not exist\n");

        doThrow(PersonNotFoundException.class).when(ticketController).create(1L, 100.5, List.of());

        command.executeCreate();

        verify(output, times(1)).print("! Received PersonNotFoundException, this should not occur\n");

        args = new String[]{CommandTicket.commandString, "create", "100.5", "1"};
        command = new CommandTicket(args, view);
        Ticket ticket = new Ticket(1L, 100.5, 1L);
        TicketCategory category = new TicketCategory(1L, "foo");
        doReturn(Optional.empty()).when(ticketController).create(1L, 100.5, List.of());

        command.executeCreate();

        verify(output, times(1)).print("! Failed to create ticket\n");
        verify(ticketController, times(3)).create(1L, 100.5, List.of());

        doReturn(Optional.of(ticket)).when(ticketController).create(1L, 100.5, List.of());

        command.executeCreate();

        verify(output, times(1)).print("% Successfully created ticket with cost 100.50 EUR and id 1\n");
        verify(ticketController, times(4)).create(1L, 100.5, List.of());

    }

    @Test
    void executeGet() {
        //noinspection unchecked
        Database<Ticket> db = (Database<Ticket>) mock(Database.class);
        Output output = mock(Output.class);
        ViewCommandLine view = new ViewCommandLine(null, db, null,
                null, null, null,
                null, output);
        String[] args = new String[]{CommandTicket.commandString, "get"};
        CommandTicket command = new CommandTicket(args, view);
        doNothing().when(output).print(anyString());

        command.executeGet();

        verify(output, times(1)).print(Command.incorrectNumberOfArguments(3, 2));

        args = new String[]{CommandTicket.commandString, "get", "2"};
        command = spy(new CommandTicket(args, view));
        doReturn(Optional.empty()).when(db).getById(2L);

        command.executeGet();

        verify(output, times(1)).print("! Ticket does not exist\n");
        verify(db, times(1)).getById(2L);

        Ticket ticket = new Ticket(2L, 100, 3L);
        doReturn(Optional.of(ticket)).when(db).getById(2L);

        command.executeGet();

        verify(command, times(1)).ticketRepresentation(ticket);
    }

    @Test
    void executeAdd() throws TicketNotFoundException, PersonNotFoundException {
        TicketController ticketController = mock(TicketController.class);
        Output output = mock(Output.class);
        ViewCommandLine view = new ViewCommandLine(null, null, null,
                null, ticketController, null,
                null, output);
        String[] args = new String[]{CommandTicket.commandString, "add"};
        CommandTicket command = new CommandTicket(args, view);
        doNothing().when(output).print(anyString());

        command.executeAdd();

        verify(output, times(1)).print(Command.incorrectNumberOfArguments(4, 2));

        args = new String[]{CommandTicket.commandString, "add", "1"};
        command = new CommandTicket(args, view);

        command.executeAdd();

        verify(output, times(1)).print(Command.incorrectNumberOfArguments(4, 3));

        args = new String[]{CommandTicket.commandString, "add", "1", "2"};
        command = new CommandTicket(args, view);
        doThrow(TicketNotFoundException.class).when(ticketController).addPerson(any(), any());

        command.executeAdd();

        verify(output, times(1)).print("! Ticket does not exist\n");

        doThrow(PersonNotFoundException.class).when(ticketController).addPerson(any(), any());

        command.executeAdd();

        verify(output, times(1)).print("! Person does not exist\n");

        doNothing().when(ticketController).addPerson(1L, 2L);

        command.executeAdd();

        verify(ticketController, times(3)).addPerson(1L, 2L);
        verify(output, times(1)).print("% Successfully added person 2 to ticket 1\n");
    }

    @Test
    void executeSetWeight() throws TicketNotFoundException {
        TicketController ticketController = mock(TicketController.class);
        Output output = mock(Output.class);
        ViewCommandLine view = new ViewCommandLine(null, null, null,
                null, ticketController, null,
                null, output);
        String[] args = new String[]{CommandTicket.commandString, "set"};
        CommandTicket command = new CommandTicket(args, view);
        doNothing().when(output).print(anyString());

        command.execute();

        verify(output, times(1)).print(Command.incorrectNumberOfArguments(5, 2));

        args = new String[]{CommandTicket.commandString, "set", "1"};
        command = new CommandTicket(args, view);

        command.execute();

        verify(output, times(1)).print(Command.incorrectNumberOfArguments(5, 3));

        args = new String[]{CommandTicket.commandString, "set", "1", "2"};
        command = new CommandTicket(args, view);

        command.execute();

        verify(output, times(1)).print(Command.incorrectNumberOfArguments(5, 4));

        args = new String[]{CommandTicket.commandString, "set", "1", "2", "100"};
        command = new CommandTicket(args, view);
        int changeWeightCounter = 0;
        doThrow(TicketNotFoundException.class).when(ticketController).changeWeight(1L, 2L, 100);

        command.execute();

        verify(output, times(1)).print("! Ticket with id 1 not found.");
        verify(ticketController, times(++changeWeightCounter)).changeWeight(1L, 2L, 100);

        doNothing().when(ticketController).changeWeight(1L, 2L, 100);

        command.execute();

        verify(output, times(1)).print("% Successfully updated weight");
        verify(ticketController, times(++changeWeightCounter)).changeWeight(1L, 2L, 100);

    }

    @Test
    void ticketPresentation() {
        Ticket ticket = new Ticket(1L, 100.00, 2L);
        ticket.getDistribution().put(3L, 120.00);
        ticket.getDistribution().put(4L, 0.5);
        CommandTicket command = new CommandTicket();
        String expected = """
                % id: 1
                % cost: 100.00 EUR
                % category: 2
                % distribution:
                %   3 -> 120.00 EUR
                %   4 -> 0.50 EUR
                """;
        assertEquals(expected, command.ticketRepresentation(ticket));
    }

    @Override
    public Command getCommand() {
        return new CommandTicket();
    }
}