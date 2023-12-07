package views.cli.commands;

import views.cli.ViewCommandLine;

public class CommandCalculate extends Command {
    public static final String commandString = "calculate";

    /**
     * @param args the arguments that are given with the command. [0] equals {@link #commandString}.
     * @param view
     */
    public CommandCalculate(String[] args, ViewCommandLine view) {
        super(args, view);
    }

    public CommandCalculate() {
    }

    @Override
    public void execute() {
        assert view != null;
        if (args.length != 1) {
            view.output.print(incorrectNumberOfArguments(1, args.length));
            return;
        }
        view.getTicketController().calculateAll();
        view.output.print("% Successfully calculated debts");
    }

    @Override
    public String shortDescription() {
        return "Calculate all the debts based on the currently stored tickets";
    }

    @Override
    public String description() {
        return """
                % Calculate all the debts of each person based on all the tickets present in the database.
                """;
    }

    @Override
    public String getCommandString() {
        return commandString;
    }
}
