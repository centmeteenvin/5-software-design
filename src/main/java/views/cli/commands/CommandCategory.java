package views.cli.commands;

import models.TicketCategory;
import views.cli.ViewCommandLine;

import java.util.Optional;

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
                %
                % get {id}:
                %   Fetches a person from the database and shows the following data:
                %    - id
                %    - name
                %    - associated tickets.
                """;
    }

    @Override
    public String getCommandString() {
        return CommandCategory.commandString;
    }

    @Override
    public void execute() {
        assert view != null;
        if (args.length == 1) {
            view.output.print(incorrectNumberOfArguments(2, args.length));
            return;
        }
        switch (args[1]) {
            case "create"   -> executeCreate();
            case "get"      -> executeGet();
            default -> view.output.print("! Command not found, try consulting {help category}\n");
        }
    }

    public void executeCreate() {
        assert view != null;
        if (args.length != 3) {
            view.output.print(incorrectNumberOfArguments(3, args.length));
            return;
        }
        Optional<TicketCategory> category = view.getTicketCategoryController().create(args[2]);
        if (category.isEmpty()) {
            view.output.print("! Failed to create category\n");
            return;
        }
        view.output.print("%% Successfully created category %s with id %s\n".formatted(category.get().getName(), category.get().getId()));
    }

    public void executeGet() {
        assert view != null;
        if (args.length != 3) {
            view.output.print(incorrectNumberOfArguments(3, args.length));
            return;
        }
        Optional<TicketCategory> category = view.getTicketCategoryDatabase().getById(Long.valueOf(args[2]));
        if (category.isEmpty()) {
            view.output.print("! Category not found\n");
            return;
        }
        view.output.print(categoryPresentation(category.get()));
    }

    public String categoryPresentation(TicketCategory category) {
        String id = category.getId().toString();
        String name = category.getName();
        StringBuilder ticketIds = new StringBuilder("[ ");
        if (!category.getTicketIds().isEmpty()) {
            for (Long ticketId : category.getTicketIds()) {
                ticketIds.append(ticketId).append(", ");
            }
            ticketIds.delete(ticketIds.length() - 2, ticketIds.length() - 1);
        }
        ticketIds.append("]");
        return """
                %% id: %s
                %% name: %s
                %% tickets: %s
                """.formatted(id, name, ticketIds);
    }
}
