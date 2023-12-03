package views.gui.styles;

import java.awt.*;
import lombok.Data;

@Data
public class StyleBlueWhite implements Style{
    // Colors
    //Background
    private Color backgroundColor_primary = new Color(0,45,179);
    private Color backgroundColor_secondary = Color.WHITE;
    // Buttons
    private Color buttonForegroundColor = new Color(0,45,179);
    private Color buttonBackgroundColor = Color.WHITE;

    // Fonts
    private Font titleFont = addFont("OpenSans-Bold", 122);
    private Font subtitleFont = addFont("OpenSans-Medium", 54);
    private Font boldSubtitleFont = addFont("OpenSans-Bold", 54);
    private Font buttonFont = addFont("OpenSans-Bold", 30);
    private  Font listFont = addFont("OpenSans-Bold", 25);
}
