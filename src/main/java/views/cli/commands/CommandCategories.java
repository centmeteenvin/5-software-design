package views.cli.commands;

import views.cli.ViewCommandLine;

public class CommandCategories extends Command {
    public static final String commandString = "categories";

    /**
     * @param args the arguments that are given with the command. [0] equals {@link #commandString}.
     * @param view
     */
    public CommandCategories(String[] args, ViewCommandLine view) {
        super(args, view);
    }

    public CommandCategories() {
    }

    @Override
    public String shortDescription() {
        return "Displays all the stored categories";
    }

    @Override
    public String description() {
        return """
                % Displays all the stored categories.
                % The following format is used:
                %   {id} -> {name}
                """;
    }

    @Override
    public String getCommandString() {
        return null;
    }

    @Override
    public void execute() {

    }
}
