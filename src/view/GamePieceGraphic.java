package view;

import java.awt.Color;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public abstract class GamePieceGraphic extends MouseAdapter
{
    private int _Size = 15;
    private Color _Color;
    private Ellipse2D.Double _Piece;
    private boolean _IsHit = false;

    public GamePieceGraphic(int X, int Y, Color DesiredColor)
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
    
    public boolean IsHit()
    {
        return _IsHit;
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
    public void mouseReleased(MouseEvent e) 
    {
        Rectangle2D _Collision = _Piece.getBounds2D();
        
        if (_Collision.contains(e.getX(), e.getY()))
        {
            // System.out.println("GamePiece Released action at x: " + e.getX() + " y: " + e.getY());
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
        if (_IsHit == true)
        {
//          System.out.println("Dragging...");
            DraggedAction(e);
        }
    }
    
    protected int GetX()
    {
        return (int)_Piece.x;
    }
    
    protected int GetY()
    {
        return (int)_Piece.y;
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
//        System.out.println("GamePiece SelectedAction()");
        _IsHit = true;
    }
    protected void UnSelectedAction()
    {
//        System.out.println("GamePiece UnselectedAction()");
        _IsHit = false;
    }
    
    protected void DraggedAction(MouseEvent e) {};
}
