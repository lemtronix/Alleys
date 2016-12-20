package model;

import controller.MarbleEvent;
import controller.MarbleListener;

public class Marble
{
    private int _MarbleIdNumber = -1;
        
    private Spot _HomeSpot = null;
    private Spot _StartingSpot = null;
    private Spot _CurrentSpot = null;
    
    private Player _Owner = null;
    
    private MarbleListener _MarbleListener = null;
    
    public Marble(int marbleIdNumber)
    {
        _MarbleIdNumber = marbleIdNumber;
    }
    
    public int getId()
    {
        return _MarbleIdNumber;
    }
    
    public void setOwner(Player owner)
    {
        _Owner = owner;
    }
    
    public Player getOwner()
    {
        return _Owner;
    }
    
    public void setHomeSpot(Spot homeSpot)
    {
        if (checkSpotNotNull(homeSpot) == false)
            return;
        
        _HomeSpot = homeSpot;
    }
    
    public void setStartingSpot(Spot startingSpot)
    {
        if (checkSpotNotNull(startingSpot) == false)
            return;
        
        _StartingSpot = startingSpot;
    }
    
    public void setMarbleListener(MarbleListener listener)
    {
        if (listener != null)
        {
            _MarbleListener = listener;
        }
    }
    
    public boolean moveToStartingSpot()
    {
        // TODO Only move that isn't allowed is if a players marble is already there.
        move(_StartingSpot);
        return true;
    }
    
    public boolean moveToHomeSpot()
    {
        // Home spot is always available, no need to check.
        move(_HomeSpot);
        return true;
    }
    
    public boolean move(Spot newSpot)
    {
        if (newSpot.isOccupied() == true)
        {
            // Tell the marble on the new spot that it's going home!
            newSpot.getOccupyingMarble().moveToHomeSpot();
        }
        
        if (newSpot.setMarble(this) == true)
        {
            if (_CurrentSpot != null)
            {
                // Clear the current spot if not null
                _CurrentSpot.clearMarble();
            }
            
            // Move to the new spot
            _CurrentSpot = newSpot;
            
            // Notify anyone interested that a marble has updated.
            notifyListeners(this, newSpot);
            
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public boolean isHome()
    {
        if (_CurrentSpot == _HomeSpot)
        {
            return true;
        }
        
        return false;
    }
    
    public Spot getCurrentSpot()
    {
        return _CurrentSpot;
    }

    public boolean IsOwner(Player player)
    {
        if (_Owner == player)
        {
            return true;
        }
        
        return false;
    }
    
    private void notifyListeners(Marble marble, Spot spot)
    {
        if (_MarbleListener != null)
        {
            System.out.println("Marble: Notifying any listeners that an event occurred");
            _MarbleListener.marbleEventOccurred(new MarbleEvent(this, marble.getId(), spot.getSpotNumber()));
        }
    }
    
    private boolean checkSpotNotNull(Spot spotToCheck)
    {
        if (spotToCheck == null)
        {
            System.out.println("Marble: Null spot passed in for spot");
            return false;
        }

        return true;
    }
}
