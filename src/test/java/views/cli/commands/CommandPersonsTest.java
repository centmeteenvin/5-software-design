package views.cli.commands;

import database.Database;
import models.Person;
import org.junit.jupiter.api.Test;
import views.cli.ViewCommandLine;
import views.cli.io.Output;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommandPersonsTest extends CommandTest {

    @Test
    void execute() {
        String[] args = new String[]{CommandPersons.commandString, "extra"};
        Output output = mock(Output.class);
        //noinspection unchecked
        Database<Person> database = (Database<Person>)  mock(Database.class);
        ViewCommandLine view = new ViewCommandLine(
                database, null, null,
                null, null, null,
                null, output);
        Command command = (new CommandPersons(args, view));

        doNothing().when(output).print(anyString());
        List<Person> people = List.of(new Person(1L, "foo"), new Person(2L, "bar"));
        doReturn(people).when(database).getAll();

        command.execute();

        verify(output, times(1)).print(Command.incorrectNumberOfArguments(1, 2));

        args = new String[]{CommandPersons.commandString};
        command = new CommandPersons(args, view);

        command.execute();

        verify(output, times(1)).print("% 1 -> foo\n");
        verify(output, times(1)).print("% 2 -> bar\n");
    }

    @Override
    public Command getCommand() {
        return new CommandPersons();
    }
}