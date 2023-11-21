package factories;

import controllers.PersonController;
import controllers.TicketCategoryController;
import controllers.TicketController;
import database.Database;
import models.Person;
import models.Ticket;
import models.TicketCategory;
import views.View;
import views.gui.ViewJ2D;

public class ApplicationFactoryProductionGUI extends  ApplicationFactoryProduction{

    @Override
    public View createView(Database<Person> personDatabase, Database<Ticket> ticketDatabase, Database<TicketCategory> ticketCategoryDatabase, PersonController personController, TicketController ticketController, TicketCategoryController ticketCategoryController) {
        return new ViewJ2D(personDatabase, ticketDatabase, ticketCategoryDatabase, personController,
                ticketController, ticketCategoryController);
    }
}
