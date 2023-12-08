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
import views.gui.components.observers.PersonDatabaseObserver;
import views.gui.components.panels.HomePanel;
import views.gui.components.panels.PersonPanel;
import views.gui.components.panels.SamplePanel;
import views.gui.styles.Style;
import views.gui.styles.StyleBlueWhite;

import javax.swing.*;
import java.awt.*;

public class ViewJ2D extends View {
    JFrame frame = new ViewFrame();
    JPanel container = new JPanel();
    HomePanel homePanel;
    PersonPanel personPanel;


    public ViewJ2D(Database<Person> personDatabase, Database<Ticket> ticketDatabase, Database<TicketCategory> ticketCategoryDatabase, PersonController personController, TicketController ticketController, TicketCategoryController ticketCategoryController) {
        super(personDatabase, ticketDatabase, ticketCategoryDatabase, personController, ticketController, ticketCategoryController);

        PersonDatabaseObserver personDatabaseObserver = new PersonDatabaseObserver(personDatabase,personController,this);
        personDatabase.addListener(personDatabaseObserver);
    }

    public HomePanel getHomePanel() {
        return homePanel;
    }

    public PersonPanel getPersonPanel() {
        return personPanel;
    }

    @Override
    public void run() {
        setupFrame();
    }

    public void setupFrame(){
        // Determine style
        Style style = new StyleBlueWhite();

        // Create container that will hold the different panels
        container = new JPanel();

        // Add card layout to the frame
        CardLayout layout = new CardLayout();
        container.setLayout(layout);

        // Create different panels
        this.homePanel = new HomePanel(container,style);
        this.personPanel = new PersonPanel(container,style, getPersonDatabase(), getPersonController());

        // Add every panel to the container
        container.add(this.homePanel, "HomePanel");
        container.add(this.personPanel, "PersonPanel");
        container.add(new SamplePanel(2,style), "TicketPanel");

        // Show the homepanel
        layout.show(container,"HomePanel");

        // Add the container to the frame
        frame.add(container);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
