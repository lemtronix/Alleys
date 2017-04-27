package model;

/**
 * A spot on the (virtual) board. An alleys board has several kinds of spots: 
 * <ul>
 * <li>starting spots (where marbles sit before they're played), 
 * <li>home spots (the first spot a marble of a given color is played before its trip around the board,
 * <li>finishing spots (where marbles are placed at the end of their trip; when all four
 * finishing spots are filled, that player/color is finished
 * <li>spots (all the other spots, mostly making the loop around the board that marbles travel).
 * <el>
 * 
 * <P>A spot may or may not have a marble on it. It can only have one.
 * <P>A spot may or may not have a marble's color on it; only bank, starting, and homebase spots
 * have a color. If the spot has a marble, it may be any color in general, though in practice
 * starting and finishing spots only have marble of their own color.
 * 
 * <P>Note that this class has no UI components; the view package contains all components with
 * any dependency on a particular user interface environment.
 * @author rcook
 *
 */
public class Spot
{
    private int         spotIndex;                  // index into big array of spots on the board.
    private Marble      marble          = null;     // null when no marble is on the spot.
    private SpotType    spotType        = null;     // never null;
    private MarbleColor color           = null;     // null unless a starting, home, or finishing type
    private boolean     protectedMarble = false;    // true iff a marble landed on a starting spot from its bank.
    
    public Spot(int spotIndex, SpotType st)
    {
        this.spotIndex = spotIndex;
        spotType = st;
    }
    
    public Spot(int spotIndex, SpotType st, MarbleColor c)
    {
        this(spotIndex, st);
        color = c;
    }

    public String toString() 
    { 
        StringBuilder result = new StringBuilder();
        result.append(spotType.name());
        result.append(" ");
        result.append( (color == null) ? "" : color.name() );
        result.append(" ");
        result.append( (marble == null) ? "" : ", " + marble.getColor().name() + " marble");
        return result.toString();
    } 

    public void     setMarble(Marble m)         { marble = m;    }
    public Marble   getMarble()                 { return marble;    }
    public void     clearMarble()               { marble = null;    }
    public boolean  hasMarble()                 { return (marble != null); }
    
    public void     setSpotType(SpotType st)    { spotType = st; }
    public SpotType getSpotType()               { return spotType; }
    
    public MarbleColor getColor()                 { return color; }
    public void     setMarbleColor(MarbleColor c) { color = c; }
    public boolean  hasColor()                      { return color != null; }
    
    public void     setProtectedMarble(boolean p) { protectedMarble = p; }
    public boolean  getProtectedMarble()        { return protectedMarble; }
    
    /**
     * returns true iff this spot is a home spot, and there is a marble on it that
     * arrived on the spot by being moved out of its bank, i.e., a starting move.
     * @return
     */
    public boolean  isProtected()               { return protectedMarble; }
    
    public int getSpotIndex() { return spotIndex; }
}
