package views.gui.components.panels;

import views.gui.styles.Style;

import javax.swing.*;
import java.awt.*;

/**
 * A panel just for designing purposes.
 */
public class SamplePanel extends JPanel {
    Style style;
    
    public SamplePanel(int id, Style style) {
        this.style = style;
        this.setBackground(style.getBackgroundColor_primary());
        this.setBorder(BorderFactory.createLineBorder(style.getBackgroundColor_secondary(),10));
        this.setLayout(new BorderLayout());

        JLabel label = new JLabel("Sample panel " + id);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setPreferredSize(new Dimension(300,150));
        label.setFont(style.getTitleFont());

        this.add(label, BorderLayout.CENTER);
        this.setVisible(true);
    }
}
