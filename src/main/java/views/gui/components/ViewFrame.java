package views.gui.components;

import javax.swing.*;
import java.awt.*;

public class ViewFrame extends JFrame {
    public ViewFrame() {
        this.setTitle("SKMT - Script Kiddos' Money Tracker");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(new Dimension(screenSize.width-100, screenSize.height-50));
    }
}
