package views.cli.commands;

public class CommandPersons extends Command {
    public static final String commandString = "persons";

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
