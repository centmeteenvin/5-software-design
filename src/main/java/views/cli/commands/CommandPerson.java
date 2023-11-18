package views.cli.commands;

import views.cli.ViewCommandLine;

public class CommandPerson extends Command{
    public static final String commandString = "person";

    /**
     * @param args the arguments that are given with the command. [0] equals {@link #commandString}.
     * @param view
     */
    public CommandPerson(String[] args, ViewCommandLine view) {
        super(args, view);
    }

    public CommandPerson() {
    }

    @Override
    public void execute() {
        //TODO
    }

    @Override
    public String shortDescription() {
        return "Entry point for most person related commands";
    }

    @Override
    public String description() {
        return """
                % Entry point for most of the person related commands
                %
                %  - create {name}
                %       Creates a person with a give name.
                """;
    }

    @Override
    public String getCommandString() {
        return CommandPerson.commandString;
    }
}
