package factories;

import controllers.PersonController;
import controllers.TicketCategoryController;
import controllers.TicketController;
import database.Database;
import models.Person;
import models.Ticket;
import models.TicketCategory;
import org.junit.jupiter.api.Test;
import views.View;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApplicationFactoryTest {

    @Test
    void resolve() {
        ApplicationFactory factory = getFactory();
        PersonController personController = mock(PersonController.class);
        TicketController ticketController = mock(TicketController.class);
        TicketCategoryController ticketCategoryController = mock(TicketCategoryController.class);

        doNothing().when(personController).setTicketController(ticketController);
        doNothing().when(ticketController).setTicketCategoryController(ticketCategoryController);
        doNothing().when(ticketController).setPersonController(personController);
        doNothing().when(ticketCategoryController).setTicketController(ticketController);

        factory.resolve(personController, ticketController, ticketCategoryController);

        verify(personController, times(1)).setTicketController(ticketController);
        verify(ticketController, times(1)).setPersonController(personController);
        verify(ticketController, times(1)).setTicketCategoryController(ticketCategoryController);
        verify(ticketCategoryController, times(1)).setTicketController(ticketController);
    }
    protected ApplicationFactory getFactory() {
        return new ApplicationFactoryTestClass();
    }
    private static class ApplicationFactoryTestClass implements ApplicationFactory {
        @Override
        public Database<Person> createPersonDataBase() {
            return null;
        }

        @Override
        public Database<Ticket> createTicketDataBase() {
            return null;
        }

        @Override
        public Database<TicketCategory> createTicketCategoryDatabase() {
            return null;
        }

        @Override
        public PersonController createPersonController(Database<Person> personDatabase, Database<Ticket> ticketDatabase) {
            return null;
        }

        @Override
        public TicketController createTicketController(Database<Ticket> ticketDatabase, Database<Person> personDatabase, Database<TicketCategory> ticketCategoryDatabase) {
            return null;
        }

        @Override
        public TicketCategoryController createTicketCategoryController(Database<TicketCategory> ticketCategoryDatabase, Database<Ticket> ticketDatabase) {
            return null;
        }

        @Override
        public View createView(Database<Person> personDatabase, Database<Ticket> ticketDatabase, Database<TicketCategory> ticketCategoryDatabase, PersonController personController, TicketController ticketController, TicketCategoryController ticketCategoryController) {
            return null;
        }
    }

}