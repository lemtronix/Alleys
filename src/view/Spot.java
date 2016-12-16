package view;

import java.awt.Color;

public class Spot extends Piece
{
    public Spot(int X, int Y, Color DesiredColor)
    {
        super(X, Y, DesiredColor);
    }

    @Override
    protected void SelectedAction()
    {
        super.SelectedAction();
        
        SetColor(Color.yellow);
    }

    @Override
    protected void UnSelectedAction()
    {
        super.UnSelectedAction();
        
        DefaultColor();
    }
}
