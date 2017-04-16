package view;

import java.awt.Color;
import java.util.HashMap;

import model.MarbleColor;

/**
 * Contains colors for the view, corresponding to the model's MarbleColors. 
 * Includes colors for marbles and different colors for the colored spots on the boards.
 * @author rcook
 *
 */
public class ViewColor
{
    private static HashMap<MarbleColor, Color> marbleColors = new HashMap<>();
    private static HashMap<MarbleColor, Color> spotColors = new HashMap<>();
    
    // initialize the two maps, one with marble colors and one with the corresponding
    // spot colors.
    static
    {
        setColors(MarbleColor.RED,      Color.red,                      new Color(0xFF, 0x99, 0x99));
        setColors(MarbleColor.BLUE,     Color.blue,                     new Color(0x00, 0x99, 0xFF));
        setColors(MarbleColor.YELLOW,   Color.yellow,                   new Color(0xff, 0xff, 0x99));
        setColors(MarbleColor.GREEN,    new Color(0x00, 0x66, 0x33),    new Color(0x66, 0xCC, 0x33));
    }
    
    // set the marble color map and the spot color map for the given marble color.
    private static void setColors(MarbleColor marbleColor, Color realColor, Color spotColor)
    {
        marbleColors.put(marbleColor, realColor);
        spotColors.put(marbleColor, spotColor);
    }
    
    /**
     * Return the marble graphic color for the given MarbleColor.
     * @param marbleColor
     * @return Color
     */
    public static Color getMarbleColor(MarbleColor marbleColor) { return marbleColors.get(marbleColor); }
    
    /**
     * Return the spot color for the given MarbleColor.
     * @param marbleColor
     * @return Color
     */
    public static Color getSpotColor(MarbleColor marbleColor) { return spotColors.get(marbleColor); }
}
