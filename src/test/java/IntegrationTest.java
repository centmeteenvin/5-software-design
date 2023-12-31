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

        //Update the weights so that they don't add to the total cost anymore,
        assertDoesNotThrow(() -> ticketController.changeWeight(ticket.getId(), person.getId(), 25));

        //calculate again, weights should all be 0
        assertDoesNotThrow(() -> ticketController.calculateAll());
        assertEquals(0., person.getDebts().values().stream().reduce(0., Double::sum));
        assertEquals(0., person2.getDebts().values().stream().reduce(0., Double::sum));

        //Create a third person, add it to the list, set its weight to 75 and calculate
        Optional<Person> person3Optional = personController.create("baz");
        assertTrue(person3Optional.isPresent());
        Person person3 = person3Optional.get();

        assertDoesNotThrow(() -> ticketController.addPerson(ticket.getId(), person3.getId()));
        assertEquals(3, ticket.getDistribution().size());
        assertTrue(ticket.getDistribution().containsKey(person3.getId()));

        assertDoesNotThrow(() -> ticketController.changeWeight(ticket.getId(), person3.getId(), 75));
        assertDoesNotThrow(() -> ticketController.calculateAll());
        assertEquals(-75, person.getDebts().get(person3.getId()));
        assertEquals( 75, person3.getDebts().get(person.getId()));

        //person 3 pays of person
        assertDoesNotThrow(() -> personController.pay(person3.getId(), person.getId(), 75));
        assertDoesNotThrow(() -> ticketController.calculateAll());
        assertEquals(0, person.getDebts().get(person3.getId()));
        assertEquals( 0, person3.getDebts().get(person.getId()));

        //Remove person3, No tickets should be valid
        assertDoesNotThrow(() -> personController.delete(person3.getId()));
        assertDoesNotThrow(() -> ticketController.calculateAll());
        assertEquals(0., person.getDebts().values().stream().reduce(0., Double::sum));
        assertEquals(0., person2.getDebts().values().stream().reduce(0., Double::sum));
        assertEquals(2, ticket.getDistribution().size());

        //Adjust weights of ticket to make it valid again
        assertDoesNotThrow(() -> ticketController.changeWeight(ticket.getId(), person.getId(), 100));
        assertDoesNotThrow(() -> ticketController.calculateAll());
        assertEquals(-0.5, person.getDebts().get(person2.getId()));
        assertEquals(0.5, person2.getDebts().get(person.getId()));

        //Change the cost of the ticket.
        assertDoesNotThrow(() -> ticketController.changeCost(ticket.getId(), 125));
        assertEquals(125, ticket.getCost());

        //Create a new category and update the ticket to it.
        Optional<TicketCategory> category2Optional = categoryController.create("var");
        assertTrue(category2Optional.isPresent());
        TicketCategory category2 = category2Optional.get();

        assertDoesNotThrow(() -> ticketController.changeCategory(ticket.getId(), category2.getId()));
        assertEquals(category2.getId(), ticket.getTicketCategoryId());
        assertTrue(category.getTicketIds().isEmpty());
        assertTrue(category2.getTicketIds().contains(ticket.getId()));

        //Delete the second category
        assertDoesNotThrow(() -> categoryController.delete(category2.getId()));
        assertTrue(categoryDb.getById(category2.getId()).isEmpty());
        assertNull(ticket.getTicketCategoryId());

        //Change the name of the category
        assertDoesNotThrow(() -> categoryController.rename(category.getId(), "laz"));
        assertEquals("laz", category.getName());

        //Change the name of the person
        assertDoesNotThrow(() -> personController.rename(person.getId(), "FOO"));
        assertEquals("FOO", person.getName());

        //Delete the ticket
        assertDoesNotThrow(() -> ticketController.delete(ticket.getId()));
        assertEquals(1, person.getTicketsId().size());
        assertEquals(0, person2.getTicketsId().size());
        assertTrue(category.getTicketIds().isEmpty());

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
