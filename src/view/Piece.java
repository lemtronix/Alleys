package view;

import java.awt.Color;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public abstract class Piece extends MouseAdapter
{
    private int _Size = 15;
    private Color _Color;
    private Ellipse2D.Double _Piece;
    private boolean _IsSelected = false;

    public Piece(int X, int Y, Color DesiredColor)
    {
        _Color = DesiredColor;

        // TODO: Generic Shape class does not allow for the object to be moved.  How to cast to exact shape? 
        _Piece = new Ellipse2D.Double(X, Y, _Size, _Size);
    }

    public void DefaultColor()
    {
        _Color = Color.gray;
    }

    public void SetColor(Color c)
    {
        _Color = c;
    }

    public Color GetColor()
    {
        return _Color;
    }

    public Shape GetShape()
    {
        return _Piece;
    }
    
    @Override
    public void mousePressed(MouseEvent e)
    {
        Rectangle2D _Collision = _Piece.getBounds2D();
        
        if (_Collision.contains(e.getX(), e.getY()))
        {
            SelectedAction();
        }
        else
        {
            UnSelectedAction();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
        if (_IsSelected == true)
        {
//          System.out.println("Dragging...");
            DraggedAction(e);
        }
    }
     
    protected void SetX(int X)
    {
        _Piece.x = X;
    };
    
    protected void SetY(int Y)
    {
        _Piece.y = Y;
    }
    
    protected void SelectedAction()
    {
//      System.out.println("Clicked on piece!");
        _IsSelected = true;
    }
    protected void UnSelectedAction()
    {
        _IsSelected = false;
    }
    
    protected void DraggedAction(MouseEvent e) {};
}
