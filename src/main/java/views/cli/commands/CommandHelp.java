package views.cli.commands;

import views.cli.io.Input;
import views.cli.io.Output;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Optional;

public class CommandHelp extends Command {
    public static final String commandString = "help";

    /**
     * @param args the arguments that are given with the command. [0] equals {@link #commandString}.
     */
    public CommandHelp(String[] args, Input input, Output output) {
        super(args, input, output);
    }

    public CommandHelp() {
        super();
    }

    @Override
    public String shortDescription() {
        return "Displays command information.";
    }

    @Override
    public String description() {
        return """
                % Displays information of all commands.
                % Use help <command> to display a more descriptive explanation.
                """;
    }


    @Override
    public void execute() {
        assert output != null;
        if (args.length == 1) {
            allShortDescription(Commands.getAllCommands());
        }
        else {
            Optional<Command> parsedCommand = parse(args);
            if (parsedCommand.isEmpty()) {
                output.print("! Command Not Found");
                return;
            }
            specificShortDescription(parsedCommand.get());
        }
    }

    public void specificShortDescription(Command command) {

    }

    public void allShortDescription(Command[] commands) {
        assert output != null;
        for (Command command : commands) {
            output.print("%\t" + command.getCommandString() + " - " + command.shortDescription() + "\n");
        }
    }

    protected Optional<Command> parse(String[] args) {
        String[] shortenedArgs = Arrays.copyOfRange(args, 1, args.length);
        return Commands.parse(args, null, null);
    }

    @Override
    public String getCommandString() {
        return CommandHelp.commandString;
    }
}

