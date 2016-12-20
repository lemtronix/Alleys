package controller;

import java.util.EventObject;

public class MarbleEvent extends EventObject
{
    private int _MarbleIdNumber = -1;
    private int _SpotIdNumber = -1;
    
    public MarbleEvent(Object arg0, int marbleIdNumber, int spotIdNumber) {
        super(arg0);
        
        _MarbleIdNumber = marbleIdNumber;
        _SpotIdNumber = spotIdNumber;
        
    }
    
    public int getMarbleIdNumber()
    {
        return _MarbleIdNumber;
    }
    
    public int getSpotIdNumber()
    {
        return _SpotIdNumber;
    }
}
