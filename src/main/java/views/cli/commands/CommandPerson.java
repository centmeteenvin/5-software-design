package views.cli.commands;

import controllers.PersonController;
import models.Person;
import views.cli.ViewCommandLine;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Map;
import java.util.Optional;

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
            case "get" -> executeGet();
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
                %
                %  - get {id}
                %       Fetches a person and shows the following data:
                %        - All the present tickets.
                %        - All the current debts.
                """;
    }

    @Override
    public String getCommandString() {
        return CommandPerson.commandString;
    }

    public void executeCreate() {
        assert view != null;
        if (args.length != 3) {
            view.output.print(incorrectNumberOfArguments(3, args.length));
            return;
        }
        PersonController personController = view.getPersonController();
        Optional<Person> person = personController.create(args[2]);
        if (person.isEmpty()) {
            view.output.print("! Failed to create person with name \"%s\"\n".formatted(args[2]));
            return;
        }
        view.output.print("%% Successfully created person with name \"%s\" with id: %s\n".formatted(person.get().getName(), person.get().getId()));
    }

    public void executeGet() {

    }

    public String personRepresentation(Person person) {
        String id = person.getId().toString();
        String name = person.getName();
        StringBuilder ticketIds = new StringBuilder("[ ");
        for (Long ticketId : person.getTicketsId()) {
            ticketIds.append(ticketId).append(", ");
        }
        ticketIds.delete(ticketIds.length() - 2, ticketIds.length() - 1);
        ticketIds.append("]");

        double totalDebt = 0;
        StringBuilder debts = new StringBuilder();
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        DecimalFormat format = new DecimalFormat("0.00", symbols);
        for (Map.Entry<Long, Double> debt : person.getDebts().entrySet()) {
            debts.append("%   ");
            if (debt.getValue() < 0) {
                debts.append(debt.getKey()).append(" owes me ").append(format.format(-debt.getValue())).append(" EUR\n");
            }
            else {
                debts.append("I owe ").append(debt.getKey()).append(" ").append(format.format(debt.getValue())).append(" EUR\n");
            }
            totalDebt += debt.getValue();
        }
        debts.delete(debts.length() - 1, debts.length());
        return """
                %% id: %s
                %% name: %s
                %% ticketIds: %s
                %% debts:
                %s
                %% total debt: %s EUR
                """.formatted(id, name, ticketIds, debts, format.format(totalDebt));
    }

}
