package views.gui.components.panels;

import controllers.PersonController;
import database.Database;
import models.Person;
import views.gui.styles.Style;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.Optional;

public class PersonPanel extends JPanel implements ListSelectionListener {
    JPanel layoutPanel;
    Style style;
    Database<Person> personDatabase;
    PersonController personController;
    DefaultListModel<Person> listModel;
    JList<Person> personJList;
    JPanel rightPanel;
    CardLayout rightPanelLayout;

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


        this.setLayout(new BorderLayout());

        this.add(createLeftPanel(), BorderLayout.LINE_START);

        this.rightPanel.setLayout(rightPanelLayout);
        rightPanel.add(createEmptyRightPanel(),"EmptyPanel");
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

    JPanel createRightPanel(Person person) throws NullPointerException {
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(style.getBackgroundSecondaryColor());
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        //personInView = personJList.getSelectedValue();
        // Add top with name and buttons
        JPanel topContainer = createTopContainer(person.getName());
        rightPanel.add(topContainer);

        // Add username with button to change name
        JPanel usernameContainer = createUsernameContainer(person.getName());
        rightPanel.add(usernameContainer);

        // Add Id
        JPanel userIdContainer = createUserIdContainer(person.getId());
        rightPanel.add(userIdContainer);

        return rightPanel;
    }

    JPanel createEmptyRightPanel() {
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(style.getBackgroundSecondaryColor());
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        //personInView = personJList.getSelectedValue();
        // Add top with name and buttons
        JPanel topContainer = createTopContainer("");
        rightPanel.add(topContainer);

        return rightPanel;
    }


    private JPanel createUsernameContainer(String name) {
        JPanel usernameContainer = new JPanel();
        usernameContainer.setLayout(new BoxLayout(usernameContainer, BoxLayout.X_AXIS));
        usernameContainer.setBackground(style.getBackgroundSecondaryColor());
        usernameContainer.setMaximumSize(new Dimension(3 * screenSize.width / 4, 75));

        usernameContainer.add(Box.createHorizontalStrut(horizontalOffset));

        JLabel usernameLabel = new JLabel("Username :");
        usernameLabel.setMaximumSize(new Dimension(screenSize.width / 7, 100));
        usernameLabel.setForeground(style.getButton1ForegroundColor());
        usernameLabel.setFont(style.getTextFont());
        usernameLabel.setHorizontalAlignment(SwingConstants.LEFT);
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        usernameContainer.add(usernameLabel);

        JLabel personInViewLabel = new JLabel(name);
        personInViewLabel.setMaximumSize(new Dimension(screenSize.width / 6, 100));
        personInViewLabel.setForeground(style.getButton1ForegroundColor());
        personInViewLabel.setFont(style.getTextFont());
        personInViewLabel.setHorizontalAlignment(SwingConstants.LEFT);
        personInViewLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        usernameContainer.add(personInViewLabel);

        JButton changeNameButton = new JButton("change name");
        changeNameButton.setForeground(style.getButton1BackgroundColor());
        changeNameButton.setBackground(style.getButton1ForegroundColor());
        changeNameButton.setFont(style.getButtonFont());
        changeNameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        changeNameButton.addActionListener(e -> {
            System.out.println("Change name");
        });
        usernameContainer.add(changeNameButton);
        return usernameContainer;
    }

    private JPanel createUserIdContainer(Long id) {
        JPanel userIdContainer = new JPanel();
        userIdContainer.setLayout(new BoxLayout(userIdContainer, BoxLayout.X_AXIS));
        userIdContainer.setBackground(style.getBackgroundSecondaryColor());
        userIdContainer.setMaximumSize(new Dimension(3 * screenSize.width / 4, 75));

        userIdContainer.add(Box.createHorizontalStrut(horizontalOffset));

        JLabel userIdLabel = new JLabel("Id :");
        userIdLabel.setMaximumSize(new Dimension(screenSize.width / 7, 100));
        userIdLabel.setForeground(style.getButton1ForegroundColor());
        userIdLabel.setFont(style.getTextFont());
        userIdLabel.setHorizontalAlignment(SwingConstants.LEFT);
        userIdLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        userIdContainer.add(userIdLabel);

        JLabel personInViewLabel = new JLabel(String.valueOf(String.valueOf(id)));
        personInViewLabel.setMaximumSize(new Dimension(screenSize.width / 6, 100));
        personInViewLabel.setForeground(style.getButton1ForegroundColor());
        personInViewLabel.setFont(style.getTextFont());
        personInViewLabel.setHorizontalAlignment(SwingConstants.LEFT);
        personInViewLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

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

        // Name of the person in view
        JLabel personName = new JLabel(name);
        personName.setMaximumSize(new Dimension(screenSize.width / 2, 100));
        personName.setForeground(style.getButton1ForegroundColor());
        personName.setFont(style.getBoldSubtitleFont());
        personName.setHorizontalAlignment(SwingConstants.LEFT);
        personName.setAlignmentX(Component.LEFT_ALIGNMENT);
        topContainer.add(personName);

        // Button to homepage
        JButton homepageButton = new JButton("Homepage");
        homepageButton.setForeground(style.getButton1BackgroundColor());
        homepageButton.setBackground(style.getButton1ForegroundColor());
        homepageButton.setFont(style.getButtonFont());
        homepageButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        homepageButton.addActionListener(e -> {
            CardLayout layout = (CardLayout) layoutPanel.getLayout();
            layout.show(layoutPanel, "HomePanel");
        });
        topContainer.add(homepageButton);

        topContainer.add(Box.createHorizontalStrut(horizontalOffset));

        // Button to ticketpage
        JButton ticketpanelButton = new JButton("Ticketpage");
        ticketpanelButton.setForeground(style.getButton1BackgroundColor());
        ticketpanelButton.setBackground(style.getButton1ForegroundColor());
        ticketpanelButton.setFont(style.getButtonFont());
        ticketpanelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        ticketpanelButton.addActionListener(e -> {
            CardLayout layout = (CardLayout) layoutPanel.getLayout();
            layout.show(layoutPanel, "TicketPanel");
        });
        topContainer.add(ticketpanelButton);
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

    @Override
    public void valueChanged(ListSelectionEvent e) {
        Person selectedPerson = this.personJList.getSelectedValue();
        if (selectedPerson == null){
            this.rightPanelLayout.show(this.rightPanel,"EmptyPanel");
        } else {
            this.rightPanel.add(createRightPanel(selectedPerson),"rightPanel");
            this.rightPanelLayout.show(this.rightPanel,"rightPanel");
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
}

