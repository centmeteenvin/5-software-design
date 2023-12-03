package views.gui.components.panels;

import views.gui.styles.StyleBlueWhite;
import javax.swing.*;
import java.awt.*;

public class PersonPanel extends JPanel {
    JPanel layoutPanel;
    StyleBlueWhite style;
    public PersonPanel(JPanel layoutPanel) {
        this.layoutPanel = layoutPanel;

        this.setBackground(style.getBackgroundColor_primary());
        this.setLayout(new BorderLayout());

        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(style.getBackgroundColor_secondary());
        this.add(leftPanel, BorderLayout.LINE_START);
    }
}
