package views.cli.commands;

import org.junit.jupiter.api.Test;
import views.cli.ViewCommandLine;
import views.cli.io.Input;
import views.cli.io.Output;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CommandHelpTest extends CommandTest {

    @Test
    void allShortDescription() {
        String[] args = new String[]{CommandHelp.commandString};
        Output output = mock(Output.class);
        ViewCommandLine view = new ViewCommandLine(
                null, null, null,
                null, null, null,
                null, output);
        CommandHelp command = spy(new CommandHelpEmpty(args, view));
        doNothing().when(output).print(any());

        command.allShortDescription(new CommandHelp[]{command, command});

        verify(output, times(2)).print("%\t" + CommandHelp.commandString + " - " + command.shortDescription() + "\n");
        verify(output, times(1)).print(command.description());
    }

    @Test
    void specificDescription() {
        String[] args = new String[]{CommandHelp.commandString};
        Output output = mock(Output.class);
        ViewCommandLine view = new ViewCommandLine(
                null, null, null,
                null, null, null,
                null, output);
        CommandHelp command = spy(new CommandHelpEmpty(args, view));
        doNothing().when(output).print(any());

        command.specificDescription(command);

        verify(output, times(1)).print(command.description());
    }
    @Test
    void execute() {
        String[] args = new String[]{CommandHelp.commandString};
        Output output = mock(Output.class);
        ViewCommandLine view = new ViewCommandLine(
                null, null, null,
                null, null, null,
                null, output);
        CommandHelp command = spy(new CommandHelpEmpty(args, view));
        doNothing().when(output).print(any());

        command.execute();

        verify(command, times(1)).allShortDescription(any());
        verify(command, never()).specificDescription(any());

        args = new String[]{CommandHelp.commandString, "fake"};
        command = spy(new CommandHelpEmpty(args, view));

        command.execute();

        verify(command, never()).allShortDescription(any());
        verify(command, never()).specificDescription(any());
        verify(output, times(1)).print("! Command Not Found\n");

        args = new String[]{CommandHelp.commandString, "real"};
        command = spy(new CommandHelpEmpty(args, view));

        command.execute();

        verify(command, never()).allShortDescription(any());
        verify(command, times(1)).specificDescription(command);
    }

    @Test
    void parse() {
        CommandHelp command = new CommandHelp();
        Optional<Command> result = command.parse(new String[]{CommandHelp.commandString, CommandPerson.commandString});
        assertTrue(result.isPresent());
        assertTrue(result.get() instanceof CommandPerson);
    }

    @Override
    public Command getCommand() {
        return new CommandHelp();
    }

    private static class CommandHelpEmpty extends CommandHelp {

        /**
         * @param args   the arguments that are given with the command. [0] equals {@link #commandString}.
         */
        public CommandHelpEmpty(String[] args, ViewCommandLine view) {
            super(args, view);
        }

        @Override
        public String shortDescription() {
            return "";
        }

        @Override
        public String description() {
            return "long description";
        }

        @Override
        protected Optional<Command> parse(String[] args) {
            if (Objects.equals(args[1], "fake")) return Optional.empty();
            return Optional.of(this);
        }
    }
}