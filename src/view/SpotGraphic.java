package view;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import model.Marble;

public class SpotGraphic
{
    private static final int SIZE = 15;
    private Color   baseColor;
    private int     spotIndex;
    private int     xPosition;
    private int     yPosition;
    private Marble  marble = null;
    
    private Ellipse2D.Double shape;
    
    public SpotGraphic(int spotIdNumber, int x, int y, Color baseColor)
    {
        spotIndex = spotIdNumber;
        xPosition = x;
        yPosition = y;
        if (baseColor != null)   { this.baseColor = baseColor; }
                            else { this.baseColor = Color.pink; }
        shape = new Ellipse2D.Double(x, y, SIZE, SIZE);
    }

    public int      getSpotIndex()  { return spotIndex; }
    public int      getXPosition()  { return xPosition; }
    public int      getYPosition()  { return yPosition; }
    public Ellipse2D.Double getShape() { return shape; }
    
    public Marble   getMarble()                 { return marble;    }
    public void     setMarble(Marble marble)    { this.marble = marble; }
    
    public Color    getBaseColor()              { return baseColor; }
    public void     setBaseColor(Color color)   { baseColor = color; }
    
    /**
     * return the color for this spot; if there is a marble on the spot,
     * then the marble color, otherwise the base color for the spot.
     * @return
     */
    public Color getColor()
    {
        Color result;
        if (marble != null) { result = ViewColor.getMarbleColor(marble.getColor()); }
                       else { result = getBaseColor(); }
        return result;
    }
    
    /**
     * return true iff this spot graphic contains the given x,y position.
     * @param x
     * @param y
     * @return true if x,y is within this spot
     */
    public boolean containsPosition(int x, int y)
    {
        Rectangle2D collisionRectangle = shape.getBounds2D();
        return collisionRectangle.contains(x, y);
    }
}
