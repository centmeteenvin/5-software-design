package factories;

import controllers.*;
import database.Database;
import database.DatabaseHashMap;
import models.Person;
import models.Ticket;
import models.TicketCategory;
import org.junit.jupiter.api.Test;
import views.View;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ApplicationFactoryProductionTest extends ApplicationFactoryTest {
    @Test
    void createPersonDataBase() {
        assertTrue(getFactory().createPersonDataBase() instanceof DatabaseHashMap<Person>);
    }

    @Test
    void createTicketDataBase() {
        assertTrue(getFactory().createTicketDataBase() instanceof DatabaseHashMap<Ticket>);
    }

    @Test
    void createTicketCategoryDatabase() {
        assertTrue(getFactory().createTicketCategoryDatabase() instanceof DatabaseHashMap<TicketCategory>);
    }

    @Test
    void createPersonController() {
        assertTrue(getFactory().createPersonController(null, null) instanceof PersonControllerImplementation);
    }

    @Test
    void createTicketController() {
        assertTrue(getFactory().createTicketController(null, null, null) instanceof TicketControllerImplementation);
    }

    @Test
    void createTicketCategoryController() {
        assertTrue(getFactory().createTicketCategoryController(null, null) instanceof TicketCategoryControllerImplementation);
    }

    @Override
    protected ApplicationFactory getFactory() {
        return new ApplicationFactoryProductionConcrete();
    }

    private static class ApplicationFactoryProductionConcrete extends ApplicationFactoryProduction {
        @Override
        public View createView(Database<Person> personDatabase, Database<Ticket> ticketDatabase, Database<TicketCategory> ticketCategoryDatabase, PersonController personController, TicketController ticketController, TicketCategoryController ticketCategoryController) {
            return null;
        }
    }
}