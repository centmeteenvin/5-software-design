package views.gui;

import controllers.PersonController;
import controllers.TicketCategoryController;
import controllers.TicketController;
import database.Database;
import models.Person;
import models.Ticket;
import models.TicketCategory;
import views.View;
import views.gui.components.ViewFrame;
import views.gui.styles.Style;

import javax.swing.*;

public class ViewJ2D extends View {
    JFrame frame = new ViewFrame();


    public ViewJ2D(Database<Person> personDatabase, Database<Ticket> ticketDatabase, Database<TicketCategory> ticketCategoryDatabase, PersonController personController, TicketController ticketController, TicketCategoryController ticketCategoryController) {
        super(personDatabase, ticketDatabase, ticketCategoryDatabase, personController, ticketController, ticketCategoryController);
    }

    @Override
    public void run() {
        setupFrame();
    }

    public void setupFrame(){
        frame.setVisible(true);
    }
}
