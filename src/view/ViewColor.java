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
//    private static void say(String s) { System.out.println(s); }
    private static HashMap<MarbleColor, Color> marbleColors = new HashMap<>();
    private static HashMap<MarbleColor, Color> spotColors = new HashMap<>();
    private static HashMap<MarbleColor, Color> textColors = new HashMap<>();
    
    // initialize the two maps, one with marble colors and one with the corresponding
    // spot colors.
    static
    {
        setColors(MarbleColor.RED,      Color.red,                      new Color(0xFF, 0x99, 0x99), Color.white);
        setColors(MarbleColor.BLUE,     Color.blue,                     new Color(0x00, 0x99, 0xFF), Color.white);
        setColors(MarbleColor.YELLOW,   Color.yellow,                   new Color(0xffffcc), Color.black);
        setColors(MarbleColor.GREEN,    new Color(0x00, 0x66, 0x33),    new Color(0x66, 0xCC, 0x33), Color.white);
    }

// as marble color new Color(0xffe000)
// we've tried different things for yellow        
//                                        Color.yellow, // (0xffff00),
//                                        new Color
//                                                  (0xfffd01),
//                                                  (0xfffd38),
//                                                  (0xfffd40),
//                                                    (0xffff30),
//                                                    (0xffe000),   <--
//                                                    Color.white
//                                        new Color
//                                                    (0xFFFFE0) 
//                                                    (0xfafad2) 
//                                                    (0xffffcc)    <-- 
//                                                                                    (0xff, 0xff, 0x99));
//                ); 
    
    // set the marble color map and the spot color map for the given marble color.
    private static void setColors(MarbleColor marbleColor, Color realColor, Color spotColor, Color textColor)
    {
        marbleColors.put(marbleColor, realColor);
        spotColors.put(marbleColor, spotColor);
        textColors.put(marbleColor, textColor);
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
    
    /**
     * return the text color for the given marble color.
     */
    public static Color getTextColor(MarbleColor marbleColor) { return textColors.get(marbleColor); }
}
