package views.gui.components.panels;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class HomePanel extends JPanel {
    Border testBorder = BorderFactory.createLineBorder(Color.RED, 1);

    public HomePanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setBackground(Color.BLUE);
        this.add(createTitleSection());
        this.add(Box.createVerticalStrut(70));
        this.add(createButtonSection());


        //this.setBorder(testBorder);


    }

    private Font addFont(String fontName, float size) {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/fonts/" + fontName + ".ttf")).deriveFont(size);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
            return font;
        } catch (IOException | FontFormatException e) {
            throw new RuntimeException(e);
        }
    }

    JPanel createTitleSection() {
        Font titleFont = addFont("OpenSans-Bold", 122);
        Font subtitleFont = addFont("OpenSans-Medium", 54);


        JPanel titleSection = new JPanel();
        titleSection.setLayout(new BoxLayout(titleSection, BoxLayout.Y_AXIS));
        titleSection.setBackground(null);

        JLabel title = new JLabel("SKMT");
        title.setForeground(Color.WHITE);
        title.setFont(titleFont);
        title.setAlignmentX(CENTER_ALIGNMENT);


        JLabel subTitle = new JLabel("The Scripk Kiddo's Money Tracker");
        subTitle.setForeground(Color.WHITE);
        subTitle.setFont(subtitleFont);
        subTitle.setAlignmentX(CENTER_ALIGNMENT);

        titleSection.add(title);
        titleSection.add(subTitle);

        //titleSection.setBorder(testBorder);
        return titleSection;
    }

    JPanel createButtonSection() {
        Font buttonFont = addFont("OpenSans-Bold", 30);


        JPanel buttonSection = new JPanel();
        buttonSection.setLayout(new BoxLayout(buttonSection, BoxLayout.X_AXIS));
        buttonSection.setBackground(null);

        JButton userButton = new JButton("Users");
        userButton.setForeground(Color.BLUE);
        userButton.setFont(buttonFont);
        userButton.setSize(300, 100);
        userButton.setBackground(Color.WHITE);

        JButton ticketButton = new JButton("Tickets");
        ticketButton.setForeground(Color.BLUE);
        ticketButton.setFont(buttonFont);

        buttonSection.add(userButton);
        buttonSection.add(Box.createHorizontalStrut(50));
        buttonSection.add(ticketButton);

        //buttonSection.setBorder(testBorder);
        return buttonSection;
    }
}