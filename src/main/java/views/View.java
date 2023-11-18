package views;

import controllers.PersonController;
import controllers.TicketCategoryController;
import controllers.TicketController;
import database.Database;
import models.Person;
import models.Ticket;
import models.TicketCategory;

public abstract class View {

    private final Database<Person> personDatabase;
    private final Database<Ticket> ticketDatabase;
    private final Database<TicketCategory> ticketCategoryDatabase;

    private final PersonController personController;
    private final TicketController ticketController;
    private final TicketCategoryController ticketCategoryController;


    public View(Database<Person> personDatabase, Database<Ticket> ticketDatabase, Database<TicketCategory> ticketCategoryDatabase, PersonController personController, TicketController ticketController, TicketCategoryController ticketCategoryController) {
        this.personDatabase = personDatabase;
        this.ticketDatabase = ticketDatabase;
        this.ticketCategoryDatabase = ticketCategoryDatabase;
        this.personController = personController;
        this.ticketController = ticketController;
        this.ticketCategoryController = ticketCategoryController;
    }
}
