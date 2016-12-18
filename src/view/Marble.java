package view;

import java.awt.Color;
import java.awt.event.MouseEvent;

public class Marble extends GamePiece
{
    private Spot _OnSpot = null;
    
    public Marble(int X, int Y, Color DesiredColor)
    {
        super(X, Y, DesiredColor);
    }
    
    public void MoveTo(Spot SpotToMoveTo)
    {
        
        if (SpotToMoveTo == null)
        {
            System.err.println("Null spot received.");
            return;
        }

        _OnSpot = SpotToMoveTo;
                
        SetX(_OnSpot.GetX());
        SetY(_OnSpot.GetY());
        
    }
    
    public int CurrentlyOnSpotNumber()
    {
        int CurrentlyOnSpotNumber = -1;
        
        if (_OnSpot != null)
        {
            CurrentlyOnSpotNumber = _OnSpot.GetSpotNumber();
        }
        
        return CurrentlyOnSpotNumber;
    }
    
    protected void DraggedAction(MouseEvent e)
    {
        SetX(e.getX());
        SetY(e.getY());
    }
}
