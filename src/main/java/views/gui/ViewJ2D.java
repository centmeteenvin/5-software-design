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
import views.gui.components.panels.HomePanel;
import views.gui.components.panels.SamplePanel;

import javax.swing.*;
import java.awt.*;

public class ViewJ2D extends View {
    JFrame frame = new ViewFrame();
    JPanel container = new JPanel();


    public ViewJ2D(Database<Person> personDatabase, Database<Ticket> ticketDatabase, Database<TicketCategory> ticketCategoryDatabase, PersonController personController, TicketController ticketController, TicketCategoryController ticketCategoryController) {
        super(personDatabase, ticketDatabase, ticketCategoryDatabase, personController, ticketController, ticketCategoryController);
    }

    @Override
    public void run() {
        setupFrame();
    }

    public void setupFrame(){
        // Add card layout to the frame
        container = new JPanel();
        CardLayout layout = new CardLayout();
        container.setLayout(layout);
        container.add(new HomePanel(container), "HomePanel");
        container.add(new SamplePanel(1), "PersonPanel");
        container.add(new SamplePanel(2), "TicketPanel");

        layout.show(container,"HomePanel");
        frame.add(container);
        frame.setVisible(true);
    }
}
