package view;

import java.awt.Color;
import java.awt.event.MouseEvent;

public class Marble extends Piece
{
    public Marble(int X, int Y, Color DesiredColor)
    {
        super(X, Y, DesiredColor);
    }

    protected void DraggedAction(MouseEvent e)
    {
        SetX(e.getX());
        SetY(e.getY());
    }
}
