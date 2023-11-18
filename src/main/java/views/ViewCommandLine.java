package views;

import controllers.PersonController;
import controllers.TicketCategoryController;
import controllers.TicketController;
import database.Database;
import models.Person;
import models.Ticket;
import models.TicketCategory;

public class ViewCommandLine extends View {

    public ViewCommandLine(Database<Person> personDatabase, Database<Ticket> ticketDatabase, Database<TicketCategory> ticketCategoryDatabase, PersonController personController, TicketController ticketController, TicketCategoryController ticketCategoryController) {
        super(personDatabase, ticketDatabase, ticketCategoryDatabase, personController, ticketController, ticketCategoryController);
    }
}
