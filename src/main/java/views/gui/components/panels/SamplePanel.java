package views.gui.components.panels;

import views.gui.styles.Style;

import javax.swing.*;
import java.awt.*;

/**
 * A panel just for designing purposes.
 */
public class SamplePanel extends JPanel {
    public SamplePanel(int id) {
        this.setBackground(Style.getBackgroundColor_primary());
        this.setBorder(BorderFactory.createLineBorder(Style.getBackgroundColor_secondary(),10));
        this.setLayout(new BorderLayout());

        JLabel label = new JLabel("Sample panel " + id);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setPreferredSize(new Dimension(300,150));
        label.setFont(Style.getTitleFont());

        this.add(label, BorderLayout.CENTER);
        this.setVisible(true);
    }
}
