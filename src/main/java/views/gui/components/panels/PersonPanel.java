package views.gui.components.panels;

import controllers.PersonController;
import database.Database;
import models.Person;
import views.gui.styles.Style;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

import static java.lang.Math.floor;

public class PersonPanel extends JPanel {
    JPanel layoutPanel;
    Style style;
    Database<Person> personDatabase;
    PersonController personController;
    DefaultListModel<String> listModel;

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public PersonPanel(JPanel layoutPanel, Style style, Database<Person> personDatabase, PersonController personController) {
        this.layoutPanel = layoutPanel;
        this.style = style;
        this.personDatabase = personDatabase;
        this.personController = personController;
        this.listModel = new DefaultListModel<>();

        this.setLayout(new BorderLayout());

        personController.create("Foo");
        personController.create("Bar");
        personController.create("Baz");

        this.add(createLeftPanel(), BorderLayout.LINE_START);
        this.add(createRightPanel(), BorderLayout.CENTER);
    }

    JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setPreferredSize(new Dimension(screenSize.width / 4, screenSize.height));
        leftPanel.setBackground(style.getBackgroundColor_primary());

        // Title (box 1)
        JLabel userLabel = new JLabel("Users") {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Get the width and height of the label
                int width = getWidth();
                int height = getHeight();

                // Draw the bottom line
                g.setColor(Color.WHITE);
                g.drawLine(0, height, width, height);
                g.drawLine(0, height - 1, width, height - 1);
                g.drawLine(0, height - 2, width, height - 2);
            }
        };
        userLabel.setMaximumSize(new Dimension(screenSize.width / 4, 100));
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(style.getBoldSubtitleFont());
        userLabel.setHorizontalAlignment(SwingConstants.CENTER);
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(userLabel);

        // Add buffer
        leftPanel.add(Box.createVerticalStrut(screenSize.height/20));

        // Add Person button (Box2)
        JButton createPersonButton = new JButton("+ Add User");
        createPersonButton.setForeground(style.getButtonForegroundColor());
        createPersonButton.setBackground(style.getButtonBackgroundColor());
        createPersonButton.setFont(style.getButtonFont());
        createPersonButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        createPersonButton.addActionListener(e -> {
            addUser();
        });
        leftPanel.add(createPersonButton);

        // Add buffer
        leftPanel.add(Box.createVerticalStrut(screenSize.height/20));

        // Add list
        JList<String> personJList = new JList<>(listModel);
        personJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        personJList.setFixedCellHeight(screenSize.height/20);
        personJList.setFixedCellWidth(screenSize.width/5);
        personJList.setFont(style.getListFont());
        personJList.setForeground(style.getListForegroundColor());
        personJList.setBackground(style.getListBackgroundColor());
        updatePersonList();
        leftPanel.add(personJList);
        return leftPanel;
    }

    private void updatePersonList() {
        listModel.clear();
        for (Person person : personDatabase.getAll()){
            listModel.addElement(person.getName());
        }
    }

    JPanel createRightPanel() {
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(style.getBackgroundColor_secondary());
        return rightPanel;
    }

    private void addUser() {
        String name = JOptionPane.showInputDialog(null, "Enter your full name");
        while (name != null && name.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Invalid input, please enter a correct name");
            name = JOptionPane.showInputDialog(null, "Enter your full name");
        }
        if (name == null) {
            return;
        }
        Optional<Person> optionalPerson = personController.create(name);

        if (optionalPerson.isEmpty()) {
            JOptionPane.showMessageDialog(null, "An Error occurred creating the person");
            return;
        }
        JOptionPane.showMessageDialog(null, "Successfully created person: %s".formatted(optionalPerson.get().getName()));

    }
}
