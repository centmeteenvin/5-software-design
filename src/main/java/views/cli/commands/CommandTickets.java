package views.cli.commands;

import views.cli.ViewCommandLine;

public class CommandTickets extends Command {
    public static final String commandString = "tickets";

    /**
     * @param args the arguments that are given with the command. [0] equals {@link #commandString}.
     * @param view
     */
    public CommandTickets(String[] args, ViewCommandLine view) {
        super(args, view);
    }

    public CommandTickets() {
    }

    @Override
    public void execute() {
        //TODO
    }

    @Override
    public String shortDescription() {
        return null; //TODO
    }

    @Override
    public String description() {
        return null; //TODO
    }

    @Override
    public String getCommandString() {
        return commandString;
    }
}
