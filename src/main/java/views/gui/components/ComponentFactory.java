package views.gui.components;

import views.gui.styles.Style;

import javax.swing.*;
import java.awt.*;

public class ComponentFactory {
    Style style;

    public ComponentFactory(Style style) {
        this.style = style;
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