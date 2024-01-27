import controllers.PersonController;
import controllers.TicketCategoryController;
import controllers.TicketController;
import database.Database;
import factories.ApplicationFactory;
import models.Person;
import models.Ticket;
import models.TicketCategory;
import views.View;

public class Application {

    private final Database<Person> personDatabase;
    private final Database<Ticket> ticketDatabase;
    private final Database<TicketCategory> ticketCategoryDatabase;

    private final PersonController personController;
    private final TicketController ticketController;
    private final TicketCategoryController ticketCategoryController;

    private final View view;

    public Application(ApplicationFactory factory) {

        personDatabase = factory.createPersonDataBase();
        ticketDatabase = factory.createTicketDataBase();
        ticketCategoryDatabase = factory.createTicketCategoryDatabase();

        personController = factory.createPersonController(personDatabase, ticketDatabase);
        ticketController = factory.createTicketController(ticketDatabase, personDatabase, ticketCategoryDatabase);
        ticketCategoryController = factory.createTicketCategoryController(ticketCategoryDatabase, ticketDatabase);

        factory.resolve(personController, ticketController, ticketCategoryController);

        view = factory.createView(personDatabase, ticketDatabase, ticketCategoryDatabase, personController, ticketController, ticketCategoryController);
    }

    public void run() {
        view.run();
    }
}
