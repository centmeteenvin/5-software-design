package views.gui.components.panels;

import views.gui.styles.Style;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class HomePanel extends JPanel {
    Border testBorder = BorderFactory.createLineBorder(Color.RED, 1);

    public HomePanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setBackground(Style.getBackgroundColor_primary());
        this.add(createTitleSection());
        this.add(Box.createVerticalStrut(70));
        this.add(createButtonSection());

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

        titleSection.setBorder(testBorder);
        return titleSection;
    }

    JPanel createButtonSection() {
        Font buttonFont = Style.getButtonFont();
        Dimension buttonSize = new Dimension(450,150);

        JPanel buttonSection = new JPanel();
        buttonSection.setLayout(new BoxLayout(buttonSection, BoxLayout.X_AXIS));
        buttonSection.setBackground(null);
        buttonSection.setSize(1500,500);

        JButton userButton = new JButton("Users");
        userButton.setForeground(Style.getButtonForegroundColor());
        userButton.setFont(buttonFont);
        userButton.setSize(buttonSize);
        userButton.setPreferredSize(buttonSize);
        userButton.setBackground(Style.getButtonBackgroundColor());


        JButton ticketButton = new JButton("Tickets");
        ticketButton.setForeground(Style.getButtonForegroundColor());
        ticketButton.setFont(buttonFont);
        ticketButton.setSize(buttonSize);
        ticketButton.setPreferredSize(buttonSize);
        ticketButton.setBackground(Style.getButtonBackgroundColor());

        buttonSection.add(userButton);
        buttonSection.add(Box.createHorizontalStrut(50));
        buttonSection.add(ticketButton);

        buttonSection.setBorder(testBorder);
        return buttonSection;
    }
}