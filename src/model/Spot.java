package model;

public class Spot
{
    private Marble _OccupiedBy = null;
    private int _SpotNumber = -1;
    
    public Spot(int spotNumber)
    {
        _SpotNumber = spotNumber;
    }

    public boolean setMarble(Marble marble)
    {
        _OccupiedBy = marble;
        
        return true;
    }
    
    public void clearMarble()
    {
        _OccupiedBy = null;
    }
    
    public int getSpotNumber()
    {
        return _SpotNumber;
    }
    
    public Marble getOccupyingMarble()
    {
        return _OccupiedBy;
    }
    
    public boolean isOccupied()
    {
        if (_OccupiedBy == null)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
}
