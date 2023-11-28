package views.gui.styles;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public record Style() {
    // Colors
    static Color backgroundColor_primary = Color.BLUE;
    static Color backgroundColor_secondary = Color.WHITE;

    // Fonts
    static Font titleFont = addFont("OpenSans-Bold", 122);
    static Font subtitleFont = addFont("OpenSans-Medium", 54);

    public static Color getBackgroundColor_primary() {
        return backgroundColor_primary;
    }

    public static Color getBackgroundColor_secondary() {
        return backgroundColor_secondary;
    }

    public static Font getTitleFont() {
        return titleFont;
    }

    public static Font getSubtitleFont() {
        return subtitleFont;
    }

    private static Font addFont(String fontName, float size) {
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
