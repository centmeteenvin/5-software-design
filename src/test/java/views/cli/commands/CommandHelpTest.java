package views.cli.commands;

import org.junit.jupiter.api.Test;
import views.cli.io.Input;
import views.cli.io.Output;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CommandHelpTest {

    @Test
    void shortDescription() {
    }

    @Test
    void description() {
    }

    @Test
    void execute() {
        String[] args = new String[]{CommandHelp.commandString};
        Output output = mock(Output.class);
        CommandHelp command = spy(new CommandHelpEmpty(args, null, output));
        doNothing().when(output).print(any());

        command.execute();

        verify(command, times(1)).allShortDescription(any());
        verify(command, never()).specificShortDescription(any());

        args = new String[]{CommandHelp.commandString, "fake"};
        command = spy(new CommandHelpEmpty(args, null, output));

        command.execute();

        verify(command, never()).allShortDescription(any());
        verify(command, never()).specificShortDescription(any());
        verify(output, times(1)).print("! Command Not Found");

        args = new String[]{CommandHelp.commandString, "real"};
        command = spy(new CommandHelpEmpty(args, null, output));

        command.execute();

        verify(command, never()).allShortDescription(any());
        verify(command, times(1)).specificShortDescription(command);
    }

    private static class CommandHelpEmpty extends CommandHelp {

        /**
         * @param args   the arguments that are given with the command. [0] equals {@link #commandString}.
         * @param input
         * @param output
         */
        public CommandHelpEmpty(String[] args, Input input, Output output) {
            super(args, input, output);
        }

        @Override
        public String shortDescription() {
            return "";
        }

        @Override
        public String description() {
            return "";
        }

        @Override
        protected Optional<Command> parse(String[] args) {
            if (Objects.equals(args[1], "fake")) return Optional.empty();
            return Optional.of(this);
        }
    }
}