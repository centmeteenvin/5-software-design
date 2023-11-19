package views.cli.commands;

import database.Database;
import models.TicketCategory;
import org.junit.jupiter.api.Test;
import views.cli.ViewCommandLine;
import views.cli.io.Output;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CommandCategoriesTest extends CommandTest {

    @Test
    void execute() {
        //noinspection unchecked
        Database<TicketCategory> db = (Database<TicketCategory>) mock(Database.class);
        Output output = mock(Output.class);
        ViewCommandLine view = new ViewCommandLine(null, null, db,
                null, null, null,
                null, output);
        String[] args = new String[]{CommandCategories.commandString, "foo"};
        CommandCategories command = new CommandCategories(args, view);
        doNothing().when(output).print(anyString());

        command.execute();

        verify(output, times(1)).print(Command.incorrectNumberOfArguments(1, 2));

        args = new String[]{CommandCategories.commandString};
        command = new CommandCategories(args, view);
        List<TicketCategory> categories = List.of(
          new TicketCategory(1L, "foo"),
          new TicketCategory(2L, "bar")
        );
        doReturn(categories).when(db).getAll();

        command.execute();

        verify(output, times(1)).print("% 1 -> foo\n");
        verify(output, times(1)).print("% 2 -> bar\n");
    }

    @Override
    public Command getCommand() {
        return new CommandCategories();
    }
}