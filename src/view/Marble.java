package view;

import java.awt.Color;
import java.awt.event.MouseEvent;

public class Marble extends GamePiece
{
    public Marble(int X, int Y, Color DesiredColor)
    {
        super(X, Y, DesiredColor);
    }
    
    public void MoveTo(Spot SpotToMoveTo)
    {
        SetX(SpotToMoveTo.GetX());
        SetY(SpotToMoveTo.GetY());
    }
    
    protected void DraggedAction(MouseEvent e)
    {
        SetX(e.getX());
        SetY(e.getY());
    }
}
