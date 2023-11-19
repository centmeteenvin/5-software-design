package views.cli.commands;

import database.Database;
import models.Ticket;
import org.junit.jupiter.api.Test;
import views.cli.ViewCommandLine;
import views.cli.io.Output;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CommandTicketsTest extends CommandTest {

    @Test
    void execute() {
        //noinspection unchecked
        Database<Ticket> db = (Database<Ticket>) mock(Database.class);
        Output output = mock(Output.class);
        ViewCommandLine view = new ViewCommandLine(null, db, null,
                null, null, null,
                null, output
                );
        String[] args = new String[]{"tickets", "extra"};
        CommandTickets command = new CommandTickets(args, view);
        doNothing().when(output).print(anyString());

        command.execute();

        verify(output, times(1)).print(Command.incorrectNumberOfArguments(1, 2));

        List<Ticket> tickets = List.of(
                new Ticket(1L, 10, null),
                new Ticket(2L, 100, null)
        );

        doReturn(tickets).when(db).getAll();
        args = new String[]{"tickets"};
        command = new CommandTickets(args, view);

        command.execute();
        verify(output, times(1)).print("% 1 -> 10.00 EUR\n");
        verify(output, times(1)).print("% 2 -> 100.00 EUR\n");
    }

    @Override
    public Command getCommand() {
        return new CommandTickets();
    }
}