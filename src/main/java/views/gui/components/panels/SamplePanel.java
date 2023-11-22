package views.gui.components.panels;

import javax.swing.*;
import java.awt.*;

/**
 * A panel just for designing purposes.
 */
public class SamplePanel extends JPanel {
    public SamplePanel(int id) {
        this.setBackground(Color.BLUE);
        this.setBorder(BorderFactory.createLineBorder(Color.white,10));
        this.setLayout(new BorderLayout());

        JLabel label = new JLabel("Sample panel " + id);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setPreferredSize(new Dimension(300,150));
        Font labelFont = new Font(Font.SANS_SERIF, Font.BOLD, 40);
        label.setFont(labelFont);

        this.add(label, BorderLayout.CENTER);
        this.setVisible(true);
    }
}
