package views.cli.commands;

import views.cli.ViewCommandLine;

public class CommandTicket extends Command{
    public static final String commandString = "ticket";

    /**
     * @param args the arguments that are given with the command. [0] equals {@link #commandString}.
     * @param view
     */
    public CommandTicket(String[] args, ViewCommandLine view) {
        super(args, view);
    }

    public CommandTicket() {
    }

    @Override
    public void execute() {
        assert view != null;
        if (args.length == 1) {
            view.output.print(incorrectNumberOfArguments(2, 1));
            return;
        }
        switch (args[1]) {
            default -> view.output.print("! Command not found, consider consulting {help ticket}\n");
        }
    }

    @Override
    public String shortDescription() {
        return "Main entrypoint for most ticket related commands";
    }

    @Override
    public String description() {
        return """
                % Main entrypoint for most ticket related commands
                """;
    }

    @Override
    public String getCommandString() {
        return commandString;
    }
}
