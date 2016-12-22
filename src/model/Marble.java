package model;

import controller.MarbleEvent;
import controller.MarbleListener;

public class Marble
{
    private int _MarbleIdNumber = -1;

    private MarbleState _MarbleState = null;

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

    public Spot getHomeSpot()
    {
        return _HomeSpot;
    }

    public void setStartingSpot(Spot startingSpot)
    {
        if (checkSpotNotNull(startingSpot) == false)
            return;

        _StartingSpot = startingSpot;
    }

    public Spot getStartingSpot()
    {
        return _StartingSpot;
    }

    public void setMarbleListener(MarbleListener listener)
    {
        if (listener != null)
        {
            _MarbleListener = listener;
        }
    }

    public void play(Card card)
    {
        MarbleState newMarbleState = _MarbleState.play(this, card);

        if (newMarbleState != null)
        {
            // Exit the current state
            _MarbleState.exit(this);

            _MarbleState = newMarbleState;

            // Enter the new state
            _MarbleState.enter(this);
        }
    }

    public void setState(MarbleState newMarbleState)
    {
        stateMayHaveChanged(newMarbleState);
    }

    public boolean move(Spot newSpot)
    {
        // First, check if we can traverse the pathway without hitting a protected marble
        // TODO: Spot numbers may not be sequential.. now what?

        // System.out.println("Marble: " + this + " moving...");

        newSpot.setMarble(this);

        if (_CurrentSpot != null)
        {
            // System.out.println("Marble: Clearing Spot.");
            // Clear the current spot if not null
            _CurrentSpot.clearMarble();
        }

        // Move to the new spot
        _CurrentSpot = newSpot;

        // Notify anyone interested that a marble has updated.
        notifyListeners(this, newSpot);

        return true;
    }

    public Spot getCurrentSpot()
    {
        return _CurrentSpot;
    }

    public boolean isOwner(Player player)
    {
        if (_Owner == player)
        {
            return true;
        }

        return false;
    }

    public boolean isProtected()
    {
        return _MarbleState.isProtected();
    }

    private void notifyListeners(Marble marble, Spot spot)
    {
        if (_MarbleListener != null)
        {
            // System.out.println("Marble: Notifying any listeners that an event occurred");
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

    private void stateMayHaveChanged(MarbleState newMarbleState)
    {
        System.out.println("Marble: State May Have Changed.");

        // If we were passed a state, and it's different than the current state
        // TODO: Is the state != newState necessary?
        if (newMarbleState != null && _MarbleState != newMarbleState)
        {
            // On initialization, we may not have a state here, check for null
            if (_MarbleState != null)
            {
                // Exit the current state
                System.out.println("Marble: Calling state's exit action.");
                _MarbleState.exit(this);
            }

            _MarbleState = newMarbleState;

            // Enter the new state
            System.out.println("Marble: Calling state's enter action.");
            _MarbleState.enter(this);
        }
    }
}
