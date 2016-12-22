package model;

import java.util.List;

public class MarbleStateMoving implements MarbleState
{
    private boolean _IsProtected = true;
    private int _NumberOfSpotsToGo = 64;

    @Override
    public MarbleState play(Marble marble, Card card)
    {
        // TODO: Test a marble should not be able to move backwards over a protected marble. Verify this is so.

        // Is this its move method?
        int cardValue = card.toInt();

        int currentSpotNumber = marble.getCurrentSpot().getSpotNumber();
        int nextSpotNumber = currentSpotNumber + 1;
        int newSpotNumber = currentSpotNumber + cardValue;

        // Handle wrapping values around the game board
        if (newSpotNumber >= AlleysGame.MaxNumberOfSpots)
        {
            System.out.println("MarbleMoving: Overflow wrapping event.  currentSpot: " + currentSpotNumber + " nextSpot: " + nextSpotNumber
                    + " newSpot: " + newSpotNumber);
            newSpotNumber = newSpotNumber - AlleysGame.MaxNumberOfSpots;
            newSpotNumber = AlleysGame.FirstMoveableSpot + newSpotNumber;
            System.out.println("Now newSpot: " + newSpotNumber);
        }
        else if (newSpotNumber < AlleysGame.FirstMoveableSpot)
        {
            System.out.println("MarbleMoving: Underflow wrapping event.  currentSpot: " + currentSpotNumber + " nextSpot: " + nextSpotNumber
                    + " newSpot: " + newSpotNumber);
            newSpotNumber = AlleysGame.FirstMoveableSpot - newSpotNumber;
            newSpotNumber = AlleysGame.MaxNumberOfSpots - newSpotNumber;
            System.out.println("Now newSpot: " + newSpotNumber);
        }

        List<Spot> spots = AlleysGame.getWorld()._Spots;

        // Make sure each spot we're moving across is not protected
        for (int i = nextSpotNumber; i <= newSpotNumber; i++)
        {
            Spot spot = spots.get(i);

            if (spot.isOccupied() == true)
            {
                if (spot.getOccupyingMarble().isProtected() == true)
                {
                    System.out.println("MarbleStateMoving: Bumped up against a protected marble, cannot move.");

                    // Stay in the same state
                    return null;
                }

                // Check if we need to send the landed spot's marble home
                if (i == newSpotNumber)
                {
                    // If not protected, send it home!
                    spot.getOccupyingMarble().setState(new MarbleStateHome());
                }
            }
        }

        // We're able to move, the first time the marble is moved, we transition out of the protected state
        if (_IsProtected == true)
        {
            _IsProtected = false;
        }

        System.out.println("Player: Exposed Marble is currently on spot: " + currentSpotNumber + " and moving to " + newSpotNumber);

        marble.move(spots.get(newSpotNumber));

        // Decrement the number of spots left to move
        _NumberOfSpotsToGo = _NumberOfSpotsToGo - cardValue;

        // Stay in the same state
        return null;
    }

    @Override
    public boolean isProtected()
    {
        return _IsProtected;
    }

    @Override
    public void enter(Marble marble)
    {
        // System.out.println("MarbleStateMoving: Entering...");
        marble.move(marble.getStartingSpot());
        System.out.println("MarbleState: Marble on the board! I have " + _NumberOfSpotsToGo + " spots to go!");

    }

    @Override
    public void exit(Marble marble)
    {
        // System.out.println("MarbleStateMoving: Exiting...");
        System.out.println("MarbleState: Bollocks! I had only " + _NumberOfSpotsToGo + " spots to go!");

    }
}