package views.gui.components.panels;

import controllers.PersonController;
import controllers.TicketCategoryController;
import controllers.TicketController;
import database.Database;
import database.Property;
import models.Person;
import views.gui.styles.Style;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.Optional;

import static java.lang.Math.round;

public class PersonPanel extends JPanel implements ListSelectionListener, PropertyChangeListener {
    JPanel layoutPanel;
    Style style;
    Database<Person> personDatabase;
    PersonController personController;
    TicketController ticketController;
    TicketCategoryController categoryController;
    DefaultListModel<Person> listModel;
    JList<Person> personJList;
    JPanel rightPanel;
    CardLayout rightPanelLayout;
    ComponentFactory componentFactory;

    int horizontalOffset = 10;

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public PersonPanel(JPanel layoutPanel, Style style, Database<Person> personDatabase, PersonController personController, TicketController ticketController, TicketCategoryController categoryController) {
        this.layoutPanel = layoutPanel;
        this.style = style;
        this.personDatabase = personDatabase;
        this.personController = personController;
        this.ticketController = ticketController;
        this.categoryController = categoryController;
        this.listModel = new DefaultListModel<>();
        this.rightPanel = new JPanel();
        this.rightPanelLayout = new CardLayout();
        this.componentFactory = new ComponentFactory(this.style);

        this.setLayout(new BorderLayout());

        this.add(createLeftPanel(), BorderLayout.LINE_START);

        this.rightPanel.setLayout(rightPanelLayout);
        rightPanel.add(createEmptyRightPanel(), "EmptyPanel");
        this.add(this.rightPanel, BorderLayout.CENTER);
    }

    // ========================================================================================== //
    // Left panel + components
    // ========================================================================================== //

