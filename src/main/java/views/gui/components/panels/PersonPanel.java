package views.gui.components.panels;

import controllers.PersonController;
import database.Database;
import database.Property;
import models.Person;
import views.gui.styles.Style;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;
import java.util.Optional;

public class PersonPanel extends JPanel implements ListSelectionListener, PropertyChangeListener {
    JPanel layoutPanel;
    Style style;
    Database<Person> personDatabase;
    PersonController personController;
    DefaultListModel<Person> listModel;
    JList<Person> personJList;
    JPanel rightPanel;
    CardLayout rightPanelLayout;
    LabelFactory labelFactory;

    int horizontalOffset = 10;

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public PersonPanel(JPanel layoutPanel, Style style, Database<Person> personDatabase, PersonController personController) {
        this.layoutPanel = layoutPanel;
        this.style = style;
        this.personDatabase = personDatabase;
        this.personController = personController;
        this.listModel = new DefaultListModel<>();
        this.rightPanel = new JPanel();
        this.rightPanelLayout = new CardLayout();
        this.labelFactory = new LabelFactory(this.style);

        this.setLayout(new BorderLayout());

        this.add(createLeftPanel(), BorderLayout.LINE_START);

        this.rightPanel.setLayout(rightPanelLayout);
        rightPanel.add(createEmptyRightPanel(), "EmptyPanel");
        this.add(this.rightPanel, BorderLayout.CENTER);
    }

    JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setPreferredSize(new Dimension(screenSize.width / 4, screenSize.height));
        leftPanel.setBackground(style.getBackgroundPrimaryColor());

