package views.gui.components.panels;

import controllers.TicketController;
import database.Database;
import database.Property;
import models.Person;
import models.Ticket;
import models.TicketCategory;
import views.gui.styles.Style;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TicketPanel extends JPanel implements ListSelectionListener, PropertyChangeListener {
    JPanel layoutPanel;
    Style style;

    Database<Ticket> ticketDatabase;
    Database<TicketCategory> categoryDatabase;
    Database<Person> personDatabase;

    TicketController ticketController;


    DefaultListModel<Ticket> listModel;
    JList<Ticket> ticketJList;
    JPanel rightPanel;
    CardLayout rightPanelLayout;
    ComponentFactory componentFactory;

    int horizontalOffset = 10;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private Ticket selectedTicket;

    public TicketPanel(JPanel layoutPanel, Style style, Database<Ticket> ticketDatabase, Database<TicketCategory> categoryDatabase, Database<Person> personDatabase, TicketController ticketController) {
        this.layoutPanel = layoutPanel;
        this.style = style;
        this.ticketDatabase = ticketDatabase;
        this.categoryDatabase = categoryDatabase;
        this.personDatabase = personDatabase;
        this.ticketController = ticketController;
        this.listModel = new DefaultListModel<>();
        this.rightPanel = new JPanel();
        this.rightPanelLayout = new CardLayout();
        this.componentFactory = new ComponentFactory(this.style);
        this.selectedTicket = null;

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
        JLabel userLabel = new JLabel("Tickets") {
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

        // Add Ticket button (Box2)
        JButton createTicketButton = new JButton("+ Add Ticket");
        createTicketButton.setForeground(style.getButton1ForegroundColor());
        createTicketButton.setBackground(style.getButton1BackgroundColor());
        createTicketButton.setFont(style.getButtonFont());
        createTicketButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        createTicketButton.addActionListener(e -> addTicket());
        leftPanel.add(createTicketButton);

        // Add buffer
        leftPanel.add(Box.createVerticalStrut(screenSize.height / 20));

        // Add list
        ticketJList = new JList<>(listModel);

        // Added a custom ListCellRenderer, so it holds a Ticket but show only it's name
        ticketJList.setCellRenderer(new TicketListCellRenderer());
        ticketJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ticketJList.setFixedCellHeight(screenSize.height / 20);
        ticketJList.setFixedCellWidth(screenSize.width / 5);
        ticketJList.setFont(style.getListFont());
        ticketJList.setForeground(style.getListForegroundColor());
        ticketJList.setBackground(style.getListBackgroundColor());
        ticketJList.addListSelectionListener(this);
        leftPanel.add(ticketJList);
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

        // Name of the ticket in view
        JLabel ticketName = componentFactory.getSubtitleLabel(name);
        ticketName.setMaximumSize(new Dimension(screenSize.width / 2, 100));
        ticketName.setAlignmentY(Component.CENTER_ALIGNMENT);
        topContainer.add(ticketName);

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
     * - The chosen ticket name and id
     * - The changeName button to change the chosen tickets name
     * - The ditrsibutionHolders and pay buttons to repay the ditrsibution
     *
     * <p>
     * The values given depends on the selected ticket in the TicketJList on the left panel
     * </p>
     *
     * @param ticket the ticket whose values will be displayed
     * @return the rightPanel
     */
    Box createRightPanel(Ticket ticket) {
        // A VerticalBox is just a JPanel with BoxLayout in the Y-axis
        Box rightPanel = Box.createVerticalBox();
        rightPanel.setOpaque(true);
        rightPanel.setBackground(style.getBackgroundSecondaryColor());
        rightPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.setAlignmentY(Component.CENTER_ALIGNMENT);

        // Add top with name and buttons
        JPanel topContainer = createTopContainer(String.valueOf(ticket.getId()));
        rightPanel.add(topContainer);

        // Add username with button to change name
        Box usernameContainer = createCategoryContainer(ticket);
        rightPanel.add(usernameContainer);

        // Add Id
        Box userIdContainer = createPayerIdContainer(ticket);
        rightPanel.add(userIdContainer);

        // Add distributions
        rightPanel.add(createDistributionContainer(ticket));

        return rightPanel;
    }

    private Box createCategoryContainer(Ticket ticket) {
        Box usernameContainer = Box.createHorizontalBox();
        usernameContainer.setBackground(style.getTransparantColor());
        usernameContainer.setMaximumSize(new Dimension(3 * screenSize.width / 4, 75));
        usernameContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        usernameContainer.setAlignmentY(Component.CENTER_ALIGNMENT);

        usernameContainer.add(Box.createHorizontalStrut(horizontalOffset));

        JLabel usernameLabel = componentFactory.getSecondaryNormalLabel("Category :");
        usernameLabel.setMaximumSize(new Dimension(screenSize.width / 7, 75));

        usernameContainer.add(usernameLabel);

        JLabel ticketInViewLabel = componentFactory.getSecondaryNormalLabel(String.valueOf(categoryDatabase.getById(ticket.getTicketCategoryId()).get().getName()));
        ticketInViewLabel.setMaximumSize(new Dimension(screenSize.width / 7, 75));

        usernameContainer.add(ticketInViewLabel);

        JButton changeNameButton = componentFactory.getPrimaryButton("change category");
        changeNameButton.addActionListener(e -> changeTicketCategory(ticket));
        changeNameButton.setAlignmentY(Component.CENTER_ALIGNMENT);
        usernameContainer.add(changeNameButton);
        return usernameContainer;
    }

    private Box createPayerIdContainer(Ticket ticket) {
        Box userIdContainer = Box.createHorizontalBox();
        userIdContainer.setOpaque(true);
        userIdContainer.setBackground(style.getTransparantColor());
        userIdContainer.setMaximumSize(new Dimension(3 * screenSize.width / 4, 75));
        userIdContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        userIdContainer.setAlignmentY(Component.CENTER_ALIGNMENT);

        userIdContainer.add(Box.createHorizontalStrut(horizontalOffset));

        JLabel userIdLabel = componentFactory.getSecondaryNormalLabel("Payer :");
        userIdLabel.setMaximumSize(new Dimension(screenSize.width / 7, 75));
        userIdContainer.add(userIdLabel);

        JLabel ticketInViewLabel;
        if (ticket.getPayerId() != null) {
            ticketInViewLabel = componentFactory.getSecondaryNormalLabel(personDatabase.getById(ticket.getPayerId()).get().getName());
        } else {
            ticketInViewLabel = componentFactory.getSecondaryNormalLabel("");
        }
        ticketInViewLabel.setMaximumSize(new Dimension(screenSize.width / 7, 100));
        userIdContainer.add(ticketInViewLabel);

        JButton changeNameButton = componentFactory.getPrimaryButton("change payer");
        changeNameButton.addActionListener(e -> changePayer(ticket));
        changeNameButton.setAlignmentY(Component.CENTER_ALIGNMENT);
        userIdContainer.add(changeNameButton);

        return userIdContainer;
    }

    private Box createDistributionContainer(Ticket ticket) {
        Box distributionContainer = Box.createVerticalBox();
        distributionContainer.setOpaque(true);
        distributionContainer.setBackground(style.getTransparantColor());
        distributionContainer.setMaximumSize(new Dimension(3 * screenSize.width / 4, 1000));
        distributionContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        distributionContainer.setAlignmentY(Component.CENTER_ALIGNMENT);

        Box distributionLabelContainer = Box.createHorizontalBox();
        distributionLabelContainer.setMaximumSize(new Dimension(3 * screenSize.width / 4, 75));
        distributionLabelContainer.add(Box.createHorizontalStrut(horizontalOffset));
        distributionLabelContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        distributionLabelContainer.setAlignmentY(Component.CENTER_ALIGNMENT);

        JLabel distributionLabel = componentFactory.getSecondaryNormalLabel("Distribution :");
        distributionLabel.setMaximumSize(new Dimension(2 * screenSize.width / 7, 75));
        distributionLabelContainer.add(distributionLabel);

        JButton changeDistributionButton = componentFactory.getPrimaryButton("change distribution");
        changeDistributionButton.setMaximumSize(new Dimension(screenSize.width / 5, 50));
        changeDistributionButton.addActionListener(e -> updateDistribution(ticket));
        changeDistributionButton.setAlignmentY(Component.CENTER_ALIGNMENT);
        distributionLabelContainer.add(changeDistributionButton);

        distributionContainer.add(distributionLabelContainer);
        // Column 1 with subtitle
        Map<Long, Double> distribution = ticket.getDistribution();

        for (Long key : distribution.keySet()) {
            Optional<Person> optDistributionHolder = personDatabase.getById(key);
            if (optDistributionHolder.isEmpty()) continue;
            Box row = componentFactory.getSmallRow(optDistributionHolder.get(), distribution.get(key));
            distributionContainer.add(row);
        }
        return distributionContainer;
    }


    // ========================================================================================== //
    // Button actions
    // ========================================================================================== //
    // TODO: change this method
    private void addTicket() {
        // Get payerId
        List<Person> persons = this.personDatabase.getAll();
        List<String> personsStringList = persons.stream().map(Person::getName).toList();
        Object[] personsStringArray = personsStringList.toArray();

        Object payerPerson = JOptionPane.showInputDialog(null, "Choose your payer", "Choose payer", JOptionPane.PLAIN_MESSAGE, null, personsStringArray, null);

        int payerIndex = personsStringList.indexOf(payerPerson);
        Long payerId = persons.get(payerIndex).getId();

        // Get amount
        // Create a NumberFormatter with a decimal format pattern
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        NumberFormatter formatter = new NumberFormatter(decimalFormat);
        formatter.setValueClass(Double.class);
        formatter.setAllowsInvalid(false); // Only accept valid numbers

        // Create the JFormattedTextField and set the formatter
        JFormattedTextField decimalTextField = new JFormattedTextField(formatter);
        decimalTextField.setColumns(10);

        Object[] message = {"Give payed amount: ", decimalTextField};

        // Add an InputVerifier to validate the entered value
        decimalTextField.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                JFormattedTextField textField = (JFormattedTextField) input;
                return textField.isEditValid(); // Return true only if the input is valid
            }
        });

        JOptionPane.showConfirmDialog(null, message, "Payed amount", JOptionPane.DEFAULT_OPTION);

        double amountPayed = (double) decimalTextField.getValue();

        // Get category
        List<TicketCategory> categories = this.categoryDatabase.getAll();
        List<String> categoriesStringList = categories.stream().map(TicketCategory::getName).toList();
        Object[] categoriesStringArray = categoriesStringList.toArray();

        Object category = JOptionPane.showInputDialog(null, "Choose your category", "Choose category", JOptionPane.PLAIN_MESSAGE, null, categoriesStringArray, categoriesStringArray[0]);

        int index = categoriesStringList.indexOf(category);
        Long categoryId = categories.get(index).getId();


        // Get personsInvolved
        JList<Object> list = new JList<>(personsStringArray);
        JScrollPane jscrollpane = new JScrollPane();
        jscrollpane.setViewportView(list);

        Object[] message2 = {"Select persons involved:", jscrollpane};

        JOptionPane.showConfirmDialog(null, message2, "Login", JOptionPane.DEFAULT_OPTION);

        int[] indexes = list.getSelectedIndices();
        List<Long> personsInvolved = Arrays.stream(indexes).mapToObj(i -> persons.get(i).getId()).toList();

        Optional<Ticket> optionalTicket = ticketController.create(categoryId, amountPayed, personsInvolved);

        if (optionalTicket.isEmpty()) {
            JOptionPane.showMessageDialog(null, "An Error occurred creating the ticket");
            return;
        }

        ticketController.setPayer(optionalTicket.get().getId(), payerId);

        JOptionPane.showMessageDialog(null, "Successfully created ticket: %s".formatted(optionalTicket.get().getId()));
    }

    private void changeTicketCategory(Ticket ticket) {
        List<TicketCategory> options = this.categoryDatabase.getAll();
        List<String> optionsStringList = options.stream().map(TicketCategory::getName).toList();
        Object[] optionsStringArray = optionsStringList.toArray();


        Object category = JOptionPane.showInputDialog(null, "Choose your category", "Choose category", JOptionPane.PLAIN_MESSAGE, null, optionsStringArray, optionsStringArray[0]);

        int index = optionsStringList.indexOf(category);
        Long categoryId = options.get(index).getId();

        ticketController.changeCategory(ticket.getId(), categoryId);

        JOptionPane.showMessageDialog(null, "Successfully changed category: %s".formatted(options.get(index).getName()));
    }

    private void changePayer(Ticket ticket) {
        List<Person> options = this.personDatabase.getAll();
        List<String> optionsStringList = options.stream().map(Person::getName).toList();
        Object[] optionsStringArray = optionsStringList.toArray();

        Object person = JOptionPane.showInputDialog(null, "Choose your payer", "Choose payer", JOptionPane.PLAIN_MESSAGE, null, optionsStringArray, optionsStringArray[0]);

        int index = optionsStringList.indexOf(person);
        Long personId = options.get(index).getId();

        ticketController.setPayer(ticket.getId(), personId);

        JOptionPane.showMessageDialog(null, "Successfully changed category to %s".formatted(options.get(index).getName()));
    }

    private void updateDistribution(Ticket ticket) {

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
        this.selectedTicket = this.ticketJList.getSelectedValue();
        updateRightPanel();
    }

    /**
     * This function will update this object whenever a change happens in the TicketDatabase
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(Property.UPDATE.name)) {
            updateTicketList();
            updateRightPanel();
        } else if (evt.getPropertyName().equals(Property.CREATE.name)) {
            updateTicketList();
            updateRightPanel();
        } else if (evt.getPropertyName().equals(Property.DELETE.name)) {
            updateTicketList();
            updateRightPanel();
        }
    }

    public void updateTicketList() {
        listModel.clear();
        for (Ticket ticket : ticketDatabase.getAll()) {
            listModel.addElement(ticket);
        }
        ticketJList.setModel(listModel);
    }

    public void updateRightPanel() {
        if (this.selectedTicket == null) {
            this.rightPanelLayout.show(this.rightPanel, "EmptyPanel");
        } else {
            this.rightPanel.add(createRightPanel(this.selectedTicket), "rightPanel");
            this.rightPanelLayout.show(this.rightPanel, "rightPanel");
        }
    }

    // ========================================================================================== //
    // Extra lay-out objects
    // ========================================================================================== //

    // Private object so that we can pass a Ticket in to the list, but only show its name and id
    private static class TicketListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            // Call the super method to get the default cell renderer component
            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            // If the value is an instance of Ticket, display the desired property
            if (value instanceof Ticket ticket) {
                setText("#" + ticket.getId()); // Assuming getName() is the desired property
            }
            return c;
        }
    }

    private class ComponentFactory {
        Style style;

        public ComponentFactory(Style style) {
            this.style = style;
        }

        public Box getSmallRow(Person distributionHolder, double amount) {
            Box box = Box.createHorizontalBox();
            box.setOpaque(true);
            box.setBackground(this.style.getTransparantColor());
            box.setMaximumSize(new Dimension(screenSize.width / 2, 50));
            box.setAlignmentX(Component.LEFT_ALIGNMENT);
            box.setAlignmentY(Component.CENTER_ALIGNMENT);
            box.add(Box.createHorizontalStrut(3 * horizontalOffset));

            JLabel ticketLabel = getSecondarySmallLabel(distributionHolder.getName() + ":");
            ticketLabel.setMaximumSize(new Dimension(300, 50));

            JLabel ditrsibutionLabel = getSecondarySmallLabel("€ " + amount);
            ditrsibutionLabel.setMaximumSize(new Dimension(200, 50));

            box.add(ticketLabel);
            box.add(ditrsibutionLabel);

            if (amount >= 0.) {
                ditrsibutionLabel.setForeground(new Color(0, 153, 51));
            } else {
                ditrsibutionLabel.setForeground(Color.red);
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
