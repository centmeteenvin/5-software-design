package views.cli.commands;

import models.Ticket;
import views.cli.ViewCommandLine;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

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
        assert view != null;
        if (args.length != 1) {
            view.output.print(incorrectNumberOfArguments(1, args.length));
            return;
        }
        List<Ticket> tickets = view.getTicketDatabase().getAll();
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        DecimalFormat format = new DecimalFormat("0.00", symbols);
        for (Ticket ticket : tickets) {
            view.output.print("%% %s -> %s EUR\n".formatted(ticket.getId(), format.format(ticket.getCost())));
        }
    }

    @Override
    public String shortDescription() {
        return "Lists all stored tickets";
    }

    @Override
    public String description() {
        return """
                % Lists all stored tickets.
                % Following format is used:
                % {ticket id} -> {cost}
                """;
    }

    @Override
    public String getCommandString() {
        return commandString;
    }
}
