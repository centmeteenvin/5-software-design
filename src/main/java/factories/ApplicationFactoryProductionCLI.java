package factories;

import controllers.PersonController;
import controllers.TicketCategoryController;
import controllers.TicketController;
import database.Database;
import models.Person;
import models.Ticket;
import models.TicketCategory;
import views.View;
import views.cli.ViewCommandLine;

/**
 * This implements all the controller en database implementation
 */
public class ApplicationFactoryProductionCLI extends ApplicationFactoryProduction{
    @Override
    public View createView(Database<Person> personDatabase, Database<Ticket> ticketDatabase, Database<TicketCategory> ticketCategoryDatabase, PersonController personController, TicketController ticketController, TicketCategoryController ticketCategoryController) {
        return new ViewCommandLine(personDatabase, ticketDatabase, ticketCategoryDatabase, personController, ticketController, ticketCategoryController);
    }
}
