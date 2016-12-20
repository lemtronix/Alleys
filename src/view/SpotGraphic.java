package view;

import java.awt.Color;

public class SpotGraphic extends GamePieceGraphic
{
    private int _SpotNumber = -1;
    
    public SpotGraphic(int spotIdNumber, int X, int Y, Color DesiredColor)
    {
        super(X, Y, DesiredColor);
        
        // Each spot gets their own unique number
        _SpotNumber = spotIdNumber;
    }

    public int GetSpotNumber()
    {
        return _SpotNumber;
    }
    
    @Override
    protected void SelectedAction()
    {
        super.SelectedAction();
        
        SetColor(Color.pink);
    }

    @Override
    protected void UnSelectedAction()
    {
        super.UnSelectedAction();
        
        DefaultColor();
    }
}
