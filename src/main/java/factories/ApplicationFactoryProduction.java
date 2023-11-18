package factories;

import controllers.*;
import database.Database;
import database.DatabaseHashMap;
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
        return new DatabaseHashMap<>();
    }

    @Override
    public final Database<Ticket> createTicketDataBase() {
        return new DatabaseHashMap<>();
    }

    @Override
    public final Database<TicketCategory> createTicketCategoryDatabase() {
        return new DatabaseHashMap<>();
    }

    @Override
    public final PersonController createPersonController(Database<Person> personDatabase, Database<Ticket> ticketDatabase) {
        return new PersonControllerImplementation(personDatabase, ticketDatabase);
    }

    @Override
    public final TicketController createTicketController(Database<Ticket> ticketDatabase, Database<Person> personDatabase, Database<TicketCategory> ticketCategoryDatabase) {
        return new TicketControllerImplementation(ticketDatabase, personDatabase, ticketCategoryDatabase);
    }

    @Override
    public final TicketCategoryController createTicketCategoryController(Database<TicketCategory> ticketCategoryDatabase, Database<Ticket> ticketDatabase) {
        return new TicketCategoryControllerImplementation(ticketCategoryDatabase, ticketDatabase);
    }
}
