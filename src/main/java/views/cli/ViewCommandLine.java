package views.cli;

import controllers.PersonController;
import controllers.TicketCategoryController;
import controllers.TicketController;
import database.Database;
import models.Person;
import models.Ticket;
import models.TicketCategory;
import views.View;
import views.cli.io.Input;
import views.cli.io.InputStandard;
import views.cli.io.Output;
import views.cli.io.OutputStandard;

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
                Welcome To The Script Kiddos' Money Tracker AKA: SKMT.
                
                To get started you can type "help".
                This will show you all available commands.
                """);
    }
}
