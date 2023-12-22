package views.gui.styles;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public interface Style {

    Color getBackgroundPrimaryColor();
    Color getBackgroundSecondaryColor();

    Color getButton1ForegroundColor();
    Color getButton1BackgroundColor();
    Color getButton2ForegroundColor();
    Color getButton2BackgroundColor();

    Color getListForegroundColor();
    Color getListBackgroundColor();

    Color getLinePrimaryColor();
    Color getLineSecondaryColor();

    Color getLabel1ForegroundColor();
    Color getLabel1BackgroundColor();
    Color getLabel2ForegroundColor();
    Color getLabel2BackgroundColor();

    Font getTitleFont();
    Font getSubtitleFont();
    Font getBoldSubtitleFont();
    Font getButtonFont();
    Font getListFont();
    Font getTextFont();
    Font getSmallTextFont();


    default Font addFont(String fontName, float size) {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/fonts/" + fontName + ".ttf")).deriveFont(size);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
            return font;
        } catch (IOException | FontFormatException e) {
            throw new RuntimeException(e);
        }
    }
}
