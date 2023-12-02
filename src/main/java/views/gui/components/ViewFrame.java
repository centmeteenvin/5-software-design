package views.gui.components;

import views.gui.components.panels.HomePanel;
import views.gui.components.panels.SamplePanel;

import javax.swing.*;
import java.awt.*;

public class ViewFrame extends JFrame {
    public ViewFrame() {
        this.setTitle("SKMT - Script Kiddos' Money Tracker");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(new Dimension(screenSize.width-100, screenSize.height-50));

        // Add card layout to the frame
        JPanel container = new JPanel();
        CardLayout layout = new CardLayout();
        container.setLayout(layout);
        container.add(new HomePanel(), "HomePanel");
        container.add(new SamplePanel(1), "PersonPanel");
        container.add(new SamplePanel(2), "TicketPanel");

        layout.show(container,"HomePanel");
        this.add(container);
    }
}
