package views.cli.commands;

import exceptions.notFoundExceptions.CategoryNotFoundException;
import exceptions.notFoundExceptions.PersonNotFoundException;
import exceptions.notFoundExceptions.TicketNotFoundException;
import models.Ticket;
import models.TicketCategory;
import views.cli.ViewCommandLine;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Map;
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
                %
                % add {ticket id} {person id}:
                %   adds the given person to the given ticket with cost 0.
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
            case "add"      -> executeAdd();
            case "set"      -> executeSetWeight();
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

        Optional<Ticket> ticket;
        try {
            ticket = view.getTicketController().create(Long.valueOf(args[3]), cost, List.of());
        } catch (CategoryNotFoundException e) {
            view.output.print("! Category does not exist\n");
            return;
        } catch (PersonNotFoundException e) {
            view.output.print("! Received PersonNotFoundException, this should not occur\n");
            return;
        }
        if (ticket.isEmpty()) {
            view.output.print("! Failed to create ticket\n");
            return;
        }
        view.output.print("%% Successfully created ticket with cost %s EUR and id %s\n".formatted(format.format(cost), ticket.get().getId()));
    }

    public void executeGet() {
        assert view != null;
        if (args.length != 3) {
            view.output.print(incorrectNumberOfArguments(3, args.length));
            return;
        }
        Optional<Ticket> ticket = view.getTicketDatabase().getById(Long.valueOf(args[2]));
        if (ticket.isEmpty()) {
            view.output.print("! Ticket does not exist\n");
            return;
        }
        view.output.print(ticketRepresentation(ticket.get()));
    }

    public void executeAdd() {
        assert view != null;
        if (args.length != 4) {
            view.output.print(incorrectNumberOfArguments(4, args.length));
            return;
        }
        try {
            view.getTicketController().addPerson(Long.valueOf(args[2]), Long.valueOf(args[3]));
        } catch (TicketNotFoundException e) {
            view.output.print("! Ticket does not exist\n");
            return;
        } catch (PersonNotFoundException e) {
            view.output.print("! Person does not exist\n");
            return;        }
        view.output.print("%% Successfully added person %s to ticket %s\n".formatted(args[3], args[2]));
    }

    public void executeSetWeight() {
        assert view != null;
        if (args.length != 5) {
            view.output.print(incorrectNumberOfArguments(5, args.length));
            return;
        }
        try {
            view.getTicketController().changeWeight(Long.parseLong(args[2]), Long.parseLong(args[3]), Double.parseDouble(args[4]));
            view.output.print("% Successfully updated weight");
        } catch (TicketNotFoundException e) {
            view.output.print("! Ticket with id %s not found.".formatted(args[2]));
        }
    }

    public String ticketRepresentation(Ticket ticket) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        DecimalFormat format = new DecimalFormat("0.00", symbols);
        String id = ticket.getId().toString();
        String cost = format.format(ticket.getCost());
        String category = ticket.getTicketCategoryId().toString();
        StringBuilder distribution = new StringBuilder();
        if (!ticket.getDistribution().isEmpty()) {
            for (Map.Entry<Long, Double> entry : ticket.getDistribution().entrySet()) {
                distribution.append("%   ").append(entry.getKey()).append(" -> ").append(format.format(entry.getValue())).append(" EUR\n");
            }
            distribution.delete(distribution.length()-1, distribution.length());
        }
        else distribution.append("%");
        return """
                %% id: %s
                %% cost: %s EUR
                %% category: %s
                %% distribution:
                %s
                """.formatted(id, cost, category, distribution);
    }

    @Override
    public String getCommandString() {
        return commandString;
    }
}
