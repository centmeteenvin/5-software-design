package views;

import controllers.PersonController;
import controllers.TicketCategoryController;
import controllers.TicketController;
import database.Database;
import lombok.Getter;
import models.Person;
import models.Ticket;
import models.TicketCategory;

@Getter
public abstract class View {

    protected final Database<Person> personDatabase;
    protected final Database<Ticket> ticketDatabase;
    protected final Database<TicketCategory> ticketCategoryDatabase;

    protected final PersonController personController;
    protected final TicketController ticketController;
    protected final TicketCategoryController ticketCategoryController;


    public View(Database<Person> personDatabase, Database<Ticket> ticketDatabase, Database<TicketCategory> ticketCategoryDatabase, PersonController personController, TicketController ticketController, TicketCategoryController ticketCategoryController) {
        this.personDatabase = personDatabase;
        this.ticketDatabase = ticketDatabase;
        this.ticketCategoryDatabase = ticketCategoryDatabase;
        this.personController = personController;
        this.ticketController = ticketController;
        this.ticketCategoryController = ticketCategoryController;
    }

    public abstract void run();
}
