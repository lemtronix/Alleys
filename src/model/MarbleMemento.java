package model;

public class MarbleMemento
{
    private MarbleState _MarbleState;
    private Spot _CurrentSpot;
    private int _NumberOfSpotsToGo;

    public MarbleMemento(MarbleState marbleState, Spot currentSpot, int numberOfSpotsToGo)
    {
        _MarbleState = marbleState;
        _CurrentSpot = currentSpot;
        _NumberOfSpotsToGo = numberOfSpotsToGo;
    }

    public MarbleState getMarbleState()
    {
        return _MarbleState;
    }

    public Spot getCurrentSpot()
    {
        return _CurrentSpot;
    }

    public int getNumberOfSpotsToGo()
    {
        return _NumberOfSpotsToGo;
    }
}
