package view;

import java.awt.Color;

public class Spot extends GamePiece
{
    //TODO: Eventually, move this into a model package as this is logic that's used to calculate stuff that happens in the game
    private static int SpotCounter = 0;
    private int _SpotNumber = 0;
    
    public Spot(int X, int Y, Color DesiredColor)
    {
        super(X, Y, DesiredColor);
        
        // Each spot gets their own unique number
        _SpotNumber = SpotCounter;
        SpotCounter++;
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
