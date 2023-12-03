package views.gui.components.panels;

import views.gui.styles.Style;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class HomePanel extends JPanel {
    Border testBorder = BorderFactory.createLineBorder(Color.RED, 1);


    public HomePanel(JPanel layoutPanel) {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setBackground(Style.getBackgroundColor_primary());
        this.add(createTitleSection());
        this.add(Box.createVerticalStrut(70));
        this.add(createButtonSection(layoutPanel));

        //this.setBorder(testBorder);
    }

    JPanel createTitleSection() {

        JPanel titleSection = new JPanel();
        titleSection.setLayout(new BoxLayout(titleSection, BoxLayout.Y_AXIS));
        titleSection.setBackground(null);

        JLabel title = new JLabel("SKMT");
        title.setForeground(Color.WHITE);
        title.setFont(Style.getTitleFont());
        title.setAlignmentX(CENTER_ALIGNMENT);


        JLabel subTitle = new JLabel("The Scripk Kiddo's Money Tracker");
        subTitle.setForeground(Color.WHITE);
        subTitle.setFont(Style.getSubtitleFont());
        subTitle.setAlignmentX(CENTER_ALIGNMENT);

        titleSection.add(title);
        titleSection.add(subTitle);

        //titleSection.setBorder(testBorder);
        return titleSection;
    }

    JPanel createButtonSection(JPanel layoutPanel) {
        Font buttonFont = Style.getButtonFont();

        JPanel buttonSection = new JPanel();
        buttonSection.setLayout(new BoxLayout(buttonSection, BoxLayout.X_AXIS));
        buttonSection.setBackground(null);

        JButton userButton = new JButton("Users");
        // Visual
        userButton.setForeground(Style.getButtonForegroundColor());
        userButton.setFont(buttonFont);
        userButton.setBackground(Style.getButtonBackgroundColor());
        // Function
        userButton.addActionListener(e -> {
            //System.out.println("User button pushed");
            CardLayout layout = (CardLayout) layoutPanel.getLayout();
            layout.show(layoutPanel, "PersonPanel");
        });


        JButton ticketButton = new JButton("Tickets");
        // Visual
        ticketButton.setForeground(Style.getButtonForegroundColor());
        ticketButton.setFont(buttonFont);
        ticketButton.setBackground(Style.getButtonBackgroundColor());
        // Function
        ticketButton.addActionListener(e -> {
            //System.out.println("Ticket button pushed");
            CardLayout layout = (CardLayout) layoutPanel.getLayout();
            layout.show(layoutPanel, "TicketPanel");
        });

        // Construct panel
        buttonSection.add(userButton);
        buttonSection.add(Box.createHorizontalStrut(50));
        buttonSection.add(ticketButton);

        //buttonSection.setBorder(testBorder);
        return buttonSection;
    }
}