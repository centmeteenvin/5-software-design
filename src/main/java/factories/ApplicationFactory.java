package factories;

import controllers.PersonController;
import controllers.TicketCategoryController;
import controllers.TicketController;
import database.Database;
import models.Person;
import models.Ticket;
import models.TicketCategory;
import views.View;

public interface ApplicationFactory {

    Database<Person> createPersonDataBase();
    Database<Ticket> createTicketDataBase();
    Database<TicketCategory> createTicketCategoryDatabase();

    PersonController createPersonController(Database<Person> personDatabase, Database<Ticket> ticketDatabase);
    TicketController createTicketController(Database<Ticket> ticketDatabase, Database<Person> personDatabase, Database<TicketCategory> ticketCategoryDatabase);
    TicketCategoryController createTicketCategoryController(Database<TicketCategory> ticketCategoryDatabase, Database<Ticket> ticketDatabase);

    View createView(Database<Person> personDatabase, Database<Ticket> ticketDatabase, Database<TicketCategory> ticketCategoryDatabase,
            PersonController personController, TicketController ticketController, TicketCategoryController ticketCategoryController
    );

    default void resolve(PersonController personController, TicketController ticketController, TicketCategoryController ticketCategoryController) {
        personController.setTicketController(ticketController);

        ticketController.setPersonController(personController);
        ticketController.setTicketCategoryController(ticketCategoryController);

        ticketCategoryController.setTicketController(ticketController);
    }
}

