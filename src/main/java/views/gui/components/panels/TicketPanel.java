package views.gui.components.panels;

import controllers.PersonController;
import controllers.TicketCategoryController;
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
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;
import java.util.Optional;

/**
 * Will hold all the information and lay-out of the ticket panel.
 * <p>
 * This object holds everything about the lay-out. The style can be changed by choosing another {@link Style}.
 * </p>
 *
 * <p>
 * This object will also act as:
 * - {@link ListSelectionListener} to update itself whenever an item from the list is picked
 * - {@link PropertyChangeListener} to update itself whenever a change happens in the {@link Database<Ticket>}
 * </p>
 */
public class TicketPanel extends JPanel implements ListSelectionListener, PropertyChangeListener {
    // The style object that determines the colors and fonts used in the lay-out
    Style style;

    // The different panels that need to be updated across the whole object
    JPanel layoutPanel;
    JPanel rightPanel;
    CardLayout rightPanelLayout;

    // The objects to modify the tickets and also to update the list with ticket names
    Database<Ticket> ticketDatabase;
    TicketController ticketController;
    Database<TicketCategory> ticketCategoryDatabase;
    TicketCategoryController ticketCategoryController;
    Database<Person> personDatabase;
    PersonController personController;

    // Objects to read and modify the left panel list with ticket names
    DefaultListModel<Ticket> listModel;
    JList<Ticket> ticketJList;

    // A private object that makes specific labels and buttons for this specific object
    ComponentFactory componentFactory;

    // Variables for lay-out
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int horizontalOffset = 10;

    public TicketPanel(JPanel layoutPanel, Style style, Database<Ticket> ticketDatabase, TicketController ticketController) {
        this.layoutPanel = layoutPanel;
        this.style = style;
        this.ticketDatabase = ticketDatabase;
        this.ticketController = ticketController;
        this.listModel = new DefaultListModel<>();
        this.rightPanel = new JPanel();
        this.rightPanelLayout = new CardLayout();
        this.componentFactory = new ComponentFactory(this.style);

        this.setLayout(new BorderLayout());

        // Add the left panel
        this.add(createLeftPanel(), BorderLayout.LINE_START);

        // Add the right panel. For this we use the CardLayout, so we can switch between an empty panel and one with information on it
        this.rightPanel.setLayout(rightPanelLayout);
        rightPanel.add(createEmptyRightPanel(), "EmptyPanel");
        this.add(this.rightPanel, BorderLayout.CENTER);
    }

