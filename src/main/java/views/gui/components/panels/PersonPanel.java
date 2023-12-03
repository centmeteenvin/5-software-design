package views.gui.components.panels;

import views.gui.styles.Style;

import javax.swing.*;
import java.awt.*;

public class PersonPanel extends JPanel {
    JPanel layoutPanel;
    Style style;

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public PersonPanel(JPanel layoutPanel, Style style) {
        this.layoutPanel = layoutPanel;
        this.style = style;

        this.setLayout(new BorderLayout());

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
        leftPanel.add(Box.createVerticalStrut(30));

        // Add Person button (Box2)
        JButton createPersonButton = new JButton("+ Add User");
        createPersonButton.setForeground(style.getButtonForegroundColor());
        createPersonButton.setBackground(style.getButtonBackgroundColor());
        createPersonButton.setFont(style.getButtonFont());
        createPersonButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        createPersonButton.addActionListener(e -> {
            System.out.println("Add user");
        });
        leftPanel.add(createPersonButton);


        return leftPanel;
    }

    JPanel createRightPanel() {
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(style.getBackgroundColor_secondary());
        return rightPanel;
    }

}
