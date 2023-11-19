package views.cli.commands;

import org.junit.jupiter.api.Test;
import views.cli.ViewCommandLine;
import views.cli.io.Output;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CommandCategoryTest extends CommandTest {

    @Test
    void execute() {
        Output output = mock(Output.class);
        ViewCommandLine view = new ViewCommandLine(null, null, null,
                null, null, null,
                null, output);
        String[] args = new String[]{CommandCategory.commandString};
        CommandCategory command = new CommandCategory(args, view);
        doNothing().when(output).print(anyString());

        command.execute();

        verify(output, times(1)).print(Command.incorrectNumberOfArguments(2, 1));

        args = new String[]{CommandCategory.commandString, "foo"};
        command = new CommandCategory(args, view);

        command.execute();

        verify(output, times(1)).print("! Command not found, try consulting {help category}\n");

        args = new String[]{CommandCategory.commandString, "create"};
        command = spy(new CommandCategory(args, view));

        command.execute();

        verify(command, times(1)).executeCreate();
    }

    @Override
    public Command getCommand() {
        return new CommandCategory();
    }
}