package views.cli;

import controllers.PersonController;
import controllers.TicketCategoryController;
import controllers.TicketController;
import database.Database;
import lombok.Getter;
import lombok.Singular;
import models.Person;
import models.Ticket;
import models.TicketCategory;
import views.View;
import views.cli.commands.Command;
import views.cli.commands.Commands;
import views.cli.io.Input;
import views.cli.io.InputStandard;
import views.cli.io.Output;
import views.cli.io.OutputStandard;

import java.util.Objects;
import java.util.Optional;

@Getter
public class ViewCommandLine extends View {
    public final Output output;
    public final Input input;

    public ViewCommandLine(
            Database<Person> personDatabase, Database<Ticket> ticketDatabase,
            Database<TicketCategory> ticketCategoryDatabase, PersonController personController,
            TicketController ticketController, TicketCategoryController ticketCategoryController,
            Input input, Output output
    ) {
        super(personDatabase, ticketDatabase, ticketCategoryDatabase, personController, ticketController, ticketCategoryController);
        this.output = output;
        this.input = input;
    }

    @Override
    public void run() {
        output.print("""
                # Welcome To The Script Kiddos' Money Tracker AKA: SKMT.
                #
                # To get started you can type "help".
                # This will show you all available commands.
                #
                # To exit the program type: "exit".
                """);

        while (true) {
            output.print(" > ");
            String inputString = input.input("");
            if (Objects.equals(inputString, "exit")) break;
            Optional<Command> command = Commands.parse(inputString.split(" "), this);
            if (command.isEmpty()) output.print("! Command Not Found\n");
            else {
                command.get().execute();
            }
        }
    }
}
