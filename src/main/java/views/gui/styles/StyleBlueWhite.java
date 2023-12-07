package views.gui.styles;

import java.awt.*;
import lombok.Data;

@Data
public class StyleBlueWhite implements Style{
    // Colors
    Color primaryColor = new Color(0,45,179);
    Color secondaryColor = Color.WHITE;
    //Background
    private Color backgroundPrimaryColor = primaryColor;
    private Color backgroundSecondaryColor = secondaryColor;
    // Buttons
    private Color button1ForegroundColor = primaryColor;
    private Color button1BackgroundColor = secondaryColor;
    private Color button2ForegroundColor = secondaryColor;
    private Color button2BackgroundColor = primaryColor;
    // List
    private Color listForegroundColor = primaryColor;
    private Color listBackgroundColor = secondaryColor;
    // Decorations
    private Color linePrimaryColor = primaryColor;
    private Color lineSecondaryColor = secondaryColor;
    // Labels
    private Color label1ForegroundColor = primaryColor;
    private Color label1BackgroundColor = secondaryColor;
    private Color label2ForegroundColor = secondaryColor;
    private Color label2BackgroundColor = primaryColor;

    // Fonts
    private Font titleFont = addFont("OpenSans-Bold", 122);
    private Font subtitleFont = addFont("OpenSans-Medium", 54);
    private Font boldSubtitleFont = addFont("OpenSans-Bold", 54);
    private Font buttonFont = addFont("OpenSans-Bold", 30);
    private Font listFont = addFont("OpenSans-Bold", 25);
    private Font textFont = addFont("Opensans-Medium", 30);
}