        // Title (box 1)
        JLabel userLabel = new JLabel("Users") {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Get the width and height of the label
                int width = getWidth();
                int height = getHeight();

                // Draw the bottom line
                g.setColor(style.getLineSecondaryColor());
                g.drawLine(0, height, width, height);
                g.drawLine(0, height - 1, width, height - 1);
                g.drawLine(0, height - 2, width, height - 2);
            }
        };
        userLabel.setMaximumSize(new Dimension(screenSize.width / 4, 100));
        userLabel.setForeground(style.getLabel2ForegroundColor());
        userLabel.setFont(style.getBoldSubtitleFont());
        userLabel.setHorizontalAlignment(SwingConstants.CENTER);
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(userLabel);

        // Add buffer
        leftPanel.add(Box.createVerticalStrut(10));

        // Add Person button (Box2)
        JButton createPersonButton = new JButton("+ Add User");
        createPersonButton.setForeground(style.getButton1ForegroundColor());
        createPersonButton.setBackground(style.getButton1BackgroundColor());
        createPersonButton.setFont(style.getButtonFont());
        createPersonButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        createPersonButton.addActionListener(e -> {
            addUser();
        });
        leftPanel.add(createPersonButton);

        // Add buffer
        leftPanel.add(Box.createVerticalStrut(screenSize.height / 20));

        // Add list
        personJList = new JList<>(listModel);
        personJList.setCellRenderer(new PersonListCellRenderer());
        personJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        personJList.setFixedCellHeight(screenSize.height / 20);
        personJList.setFixedCellWidth(screenSize.width / 5);
        personJList.setFont(style.getListFont());
        personJList.setForeground(style.getListForegroundColor());
        personJList.setBackground(style.getListBackgroundColor());
        personJList.addListSelectionListener(this);
        leftPanel.add(personJList);
        return leftPanel;
    }

    Box createRightPanel(Person person) throws NullPointerException {
        Box rightPanel = Box.createVerticalBox();
        rightPanel.setBackground(style.getBackgroundSecondaryColor());
        rightPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.setAlignmentY(Component.CENTER_ALIGNMENT);

        // Add top with name and buttons
        JPanel topContainer = createTopContainer(person.getName());
        rightPanel.add(topContainer);

        // Add username with button to change name
        Box usernameContainer = createUsernameContainer(person.getName());
        rightPanel.add(usernameContainer);

        // Add Id
        Box userIdContainer = createUserIdContainer(person.getId());
        rightPanel.add(userIdContainer);

        // Add debts
        Box userDebtContainer = Box.createVerticalBox();
        userDebtContainer.setBackground(style.getBackgroundSecondaryColor());
        userDebtContainer.setMaximumSize(new Dimension(3 * screenSize.width / 4, 1000));
        userDebtContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        userDebtContainer.setAlignmentY(Component.CENTER_ALIGNMENT);

        Box debtLabelContainer = Box.createHorizontalBox();
        debtLabelContainer.setMaximumSize(new Dimension(2 * screenSize.width, 75));
        debtLabelContainer.add(Box.createHorizontalStrut(horizontalOffset));
        debtLabelContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        debtLabelContainer.setAlignmentY(Component.CENTER_ALIGNMENT);

        JLabel userDebtLabel = labelFactory.getSecondaryNormalLabel("Debts :");
        userDebtLabel.setMaximumSize(new Dimension(screenSize.width / 7, 75));
        debtLabelContainer.add(userDebtLabel);

        userDebtContainer.add(debtLabelContainer);
        // Column 1 with subtitle
        Map<Long, Double> debts = person.getDebts();

        for (Long key : debts.keySet()) {
            Optional<Person> optDebtHolder = personDatabase.getById(key);
            if (optDebtHolder.isEmpty()) continue;
            Box row = labelFactory.getSmallRow(optDebtHolder.get().getName(), debts.get(key));
            userDebtContainer.add(row);
        }

        rightPanel.add(userDebtContainer);

        return rightPanel;
    }

    private Box createUsernameContainer(String name) {
        Box usernameContainer = Box.createHorizontalBox();
        usernameContainer.setBackground(style.getBackgroundSecondaryColor());
        usernameContainer.setMaximumSize(new Dimension(3 * screenSize.width / 4, 75));
        usernameContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        usernameContainer.setAlignmentY(Component.CENTER_ALIGNMENT);

        usernameContainer.add(Box.createHorizontalStrut(horizontalOffset));

        JLabel usernameLabel = labelFactory.getSecondaryNormalLabel("Username :");
        usernameLabel.setMaximumSize(new Dimension(screenSize.width / 7, 75));

        usernameContainer.add(usernameLabel);

        JLabel personInViewLabel = labelFactory.getSecondaryNormalLabel(name);
        personInViewLabel.setMaximumSize(new Dimension(screenSize.width / 6, 75));

        usernameContainer.add(personInViewLabel);

        JButton changeNameButton = labelFactory.getPrimaryButton("change name");
        changeNameButton.addActionListener(e -> {
            System.out.println("Change name");
        });
        changeNameButton.setAlignmentY(Component.CENTER_ALIGNMENT);
        usernameContainer.add(changeNameButton);
        return usernameContainer;
    }

    Box createEmptyRightPanel() {
        Box rightPanel = Box.createVerticalBox();
        rightPanel.setBackground(style.getBackgroundSecondaryColor());

        // Add top with name and buttons
        JPanel topContainer = createTopContainer("");
        rightPanel.add(topContainer);

        return rightPanel;
    }


    private Box createUserIdContainer(Long id) {
        Box userIdContainer = Box.createHorizontalBox();
        userIdContainer.setBackground(style.getBackgroundSecondaryColor());
        userIdContainer.setMaximumSize(new Dimension(3 * screenSize.width / 4, 75));
        userIdContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        userIdContainer.setAlignmentY(Component.CENTER_ALIGNMENT);

        userIdContainer.add(Box.createHorizontalStrut(horizontalOffset));

        JLabel userIdLabel = labelFactory.getSecondaryNormalLabel("Id :");
        userIdLabel.setMaximumSize(new Dimension(screenSize.width / 7, 75));
        userIdContainer.add(userIdLabel);

        JLabel personInViewLabel = labelFactory.getSecondaryNormalLabel(String.valueOf(String.valueOf(id)));
        personInViewLabel.setMaximumSize(new Dimension(screenSize.width / 6, 100));
        userIdContainer.add(personInViewLabel);

        return userIdContainer;
    }

    private JPanel createTopContainer(String name) {
        // Create container for top panel
        JPanel topContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Get the width and height of the label
                int width = getWidth();
                int height = getHeight();

                // Draw the bottom line
                g.setColor(style.getBackgroundPrimaryColor());
                g.drawLine(0, height, width, height);
                g.drawLine(0, height - 1, width, height - 1);
                g.drawLine(0, height - 2, width, height - 2);
            }
        };
        topContainer.setMaximumSize(new Dimension(3 * screenSize.width / 4, 100));
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.X_AXIS));
        topContainer.add(Box.createHorizontalStrut(horizontalOffset));
        topContainer.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Name of the person in view
        JLabel personName = labelFactory.getSubtitleLabel(name);
        personName.setMaximumSize(new Dimension(screenSize.width / 2, 100));
        personName.setAlignmentY(Component.CENTER_ALIGNMENT);
        topContainer.add(personName);

        // Button to homepage
        JButton homepageButton = labelFactory.getPrimaryButton("Homepage");
        homepageButton.addActionListener(e -> {
            CardLayout layout = (CardLayout) layoutPanel.getLayout();
            layout.show(layoutPanel, "HomePanel");
        });
        homepageButton.setAlignmentY(Component.CENTER_ALIGNMENT);
        topContainer.add(homepageButton);

        topContainer.add(Box.createHorizontalStrut(horizontalOffset));

        // Button to ticketpage
        JButton ticketpageButton = labelFactory.getPrimaryButton("Ticketpage");
        ticketpageButton.addActionListener(e -> {
            CardLayout layout = (CardLayout) layoutPanel.getLayout();
            layout.show(layoutPanel, "TicketPanel");
        });
        ticketpageButton.setAlignmentY(Component.CENTER_ALIGNMENT);
        topContainer.add(ticketpageButton);
        return topContainer;
    }

    public void updatePersonList() {
        listModel.clear();
        for (Person person : personDatabase.getAll()) {
            listModel.addElement(person);
        }
        personJList.setModel(listModel);
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

    /**
     * This function will run every time a different value is selected in the Jlist
     *
     * @param e the event that characterizes the change.
     */
    @Override
    public void valueChanged(ListSelectionEvent e) {
        Person selectedPerson = this.personJList.getSelectedValue();
        if (selectedPerson == null) {
            this.rightPanelLayout.show(this.rightPanel, "EmptyPanel");
        } else {
            this.rightPanel.add(createRightPanel(selectedPerson), "rightPanel");
            this.rightPanelLayout.show(this.rightPanel, "rightPanel");
        }
    }

    /**
     * This function will update this object whenever a change happens in the PersonDatabase
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(Property.CREATE.name)) {
            updatePersonList();
        } else if (evt.getPropertyName().equals(Property.UPDATE.name)) {
            updatePersonList();
        } else if (evt.getPropertyName().equals(Property.DELETE.name)) {
            updatePersonList();
        }
    }

    // Private object so that we can pass a Person in to the list, but only show its name and id
    private class PersonListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            // Call the super method to get the default cell renderer component
            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            // If the value is an instance of Person, display the desired property
            if (value instanceof Person person) {
                setText(person.getName() + "   #" + person.getId()); // Assuming getName() is the desired property
            }
            return c;
        }
    }

    private class LabelFactory {
        Style style;

        public LabelFactory(Style style) {
            this.style = style;
        }

        public Box getSmallRow(String name, double amount) {
            Box box = Box.createHorizontalBox();
            box.setMaximumSize(new Dimension(screenSize.width / 2, 50));
            box.setAlignmentX(Component.LEFT_ALIGNMENT);
            box.setAlignmentY(Component.CENTER_ALIGNMENT);
            box.add(Box.createHorizontalStrut(3 * horizontalOffset));

            JLabel personLabel = getSecondarySmallLabel(name + ":");
            personLabel.setMaximumSize(new Dimension(300, 50));

            JLabel debtLabel = getSecondarySmallLabel("€ " + amount);
            debtLabel.setMaximumSize(new Dimension(200, 50));

            box.add(personLabel);
            box.add(debtLabel);

            if (amount >= 0.) {
                debtLabel.setForeground(new Color(0, 153, 51));
            } else {
                debtLabel.setForeground(Color.red);

                JButton payButton = getPrimaryButton("Pay");
                payButton.setMaximumSize(new Dimension(100, 50));
                payButton.addActionListener(e -> {
                    System.out.println("Pay");
                });
                box.add(payButton);
            }
            return box;
        }

        public JLabel getPrimaryNormalLabel(String text) {
            JLabel label = new JLabel(text);
            label.setBackground(null);
            label.setForeground(this.style.getButton2ForegroundColor());
            label.setFont(this.style.getTextFont());
            label.setHorizontalAlignment(SwingConstants.LEFT);
            label.setAlignmentX(Component.LEFT_ALIGNMENT);
            label.setAlignmentY(Component.CENTER_ALIGNMENT);
            return label;
        }

        public JLabel getSecondaryNormalLabel(String text) {
            JLabel label = new JLabel(text);
            label.setBackground(null);
            label.setForeground(this.style.getButton1ForegroundColor());
            label.setFont(this.style.getTextFont());
            label.setHorizontalAlignment(SwingConstants.LEFT);
            label.setAlignmentX(Component.LEFT_ALIGNMENT);
            label.setAlignmentY(Component.CENTER_ALIGNMENT);
            return label;
        }

        public JLabel getPrimarySmallLabel(String text) {
            JLabel label = new JLabel(text);
            label.setBackground(null);
            label.setForeground(this.style.getButton2ForegroundColor());
            label.setFont(this.style.getSmallTextFont());
            label.setHorizontalAlignment(SwingConstants.LEFT);
            label.setAlignmentX(Component.LEFT_ALIGNMENT);
            label.setAlignmentY(Component.CENTER_ALIGNMENT);
            return label;
        }

        public JLabel getSubtitleLabel(String text) {
            JLabel label = new JLabel(text);
            label.setForeground(style.getButton1ForegroundColor());
            label.setFont(style.getBoldSubtitleFont());
            label.setHorizontalAlignment(SwingConstants.LEFT);
            label.setAlignmentX(Component.LEFT_ALIGNMENT);
            label.setAlignmentY(Component.CENTER_ALIGNMENT);
            return label;
        }

        public JLabel getSecondarySmallLabel(String text) {
            JLabel label = new JLabel(text);
            label.setBackground(null);
            label.setForeground(this.style.getButton1ForegroundColor());
            label.setFont(this.style.getSmallTextFont());
            label.setHorizontalAlignment(SwingConstants.LEFT);
            label.setAlignmentX(Component.LEFT_ALIGNMENT);
            label.setAlignmentY(Component.CENTER_ALIGNMENT);
            return label;
        }

        public JButton getPrimaryButton(String text) {
            JButton button = new JButton(text);
            button.setForeground(style.getButton1BackgroundColor());
            button.setBackground(style.getButton1ForegroundColor());
            button.setFont(style.getButtonFont());
            button.setAlignmentX(Component.LEFT_ALIGNMENT);
            button.setAlignmentY(Component.CENTER_ALIGNMENT);
            return button;
        }

        public JButton getSecondaryButton(String text) {
            JButton button = new JButton(text);
            button.setForeground(style.getButton2BackgroundColor());
            button.setBackground(style.getButton2ForegroundColor());
            button.setFont(style.getButtonFont());
            button.setAlignmentX(Component.LEFT_ALIGNMENT);
            button.setAlignmentY(Component.CENTER_ALIGNMENT);
            return button;
        }
    }
}

