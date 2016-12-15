package view;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

public class Spot
{
    private int _SpotX = 0;
    private int _SpotY = 0;
    private int _Size = 15;
    private Color _Color;
    private Shape _Shape;

    public Spot(int X, int Y, Color DesiredColor)
    {
        _SpotX = X;
        _SpotY = Y;
        _Color = DesiredColor;

        _Shape = new Ellipse2D.Double(_SpotX, _SpotY, _Size, _Size);
    }

    public void SetDefaultColor()
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
        return _Shape;
    }
}
