package views.gui.styles;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public interface Style {

    Color getBackgroundColor_primary();
    Color getBackgroundColor_secondary();

    Color getButtonForegroundColor();
    Color getButtonBackgroundColor();

    Font getTitleFont();
    Font getSubtitleFont();
    Font getButtonFont();

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
