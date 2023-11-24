import controllers.PersonController;
import controllers.TicketCategoryController;
import controllers.TicketController;
import database.Database;
import exceptions.notFoundExceptions.CategoryNotFoundException;
import exceptions.notFoundExceptions.PersonNotFoundException;
import factories.ApplicationFactoryProduction;
import lombok.Getter;
import models.Person;
import models.Ticket;
import models.TicketCategory;
import org.junit.jupiter.api.Test;
import views.View;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class IntegrationTest {
    TestApplicationFactory factory = new TestApplicationFactory();
    Application application = new Application(factory);

    Database<Person> personDb = application.getPersonDatabase();
    Database<Ticket> ticketDb = application.getTicketDatabase();
    Database<TicketCategory> categoryDb = application.getTicketCategoryDatabase();

    PersonController personController = application.getPersonController();
    TicketController ticketController = application.getTicketController();
    TicketCategoryController categoryController = application.getTicketCategoryController();

    @Test
    void integrationTest() throws PersonNotFoundException, CategoryNotFoundException {
        //Test if dependency injection is correct.
        //Test for correct db objects.
        assertEquals(personController.getPersonDatabase(), personDb);
        assertEquals(personController.getTicketDatabase(), ticketDb);

        assertEquals(ticketController.getPersonDatabase(), personDb);
        assertEquals(ticketController.getTicketDatabase(), ticketDb);
        assertEquals(ticketController.getTicketCategoryDatabase(), categoryDb);

        assertEquals(categoryController.getTicketDatabase(), ticketDb);
        assertEquals(categoryController.getTicketCategoryDatabase(), categoryDb);

        //Test for correct controllers
        assertEquals(personController.getTicketController(), ticketController);

        assertEquals(ticketController.getPersonController(), personController);
        assertEquals(ticketController.getTicketCategoryController(), categoryController);

        assertEquals(categoryController.getTicketController(), ticketController);

        //Create a person
        Optional<Person> personOptional = personController.create("foo");
        assertTrue(personOptional.isPresent());
        Person person = personOptional.get();
        assertEquals(person.getName(), "foo");
        assertTrue(person.getTicketsId().isEmpty());
        assertTrue(person.getDebts().isEmpty());
        personOptional = personDb.getById(person.getId());
        assertTrue(personOptional.isPresent());
        assertEquals(person, personOptional.get());

        //Create a Category
        Optional<TicketCategory> categoryOptional = categoryController.create("bar");
        assertTrue(categoryOptional.isPresent());
        TicketCategory category = categoryOptional.get();
        assertEquals(category.getName(), "bar");
        assertTrue(category.getTicketIds().isEmpty());
        categoryOptional = categoryDb.getById(category.getId());
        assertTrue(categoryOptional.isPresent());
        assertEquals(category, categoryOptional.get());

        //Create a Ticket
        Optional<Ticket> ticketOptional = ticketController.create(category.getId(), 100.5, List.of(person.getId()));
        assertTrue(ticketOptional.isPresent());
        Ticket ticket = ticketOptional.get();
        assertEquals(ticket.getCost(), 100.5);
        assertNull(ticket.getPayerId());
        assertEquals(ticket.getTicketCategoryId(), category.getId());
        assertTrue(category.getTicketIds().contains(ticket.getId()));
        assertTrue(ticket.getDistribution().containsKey(person.getId()));
        assertTrue(person.getTicketsId().contains(ticket.getId()));
        ticketOptional = ticketDb.getById(ticket.getId());
        assertTrue(ticketOptional.isPresent());
        assertEquals(ticket, ticketOptional.get());

        //Set the payer
        assertDoesNotThrow(() -> ticketController.setPayer(ticket.getId(), person.getId()));
        assertEquals(person.getId(), ticket.getPayerId());

        //Create and add a second person
        Optional<Person> person2Optional = personController.create("baz");
        assertTrue(person2Optional.isPresent());
        Person person2 = person2Optional.get();
        assertDoesNotThrow(() -> ticketController.addPerson(ticket.getId(), person2.getId()));
        assertTrue(person2.getTicketsId().contains(ticket.getId()));
        assertTrue(ticket.getDistribution().containsKey(person2.getId()));

        //Set the weights of both people
        assertDoesNotThrow(() -> ticketController.changeWeight(ticket.getId(), person.getId(), 100));
        assertDoesNotThrow(() -> ticketController.changeWeight(ticket.getId(), person2.getId(), .5));
        assertEquals(100, ticket.getDistribution().get(person.getId()));
        assertEquals(.5, ticket.getDistribution().get(person2.getId()));

        //Calculate The tickets
        assertDoesNotThrow(() -> ticketController.calculateAll());
        assertEquals(-0.5, person.getDebts().get(person2.getId()));
        assertEquals(0.5, person2.getDebts().get(person.getId()));
    }
}

@Getter
class TestApplicationFactory extends ApplicationFactoryProduction {
    private View view;

    @Override
    public View createView(Database<Person> personDatabase, Database<Ticket> ticketDatabase, Database<TicketCategory> ticketCategoryDatabase, PersonController personController, TicketController ticketController, TicketCategoryController ticketCategoryController) {
        return null;
    }
}
