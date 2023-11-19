package views.cli.commands;

import views.cli.ViewCommandLine;

public class CommandCategory extends Command {
    public static final String commandString = "category";

    /**
     * @param args the arguments that are given with the command. [0] equals {@link #commandString}.
     * @param view
     */
    public CommandCategory(String[] args, ViewCommandLine view) {
        super(args, view);
    }

    public CommandCategory() {
    }

    @Override
    public String shortDescription() {
        return null;
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public String getCommandString() {
        return null;
    }

    @Override
    public void execute() {

    }
}
