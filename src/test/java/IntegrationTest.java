import controllers.PersonController;
import controllers.TicketCategoryController;
import controllers.TicketController;
import database.Database;
import factories.ApplicationFactoryProduction;
import lombok.Getter;
import models.Person;
import models.Ticket;
import models.TicketCategory;
import org.junit.jupiter.api.Test;
import views.View;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    void integrationTest() {
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
