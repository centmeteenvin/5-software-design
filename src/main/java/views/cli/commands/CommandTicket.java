package views.cli.commands;

import models.Ticket;
import models.TicketCategory;
import views.cli.ViewCommandLine;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Optional;

public class CommandTicket extends Command{
    public static final String commandString = "ticket";

    /**
     * @param args the arguments that are given with the command. [0] equals {@link #commandString}.
     * @param view
     */
    public CommandTicket(String[] args, ViewCommandLine view) {
        super(args, view);
    }

    public CommandTicket() {
    }
    @Override
    public String shortDescription() {
        return "Main entrypoint for most ticket related commands";
    }

    @Override
    public String description() {
        return """
                % Main entrypoint for most ticket related commands
                %
                % create {cost} {category id}:
                %   Creates a ticket with the given cost in the given category.
                %
                % get {id}:
                %   Fetches the ticket with the given id and returns the following data:
                %    - id
                %    - cost
                %    - category
                %    - distribution
                """;
    }

    @Override
    public void execute() {
        assert view != null;
        if (args.length == 1) {
            view.output.print(incorrectNumberOfArguments(2, 1));
            return;
        }
        switch (args[1]) {
            case "create"   -> executeCreate();
            case "get"      -> executeGet();
            default -> view.output.print("! Command not found, consider consulting {help ticket}\n");
        }
    }

    public void executeCreate() {
        assert view != null;
        if (args.length != 4) {
            view.output.print(incorrectNumberOfArguments(4, args.length));
            return;
        }
        double cost;
        try {
            cost = Double.parseDouble(args[2]);
        } catch (NumberFormatException e) {
            view.output.print("! %s is not a valid number\n".formatted(args[2]));
            return;
        }
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        DecimalFormat format = new DecimalFormat("0.00", symbols);
        if (cost < 0) {
            view.output.print("! %s EUR is not a valid cost\n".formatted(format.format(cost)));
            return;
        }
        Optional<TicketCategory> category = view.getTicketCategoryDatabase().getById(Long.valueOf(args[3]));
        if (category.isEmpty()) {
            view.output.print("! Category does not exist\n");
            return;
        }
        Optional<Ticket> ticket = view.getTicketController().create(category.get().getId(), cost, List.of());
        if (ticket.isEmpty()) {
            view.output.print("! Failed to create ticket\n");
            return;
        }
        view.output.print("%% Successfully created ticket with cost %s EUR and id %s\n".formatted(format.format(cost), ticket.get().getId()));
    }

    public void executeGet() {

    }

    @Override
    public String getCommandString() {
        return commandString;
    }
}
