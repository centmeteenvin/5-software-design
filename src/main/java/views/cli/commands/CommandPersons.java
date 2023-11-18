package views.cli.commands;

import views.cli.ViewCommandLine;

public class CommandPersons extends Command {
    public static final String commandString = "persons";

    /**
     * @param args the arguments that are given with the command. [0] equals {@link #commandString}.
     * @param view
     */
    public CommandPersons(String[] args, ViewCommandLine view) {
        super(args, view);
    }

    public CommandPersons() {super();}

    @Override
    public void execute() {
        //TODO
    }

    @Override
    public String shortDescription() {
        return "Show all people and their ids";
    }

    @Override
    public String description() {
        return """
                % Shows all people that currently exist and their id.
                % The id can be used to fetch a persons information via "person {id}".
                """;
    }

    @Override
    public String getCommandString() {
        return CommandPersons.commandString;
    }
}
