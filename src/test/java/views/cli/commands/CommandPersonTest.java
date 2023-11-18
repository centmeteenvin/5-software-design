package views.cli.commands;

import database.Database;
import models.Person;
import org.junit.jupiter.api.Test;
import views.cli.ViewCommandLine;
import views.cli.io.Output;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CommandPersonTest {

    @Test
    void execute() {
        String[] args = new String[]{CommandPerson.commandString};
        Output output = mock(Output.class);
        ViewCommandLine view = new ViewCommandLine(
                null, null, null,
                null, null, null,
                null, output);
        CommandPerson command = (new CommandPerson(args, view));
        doNothing().when(output).print(anyString());

        command.execute();

        verify(output, times(1)).print(Command.incorrectNumberOfArguments(2, 1));

        args = new String[]{CommandPersons.commandString, "foo"};
        command = (new CommandPerson(args, view));

        command.execute();

        verify(output, times(1)).print("! Incorrect arguments, consult [help person]\n");

        args = new String[]{CommandPersons.commandString, "create"};
        command = spy(new CommandPerson(args, view));

        command.execute();

        verify(command, times(1)).executeCreate();
    }

    @Test
    void executeCreate() {
    }
}