    Box createLeftPanel() {
        Box leftPanel = Box.createVerticalBox();
        leftPanel.setPreferredSize(new Dimension(screenSize.width / 4, screenSize.height));
        leftPanel.setOpaque(true);
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
        Box buttonBox = Box.createHorizontalBox();
        buttonBox.setOpaque(true);
        buttonBox.setBackground(style.getTransparantColor());
        buttonBox.setMaximumSize(new Dimension(screenSize.width / 4, 50));

        buttonBox.add(Box.createHorizontalGlue());

        JButton createTicketButton = componentFactory.getSecondaryButton("+ Add");
        createTicketButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        createTicketButton.addActionListener(e -> addUser());
        buttonBox.add(createTicketButton);

        buttonBox.add(Box.createHorizontalGlue());

        JButton deleteTicketButton = componentFactory.getSecondaryButton("- Delete");
        deleteTicketButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        deleteTicketButton.addActionListener(e -> deleteUser(this.personJList.getSelectedValue()));
        buttonBox.add(deleteTicketButton);

        buttonBox.add(Box.createHorizontalGlue());

        leftPanel.add(buttonBox);

        // Add buffer
        leftPanel.add(Box.createVerticalStrut(screenSize.height / 20));

        // Add list
        personJList = new JList<>(listModel);

        // Added a custom ListCellRenderer, so it holds a Person but show only it's name
        personJList.setCellRenderer(new PersonListCellRenderer());
        personJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        personJList.setFixedCellHeight(screenSize.height / 20);
        personJList.setFixedCellWidth(screenSize.width / 5);
        personJList.setFont(style.getListFont());
        personJList.setForeground(style.getListForegroundColor());
        personJList.setBackground(style.getListBackgroundColor());
        personJList.addListSelectionListener(this);

        JScrollPane ticketScrollPane = new JScrollPane(personJList);
        ticketScrollPane.setBackground(style.getTransparantColor());
        ticketScrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        Box scrollPaneBox = Box.createVerticalBox();
        scrollPaneBox.setOpaque(true);
        scrollPaneBox.setBackground(style.getTransparantColor());
        scrollPaneBox.add(ticketScrollPane);
        scrollPaneBox.setMaximumSize(new Dimension(screenSize.width / 4, screenSize.height / 2));
        scrollPaneBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(scrollPaneBox);

        leftPanel.add(Box.createVerticalStrut(screenSize.height / 20));

        JButton calculateButton = componentFactory.getSecondaryButton("Calculate");
        calculateButton.setMaximumSize(new Dimension(screenSize.width / 4, 100));
        calculateButton.setPreferredSize(new Dimension(screenSize.width / 4, 100));
        calculateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        calculateButton.addActionListener(e -> {
            ticketController.calculateAll();
        });

        leftPanel.add(calculateButton);
        return leftPanel;
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
        topContainer.setPreferredSize(new Dimension(3 * screenSize.width / 4, 100));
        topContainer.setBackground(style.getTransparantColor());
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.X_AXIS));
        topContainer.add(Box.createHorizontalStrut(horizontalOffset));
        topContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        topContainer.setAlignmentY(Component.BOTTOM_ALIGNMENT);

        // Name of the person in view
        JLabel personName = componentFactory.getSubtitleLabel(name);
        personName.setMaximumSize(new Dimension(screenSize.width / 2, 100));
        personName.setAlignmentY(Component.CENTER_ALIGNMENT);
        topContainer.add(personName);

        // Button to homepage
        JButton homepageButton = componentFactory.getPrimaryButton("Homepage");
        homepageButton.addActionListener(e -> {
            CardLayout layout = (CardLayout) layoutPanel.getLayout();
            layout.show(layoutPanel, "HomePanel");
        });
        homepageButton.setAlignmentY(Component.CENTER_ALIGNMENT);
        topContainer.add(homepageButton);

        topContainer.add(Box.createHorizontalStrut(horizontalOffset));

        // Button to ticketpage
        JButton ticketpageButton = componentFactory.getPrimaryButton("Ticketpage");
        ticketpageButton.addActionListener(e -> {
            CardLayout layout = (CardLayout) layoutPanel.getLayout();
            layout.show(layoutPanel, "TicketPanel");
        });
        ticketpageButton.setAlignmentY(Component.CENTER_ALIGNMENT);
        topContainer.add(ticketpageButton);
        return topContainer;
    }

    // ========================================================================================== //
    // Right panel + components
    // ========================================================================================== //

    Box createEmptyRightPanel() {
        Box rightPanel = Box.createVerticalBox();
        rightPanel.setOpaque(true);
        rightPanel.setBackground(style.getBackgroundSecondaryColor());

        // Add top with name and buttons
        JPanel topContainer = createTopContainer("");
        rightPanel.add(topContainer);

        return rightPanel;
    }

    /**
     * Method that creates the whole right part of the lay-out. It holds
     * - The chosen person name and id
     * - The changeName button to change the chosen persons name
     * - The debtHolders and pay buttons to repay the debt
     *
     * <p>
     * The values given depends on the selected person in the PersonJList on the left panel
     * </p>
     *
     * @param person the person whose values will be displayed
     * @return the rightPanel
     */
    Box createRightPanel(Person person) {
        // A VerticalBox is just a JPanel with BoxLayout in the Y-axis
        Box rightPanel = Box.createVerticalBox();
        rightPanel.setOpaque(true);
        rightPanel.setBackground(style.getBackgroundSecondaryColor());
        rightPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.setAlignmentY(Component.CENTER_ALIGNMENT);

        // Add top with name and buttons
        JPanel topContainer = createTopContainer(person.getName());
        rightPanel.add(topContainer);

        // Add username with button to change name
        Box usernameContainer = createUsernameContainer(person);
        rightPanel.add(usernameContainer);

        // Add Id
        Box userIdContainer = createUserIdContainer(person.getId());
        rightPanel.add(userIdContainer);

        // Add debts
        Box userDebtContainer = createUserDebtContainer(person);
        rightPanel.add(userDebtContainer);

        return rightPanel;
    }

    private Box createUsernameContainer(Person person) {
        Box usernameContainer = Box.createHorizontalBox();
        usernameContainer.setBackground(style.getTransparantColor());
        usernameContainer.setMaximumSize(new Dimension(3 * screenSize.width / 4, 75));
        usernameContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        usernameContainer.setAlignmentY(Component.CENTER_ALIGNMENT);

        usernameContainer.add(Box.createHorizontalStrut(horizontalOffset));

        JLabel usernameLabel = componentFactory.getSecondaryNormalLabel("Username :");
        usernameLabel.setMaximumSize(new Dimension(screenSize.width / 7, 75));

        usernameContainer.add(usernameLabel);

        JLabel personInViewLabel = componentFactory.getSecondaryNormalLabel(person.getName());
        personInViewLabel.setMaximumSize(new Dimension(screenSize.width / 6, 75));

        usernameContainer.add(personInViewLabel);

        JButton changeNameButton = componentFactory.getPrimaryButton("change name");
        changeNameButton.addActionListener(e -> changeUserName(person));
        changeNameButton.setAlignmentY(Component.CENTER_ALIGNMENT);
        usernameContainer.add(changeNameButton);
        return usernameContainer;
    }

    private Box createUserIdContainer(Long id) {
        Box userIdContainer = Box.createHorizontalBox();
        userIdContainer.setOpaque(true);
        userIdContainer.setBackground(style.getTransparantColor());
        userIdContainer.setMaximumSize(new Dimension(3 * screenSize.width / 4, 75));
        userIdContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        userIdContainer.setAlignmentY(Component.CENTER_ALIGNMENT);

        userIdContainer.add(Box.createHorizontalStrut(horizontalOffset));

        JLabel userIdLabel = componentFactory.getSecondaryNormalLabel("Id :");
        userIdLabel.setMaximumSize(new Dimension(screenSize.width / 7, 75));
        userIdContainer.add(userIdLabel);

        JLabel personInViewLabel = componentFactory.getSecondaryNormalLabel(String.valueOf(String.valueOf(id)));
        personInViewLabel.setMaximumSize(new Dimension(screenSize.width / 3, 100));
        userIdContainer.add(personInViewLabel);

        return userIdContainer;
    }

    private Box createUserDebtContainer(Person person) {
        Box userDebtContainer = Box.createVerticalBox();
        userDebtContainer.setBackground(style.getTransparantColor());
        userDebtContainer.setMaximumSize(new Dimension(3 * screenSize.width / 4, 1000));
        userDebtContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        userDebtContainer.setAlignmentY(Component.CENTER_ALIGNMENT);

        Box debtLabelContainer = Box.createHorizontalBox();
        debtLabelContainer.setMaximumSize(new Dimension(2 * screenSize.width, 75));
        debtLabelContainer.add(Box.createHorizontalStrut(horizontalOffset));
        debtLabelContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        debtLabelContainer.setAlignmentY(Component.CENTER_ALIGNMENT);

        JLabel userDebtLabel = componentFactory.getSecondaryNormalLabel("Debts :");
        userDebtLabel.setMaximumSize(new Dimension(screenSize.width / 7, 75));
        debtLabelContainer.add(userDebtLabel);

        userDebtContainer.add(debtLabelContainer);
        // Column 1 with subtitle
        Map<Long, Double> debts = person.getDebts();

        for (Long key : debts.keySet()) {
            Optional<Person> optDebtHolder = personDatabase.getById(key);
            if (optDebtHolder.isEmpty()) continue;

            if (debts.get(key) != 0) {
                Box row = componentFactory.getSmallRow(optDebtHolder.get(), person, debts.get(key));
                userDebtContainer.add(row);
            }
        }
        return userDebtContainer;
    }

    // ========================================================================================== //
    // Button actions
    // ========================================================================================== //

    private void changeUserName(Person person) {
        String name = JOptionPane.showInputDialog(null, "Enter your new name");
        if (name == null) {
            return;
        }
        personController.rename(person.getId(), name);

        JOptionPane.showMessageDialog(null, "Successfully changed name: %s".formatted(person.getName()));
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

    private void deleteUser(Person person) {
        if (person != null) {
            this.personController.delete(person.getId());
        }
    }

    private void payTo(Person receiver, Person payer, Double amount) {
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        NumberFormatter formatter = new NumberFormatter(decimalFormat);
        formatter.setValueClass(Double.class);
        formatter.setAllowsInvalid(false); // Only accept valid numbers

        // Create the JFormattedTextField and set the formatter
        JFormattedTextField decimalTextField = new JFormattedTextField(formatter);
        decimalTextField.setColumns(10);

        Object[] message = {"Give payed amount: ", decimalTextField};

        decimalTextField.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                JFormattedTextField textField = (JFormattedTextField) input;
                return textField.isEditValid(); // Return true only if the input is valid
            }
        });

        while (decimalTextField.getValue() == null) {
            JOptionPane.showConfirmDialog(null, message, "Payed amount", JOptionPane.DEFAULT_OPTION);
        }

        double amountPayed = (double) decimalTextField.getValue();


        personController.pay(receiver.getId(), payer.getId(), amountPayed);
        ticketController.calculateAll();
    }


    // ========================================================================================== //
    // Value/Property changes + reactions
    // ========================================================================================== //

    /**
     * This function will run every time a different value is selected in the JList
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
            this.rightPanel.revalidate();
            this.rightPanel.repaint();
        } else if (evt.getPropertyName().equals(Property.UPDATE.name)) {
            updatePersonList();
            this.rightPanel.revalidate();
            this.rightPanel.repaint();
        } else if (evt.getPropertyName().equals(Property.DELETE.name)) {
            updatePersonList();
            this.rightPanel.revalidate();
            this.rightPanel.repaint();
        }
    }

    public void updatePersonList() {
        listModel.clear();
        for (Person person : personDatabase.getAll()) {
            listModel.addElement(person);
        }
        personJList.setModel(listModel);
    }

    // ========================================================================================== //
    // Extra lay-out objects
    // ========================================================================================== //

    // Private object so that we can pass a Person in to the list, but only show its name and id
    private static class PersonListCellRenderer extends DefaultListCellRenderer {
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

    private class ComponentFactory {
        Style style;

        public ComponentFactory(Style style) {
            this.style = style;
        }

        public Box getSmallRow(Person debtHolderPerson, Person mainPerson, double amount) {
            Box box = Box.createHorizontalBox();
            box.setOpaque(true);
            box.setBackground(this.style.getTransparantColor());
            box.setMaximumSize(new Dimension(screenSize.width / 2, 50));
            box.setAlignmentX(Component.LEFT_ALIGNMENT);
            box.setAlignmentY(Component.CENTER_ALIGNMENT);
            box.add(Box.createHorizontalStrut(3 * horizontalOffset));

            JLabel personLabel = getSecondarySmallLabel(debtHolderPerson.getName() + ":");
            personLabel.setMaximumSize(new Dimension(300, 50));

            double amount_rounded = round(amount*100)/100.;
            JLabel debtLabel = getSecondarySmallLabel("€ " + amount_rounded);
            debtLabel.setMaximumSize(new Dimension(200, 50));

            box.add(personLabel);
            box.add(debtLabel);

            if (amount < 0.) {
                debtLabel.setForeground(new Color(0, 153, 51));
            } else {
                debtLabel.setForeground(Color.red);

                JButton payButton = getPrimaryButton("Pay");
                payButton.setMaximumSize(new Dimension(100, 50));
                payButton.addActionListener(e -> payTo(debtHolderPerson, mainPerson, amount));
                box.add(payButton);
            }
            return box;
        }

        public JLabel getPrimaryNormalLabel(String text) {
            JLabel label = new JLabel(text);
            label.setForeground(this.style.getLabel2ForegroundColor());
            label.setBackground(this.style.getTransparantColor());
            label.setFont(this.style.getTextFont());
            label.setHorizontalAlignment(SwingConstants.LEFT);
            label.setAlignmentX(Component.LEFT_ALIGNMENT);
            label.setAlignmentY(Component.CENTER_ALIGNMENT);
            return label;
        }

        public JLabel getSecondaryNormalLabel(String text) {
            JLabel label = new JLabel(text);
            label.setForeground(this.style.getLabel1ForegroundColor());
            label.setBackground(this.style.getTransparantColor());
            label.setFont(this.style.getTextFont());
            label.setHorizontalAlignment(SwingConstants.LEFT);
            label.setAlignmentX(Component.LEFT_ALIGNMENT);
            label.setAlignmentY(Component.CENTER_ALIGNMENT);
            return label;
        }

        public JLabel getPrimarySmallLabel(String text) {
            JLabel label = new JLabel(text);
            label.setForeground(this.style.getLabel2ForegroundColor());
            label.setBackground(this.style.getTransparantColor());
            label.setFont(this.style.getSmallTextFont());
            label.setHorizontalAlignment(SwingConstants.LEFT);
            label.setAlignmentX(Component.LEFT_ALIGNMENT);
            label.setAlignmentY(Component.CENTER_ALIGNMENT);
            return label;
        }

        public JLabel getSubtitleLabel(String text) {
            JLabel label = new JLabel(text);
            label.setForeground(this.style.getLabel1ForegroundColor());
            label.setBackground(this.style.getTransparantColor());
            label.setFont(this.style.getBoldSubtitleFont());
            label.setHorizontalAlignment(SwingConstants.LEFT);
            label.setAlignmentX(Component.LEFT_ALIGNMENT);
            label.setAlignmentY(Component.CENTER_ALIGNMENT);
            return label;
        }

        public JLabel getSecondarySmallLabel(String text) {
            JLabel label = new JLabel(text);
            label.setForeground(this.style.getLabel1ForegroundColor());
            label.setBackground(this.style.getTransparantColor());
            label.setFont(this.style.getSmallTextFont());
            label.setHorizontalAlignment(SwingConstants.LEFT);
            label.setAlignmentX(Component.LEFT_ALIGNMENT);
            label.setAlignmentY(Component.CENTER_ALIGNMENT);
            return label;
        }

        public JButton getPrimaryButton(String text) {
            JButton button = new JButton(text);
            button.setBackground(style.getButton1ForegroundColor());
            button.setForeground(style.getButton1BackgroundColor());
            button.setFont(style.getButtonFont());
            button.setAlignmentX(Component.LEFT_ALIGNMENT);
            button.setAlignmentY(Component.CENTER_ALIGNMENT);
            return button;
        }

        public JButton getSecondaryButton(String text) {
            JButton button = new JButton(text);
            button.setBackground(style.getButton2ForegroundColor());
            button.setForeground(style.getButton2BackgroundColor());
            button.setFont(style.getButtonFont());
            button.setAlignmentX(Component.LEFT_ALIGNMENT);
            button.setAlignmentY(Component.CENTER_ALIGNMENT);
            return button;
        }
    }
}

