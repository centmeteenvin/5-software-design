package views.cli.commands;

import controllers.PersonController;
import database.Database;
import models.Person;
import org.junit.jupiter.api.Test;
import views.cli.ViewCommandLine;
import views.cli.io.Output;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CommandPersonTest extends CommandTest {

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

        args = new String[]{CommandPersons.commandString, "get"};
        command = spy(new CommandPerson(args, view));

        command.execute();

        verify(command, times(1)).executeGet();
    }

    @Test
    void executeCreate() {
        String[] args = new String[]{CommandPerson.commandString, "create"};
        Output output = mock(Output.class);
        //noinspection unchecked
        PersonController personController =  mock(PersonController.class);
        Person person = new Person(1L, "foo");
        ViewCommandLine view = new ViewCommandLine(
                null, null, null,
                personController, null, null,
                null, output);
        CommandPerson command = (new CommandPerson(args, view));
        doNothing().when(output).print(anyString());

        command.executeCreate();

        verify(output, times(1)).print(Command.incorrectNumberOfArguments(3, 2));

        args = new String[]{CommandPerson.commandString, "create", "foo"};
        command = (new CommandPerson(args, view));
        doReturn(Optional.empty()).when(personController).create(anyString());

        command.executeCreate();

        verify(personController, times(1)).create("foo");
        verify(output, times(1)).print("! Failed to create person with name \"foo\"\n");

        doReturn(Optional.of(person)).when(personController).create(anyString());

        command.executeCreate();

        verify(personController, times(2)).create("foo");
        verify(output, times(1)).print("% Successfully created person with name \"foo\" with id: 1\n");
    }

    @Test
    void personRepresentation() {
        Person person = new Person(1L, "foo");
        CommandPerson command = new CommandPerson();
        person.getDebts().put(1L, 100D);
        person.getDebts().put(2L, -1000D);
        person.getTicketsId().add(1L);
        person.getTicketsId().add(2L);
        String expected = """
                % id: 1
                % name: foo
                % ticketIds: [ 1, 2 ]
                % debts:
                %   I owe 1 100.00 EUR
                %   2 owes me 1000.00 EUR
                % total debt: -900.00 EUR
                """;
        assertEquals(expected, command.personRepresentation(person));
    }

    @Override
    public Command getCommand() {
        return new CommandPerson();
    }
}