    /**
     * Method that creates the whole left part of the screen. It holds:
     * - The title "Users"
     * - The "add user" button which is connected to the ticketDatabase {@link Database<Ticket>}
     * - The list with users in the ticketDatabase that can be modified
     *
     * @return The whole left side of the lay-out in 1 panel
     */
    Box createLeftPanel() {
        // Create the panel and initialize its color/size
        Box leftPanel = Box.createVerticalBox();
        leftPanel.setPreferredSize(new Dimension(screenSize.width / 4, screenSize.height));
        leftPanel.setBackground(style.getBackgroundPrimaryColor());

        // Title of left panel
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

        // Add Ticket button
        JButton createTicketButton = componentFactory.getPrimaryButton("+ add ticket");
        createTicketButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        createTicketButton.addActionListener(e -> {
            addTicket();
        });
        leftPanel.add(createTicketButton);

        // Add buffer
        leftPanel.add(Box.createVerticalStrut(screenSize.height / 20));

        // Add TicketCategory button
        JButton createTicketCategoryButton = componentFactory.getPrimaryButton("+ add ticketCategory");
        createTicketButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        createTicketButton.addActionListener(e -> {
            addTicketCategory();
        });
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

    /**
     * Method that creates the whole right part of the lay-out. It holds
     * - The chosen ticket name and id
     * - The changeName button to change the chosen tickets name
     * - The debtHolders and pay buttons to repay the debt
     *
     * <p>
     * The values given depends on the selected ticket in the TicketJList on the left panel
     * </p>
     *
     * @param ticket the ticket who's values will be displayed
     * @return the rightPanel
     */
    Box createRightPanel(Ticket ticket) {
        // A VerticalBox is just a JPanel with BoxLayout in the Y-axis
        Box rightPanel = Box.createVerticalBox();
        rightPanel.setBackground(style.getBackgroundSecondaryColor());
        rightPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.setAlignmentY(Component.CENTER_ALIGNMENT);

        // Add top with name and buttons
        JPanel topContainer = createTopContainer("#" + ticket.getId());
        rightPanel.add(topContainer);

        // Add username with button to change name
        Box ticketCategoryContainer = createTicketCategoryContainer(ticket);
        rightPanel.add(ticketCategoryContainer);

        // Add Id
        Box payerContainer = createPayerContainer(ticket.getId());
        rightPanel.add(payerContainer);

        // Add debts
        Box userDebtContainer = createUserDebtContainer(ticket);
        rightPanel.add(userDebtContainer);

        return rightPanel;
    }

    /**
     * This methods will read all the tickets in the displayed tickets debts.
     * If his debt is negative (aka he needs to pay them) there will be a button to repay the debt
     *
     * @param ticket The current ticket that is displayed
     * @return a box
     */
    private Box createUserDebtContainer(Ticket ticket) {
        // Create the overal box that will hold the "Debts" label and the different Debts
        Box userDebtContainer = Box.createVerticalBox();
        userDebtContainer.setBackground(style.getBackgroundSecondaryColor());
        userDebtContainer.setMaximumSize(new Dimension(3 * screenSize.width / 4, 1000));
        userDebtContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        userDebtContainer.setAlignmentY(Component.CENTER_ALIGNMENT);

        // Create the "Debts" label container
        Box debtLabelContainer = Box.createHorizontalBox();
        debtLabelContainer.setMaximumSize(new Dimension(2 * screenSize.width, 75));
        debtLabelContainer.add(Box.createHorizontalStrut(horizontalOffset));
        debtLabelContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        debtLabelContainer.setAlignmentY(Component.CENTER_ALIGNMENT);

        // Create the "Debts" label
        JLabel userDebtLabel = componentFactory.getSecondaryNormalLabel("Debts :");
        userDebtLabel.setMaximumSize(new Dimension(screenSize.width / 7, 75));
        debtLabelContainer.add(userDebtLabel);

        userDebtContainer.add(debtLabelContainer);

        // Go over all the distribution of the displayed tickets and show them in different rows
        Map<Long, Double> distribution = ticket.getDistribution();

        for (Long key : distribution.keySet()) {
            Optional<Ticket> optDebtHolder = ticketDatabase.getById(key);
            if (optDebtHolder.isEmpty()) continue;
            Box row = componentFactory.getSmallRow(ticket, optDebtHolder.get(), distribution.get(key));
            userDebtContainer.add(row);
        }
        return userDebtContainer;
    }

    private Box createTicketCategoryContainer(Ticket ticket) {
        Box usernameContainer = Box.createHorizontalBox();
        usernameContainer.setBackground(style.getBackgroundSecondaryColor());
        usernameContainer.setMaximumSize(new Dimension(3 * screenSize.width / 4, 75));
        usernameContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        usernameContainer.setAlignmentY(Component.CENTER_ALIGNMENT);

        usernameContainer.add(Box.createHorizontalStrut(horizontalOffset));

        JLabel usernameLabel = componentFactory.getSecondaryNormalLabel("Category :");
        usernameLabel.setMaximumSize(new Dimension(screenSize.width / 7, 75));

        usernameContainer.add(usernameLabel);

        Optional<TicketCategory> category = ticketCategoryDatabase.getById(ticket.getTicketCategoryId());

        JLabel ticketInViewLabel = componentFactory.getSecondaryNormalLabel(category.get().getName());
        ticketInViewLabel.setMaximumSize(new Dimension(screenSize.width / 6, 75));

        usernameContainer.add(ticketInViewLabel);

        JButton changeNameButton = componentFactory.getPrimaryButton("change category");
        changeNameButton.addActionListener(e -> {
            changeCategory(ticket);
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


    private Box createPayerContainer(Long id) {
        Box userIdContainer = Box.createHorizontalBox();
        userIdContainer.setBackground(style.getBackgroundSecondaryColor());
        userIdContainer.setMaximumSize(new Dimension(3 * screenSize.width / 4, 75));
        userIdContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        userIdContainer.setAlignmentY(Component.CENTER_ALIGNMENT);

        userIdContainer.add(Box.createHorizontalStrut(horizontalOffset));

        Optional<Person> payer = personDatabase.getById(id);

        JLabel userIdLabel = componentFactory.getSecondaryNormalLabel("Payer :");
        userIdLabel.setMaximumSize(new Dimension(screenSize.width / 7, 75));
        userIdContainer.add(userIdLabel);

        JLabel ticketInViewLabel = componentFactory.getSecondaryNormalLabel(payer.get().getName());
        ticketInViewLabel.setMaximumSize(new Dimension(screenSize.width / 6, 100));
        userIdContainer.add(ticketInViewLabel);

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

        // Name of the ticket in view
        JLabel ticketName = componentFactory.getSubtitleLabel(name);
        ticketName.setMaximumSize(new Dimension(screenSize.width / 2, 100));
        ticketName.setAlignmentY(Component.BOTTOM_ALIGNMENT);
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

    public void updateTicketList() {
        listModel.clear();
        for (Ticket ticket : ticketDatabase.getAll()) {
            listModel.addElement(ticket);
        }
        ticketJList.setModel(listModel);
    }

    private void addTicket() {
        System.out.println("Ticket added");
    }

    private void addTicketCategory() {
        System.out.println("TicketCategory added");
    }

    private void changeCategory(Ticket ticket) {
        String name = JOptionPane.showInputDialog(null, "Enter your new name");
        if (name == null) {
            return;
        }
        Long newCategoryId = 0L;
        ticketController.changeCategory(ticket.getId(), newCategoryId);

        JOptionPane.showMessageDialog(null, "Successfully changed name: %s".formatted(ticketCategoryDatabase.getById(ticket.getTicketCategoryId()).get().getName()));
    }

    /**
     * This function will run every time a different value is selected in the Jlist
     *
     * @param e the event that characterizes the change.
     */
    @Override
    public void valueChanged(ListSelectionEvent e) {
        Ticket selectedTicket = this.ticketJList.getSelectedValue();
        if (selectedTicket == null) {
            this.rightPanelLayout.show(this.rightPanel, "EmptyPanel");
        } else {
            this.rightPanel.add(createRightPanel(selectedTicket), "rightPanel");
            this.rightPanelLayout.show(this.rightPanel, "rightPanel");
        }
    }

    /**
     * This function will update this object whenever a change happens in the TicketDatabase
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(Property.CREATE.name)) {
            updateTicketList();
        } else if (evt.getPropertyName().equals(Property.UPDATE.name)) {
            updateTicketList();
        } else if (evt.getPropertyName().equals(Property.DELETE.name)) {
            updateTicketList();
        }
    }

    // Private object so that we can pass a Ticket in to the list, but only show its name and id
    private class TicketListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            // Call the super method to get the default cell renderer component
            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            // If the value is an instance of Ticket, display the desired property
            if (value instanceof Ticket ticket) {
                setText("ID " + String.valueOf(ticket.getId())); // Assuming getName() is the desired property
            }
            return c;
        }
    }

    private class ComponentFactory {
        Style style;

        public ComponentFactory(Style style) {
            this.style = style;
        }

        public Box getSmallRow(Ticket mainTicket, Ticket debtHolderTicket, double amount) {
            Box box = Box.createHorizontalBox();
            box.setMaximumSize(new Dimension(screenSize.width / 2, 50));
            box.setAlignmentX(Component.LEFT_ALIGNMENT);
            box.setAlignmentY(Component.CENTER_ALIGNMENT);
            box.add(Box.createHorizontalStrut(3 * horizontalOffset));

            JLabel ticketLabel = getSecondarySmallLabel( ":");
            ticketLabel.setMaximumSize(new Dimension(300, 50));

            JLabel debtLabel = getSecondarySmallLabel("€ " + amount);
            debtLabel.setMaximumSize(new Dimension(200, 50));

            box.add(ticketLabel);
            box.add(debtLabel);

            if (amount >= 0.) {
                debtLabel.setForeground(new Color(0, 153, 51));
            } else {
                debtLabel.setForeground(Color.red);

                JButton payButton = getPrimaryButton("Pay");
                payButton.setMaximumSize(new Dimension(100, 50));
                payButton.addActionListener(e -> {

                });
                box.add(payButton);
            }
            return box;
        }

        public JLabel getPrimaryNormalLabel(String text) {
            JLabel label = new JLabel(text);
            label.setForeground(this.style.getLabel2ForegroundColor());
            label.setFont(this.style.getTextFont());
            label.setHorizontalAlignment(SwingConstants.LEFT);
            label.setAlignmentX(Component.LEFT_ALIGNMENT);
            label.setAlignmentY(Component.CENTER_ALIGNMENT);
            return label;
        }

        public JLabel getSecondaryNormalLabel(String text) {
            JLabel label = new JLabel(text);
            label.setForeground(this.style.getLabel1ForegroundColor());
            label.setBackground(this.style.getLabel1BackgroundColor());
            label.setFont(this.style.getTextFont());
            label.setHorizontalAlignment(SwingConstants.LEFT);
            label.setAlignmentX(Component.LEFT_ALIGNMENT);
            label.setAlignmentY(Component.CENTER_ALIGNMENT);
            return label;
        }

        public JLabel getPrimarySmallLabel(String text) {
            JLabel label = new JLabel(text);
            label.setForeground(this.style.getLabel2ForegroundColor());
            label.setBackground(this.style.getLabel2BackgroundColor());
            label.setFont(this.style.getSmallTextFont());
            label.setHorizontalAlignment(SwingConstants.LEFT);
            label.setAlignmentX(Component.LEFT_ALIGNMENT);
            label.setAlignmentY(Component.CENTER_ALIGNMENT);
            return label;
        }

        public JLabel getSubtitleLabel(String text) {
            JLabel label = new JLabel(text);
            label.setForeground(this.style.getLabel1ForegroundColor());
            label.setBackground(this.style.getLabel1BackgroundColor());
            label.setFont(this.style.getBoldSubtitleFont());
            label.setHorizontalAlignment(SwingConstants.LEFT);
            label.setAlignmentX(Component.LEFT_ALIGNMENT);
            label.setAlignmentY(Component.CENTER_ALIGNMENT);
            return label;
        }

        public JLabel getSecondarySmallLabel(String text) {
            JLabel label = new JLabel(text);
            label.setForeground(this.style.getLabel1ForegroundColor());
            label.setBackground(this.style.getLabel1BackgroundColor());
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

