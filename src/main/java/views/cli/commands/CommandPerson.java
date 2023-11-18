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
        assert view != null;
        if (args.length == 1) {
            view.output.print(incorrectNumberOfArguments(2, 1));
            return;
        }
        switch (args[1]) {
            case "create" -> executeCreate();
            default -> view.output.print("! Incorrect arguments, consult [help person]\n");
        }
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

    public void executeCreate() {
        //TODO
    }
}
