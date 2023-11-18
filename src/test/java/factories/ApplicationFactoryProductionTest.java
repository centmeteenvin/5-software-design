package factories;

import controllers.*;
import database.Database;
import database.DatabaseHashMap;
import models.Person;
import models.Ticket;
import models.TicketCategory;
import org.junit.jupiter.api.Test;
import views.View;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationFactoryProductionTest {
    private final ApplicationFactoryProduction factory = new ApplicationFactoryProductionConcrete();

    @Test
    void createPersonDataBase() {
        assertTrue(factory.createPersonDataBase() instanceof DatabaseHashMap<Person>);
    }

    @Test
    void createTicketDataBase() {
        assertTrue(factory.createTicketDataBase() instanceof DatabaseHashMap<Ticket>);
    }

    @Test
    void createTicketCategoryDatabase() {
        assertTrue(factory.createTicketCategoryDatabase() instanceof DatabaseHashMap<TicketCategory>);
    }

    @Test
    void createPersonController() {
        assertTrue(factory.createPersonController(null, null) instanceof PersonControllerImplementation);
    }

    @Test
    void createTicketController() {
        assertTrue(factory.createTicketController(null, null, null) instanceof TicketControllerImplementation);
    }

    @Test
    void createTicketCategoryController() {
        assertTrue(factory.createTicketCategoryController(null, null) instanceof TicketCategoryControllerImplementation);
    }

    private static class ApplicationFactoryProductionConcrete extends ApplicationFactoryProduction {
        @Override
        public View createView(Database<Person> personDatabase, Database<Ticket> ticketDatabase, Database<TicketCategory> ticketCategoryDatabase, PersonController personController, TicketController ticketController, TicketCategoryController ticketCategoryController) {
            return null;
        }
    }
}