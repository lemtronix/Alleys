package model;

/**
 * The colors of the marbles in the game; this enum also defines the order in which players move and 
 * in which the colors appear on the playing board. The rules specify that Red is the dealer, so we
 * use that as a starting color.
 * @author rcook
 *
 */
public enum MarbleColor
{
     RED
    ,BLUE
    ,YELLOW
    ,GREEN
    ;

    public String toString() { return this.name().toLowerCase(); } 
    
    private static MarbleColor colors[] = MarbleColor.values();
    
    /**
     * returns the marble color that is moved first in the game.
     * @return MarbleColor
     */
    public static MarbleColor getInitialColor() { return RED; }
    
    /**
     * returns the color that moves after the given color
     * @param marbleColor to start with
     * @return MarbleColor that is next in sequence, wrapping at the end to return
     * to the first MarbleColor of the enum
     */
    public MarbleColor getNextColor(MarbleColor marbleColor)
    {
        MarbleColor result = null;
        switch (marbleColor)
        {
        case RED:       result = BLUE;      break;
        case BLUE:      result = YELLOW;    break;
        case YELLOW:    result = GREEN;     break;
        case GREEN:     result = RED;       break;
        }
        return result;
    }
    
    public static MarbleColor getColor(int ordinal) { return colors[ordinal]; }
    
}
