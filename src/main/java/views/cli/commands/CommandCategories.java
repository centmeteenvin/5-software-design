package views.cli.commands;

import models.TicketCategory;
import views.cli.ViewCommandLine;

import java.util.List;

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
        return CommandCategories.commandString;
    }

    @Override
    public void execute() {
        assert view != null;
        if (args.length != 1) {
            view.output.print(incorrectNumberOfArguments(1, args.length));
            return;
        }
        List<TicketCategory> categories = view.getTicketCategoryDatabase().getAll();
        for (TicketCategory category : categories) {
            view.output.print("%% %s -> %s\n".formatted(category.getId(), category.getName()));
        }
    }
}
