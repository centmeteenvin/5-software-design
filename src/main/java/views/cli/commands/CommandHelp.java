package views.cli.commands;

import views.cli.ViewCommandLine;
import views.cli.io.Input;
import views.cli.io.Output;

import java.util.Arrays;
import java.util.Optional;

public class CommandHelp extends Command {
    public static final String commandString = "help";

    /**
     * @param args the arguments that are given with the command. [0] equals {@link #commandString}.
     */
    public CommandHelp(String[] args, ViewCommandLine view) {
        super(args, view);
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
        if (args.length == 1) {
            allShortDescription(Commands.getAllCommands());
        }
        else {
            Optional<Command> parsedCommand = parse(args);
            if (parsedCommand.isEmpty()) {
                assert view != null;
                view.output.print("! Command Not Found\n");
                return;
            }
            specificDescription(parsedCommand.get());
        }
    }

    public void specificDescription(Command command) {
        assert view != null;
        view.output.print(command.description());
    }

    public void allShortDescription(Command[] commands) {
        assert view != null;
        view.output.print(description());
        for (Command command : commands) {
            view.output.print("%\t" + command.getCommandString() + " - " + command.shortDescription() + "\n");
        }
    }

    protected Optional<Command> parse(String[] args) {
        String[] shortenedArgs = Arrays.copyOfRange(args, 1, args.length);
        return Commands.parse(args, view);
    }

    @Override
    public String getCommandString() {
        return CommandHelp.commandString;
    }
}

