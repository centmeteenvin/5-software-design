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
        return "Main entrypoint for all ticket category related commands";
    }

    @Override
    public String description() {
        return """
                % Main entrypoint for ticket category related commands
                %
                % create {name}:
                %   creates a new category with the given name.
                """;
    }

    @Override
    public String getCommandString() {
        return null;
    }

    @Override
    public void execute() {
        assert view != null;
        if (args.length == 1) {
            view.output.print(incorrectNumberOfArguments(2, args.length));
            return;
        }
        switch (args[1]) {
            case "create" -> executeCreate();
            default -> view.output.print("! Command not found, try consulting {help category}\n");
        }
    }

    public void executeCreate() {

    }
}
