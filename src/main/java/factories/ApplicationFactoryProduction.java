package factories;

import controllers.PersonController;
import controllers.TicketCategoryController;
import controllers.TicketController;
import database.Database;
import models.Person;
import models.Ticket;
import models.TicketCategory;

/**
 * This is the factory that returns all the concrete Implementations of the controllers and the databases.
 * It does not yet define the view it will use.
 */
public abstract class ApplicationFactoryProduction implements AbstractApplicationFactory{
    @Override
    public final Database<Person> createPersonDataBase() {
        return null;
    }

    @Override
    public final Database<Ticket> createTicketDataBase() {
        return null;
    }

    @Override
    public final Database<TicketCategory> createTicketCategoryDatabase() {
        return null;
    }

    @Override
    public final PersonController createPersonController(Database<Person> personDatabase, Database<Ticket> ticketDatabase) {
        return null;
    }

    @Override
    public final TicketController createTicketController(Database<Ticket> ticketDatabase, Database<Person> personDatabase, Database<TicketCategory> ticketCategoryDatabase) {
        return null;
    }

    @Override
    public final TicketCategoryController createTicketCategoryController(Database<TicketCategory> ticketCategoryDatabase, Database<Ticket> ticketDatabase) {
        return null;
    }
}
