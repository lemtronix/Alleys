package model;

import java.util.List;

public class MarbleStateMoving implements MarbleState
{
    private final int _StartingNumberOfSpotsToGo = 63;
    private boolean _IsProtected = true;

    @Override
    public MarbleState play(Marble marble, Card card, boolean splitSeven)
    {
        MarbleState newMarbleState = null;
        boolean movingForward = true;

        Spot finalSpotToMoveTo = null;
        int numberOfFinishSpotsNeeded = 0;
        int numberOfBoardSpotsNeeded = 0;
        int numberOfBoardSpotsToGo = marble.getNumberOfBoardSpotsToGo();
        // Is this its move method?
        int cardValue = card.toInt(splitSeven);

        // Make sure that the move is valid
        if (cardValue < numberOfBoardSpotsToGo)
        {
            // No additional checks if the card value is less than the number of spots to go
            numberOfBoardSpotsNeeded = cardValue;
        }
        else if (cardValue <= (numberOfBoardSpotsToGo + AlleysGame.TotalNumberOfFinishSpots))
        {
            // Figure out how many finish spots does this marble need?
            numberOfFinishSpotsNeeded = cardValue - numberOfBoardSpotsToGo;
            numberOfBoardSpotsNeeded = cardValue - numberOfFinishSpotsNeeded;
            System.out.println("MarbleStateMoving: Marble Finishing and needs " + numberOfBoardSpotsNeeded
                    + " of board spots and " + numberOfFinishSpotsNeeded + " finishing spots!");
        }
        else
        {
            System.out.println("MarbleStateMoving: Not enough spots left.");
            marble.setMoveResultSuccess(false);
            return null;
        }

        int numberOfBoardSpotsToMove = Math.abs(numberOfBoardSpotsNeeded);
        int currentSpotNumber = marble.getCurrentSpot().getSpotNumber();
        int nextSpotNumber = currentSpotNumber;

        List<Spot> boardSpots = AlleysGame.getWorld().getSpots();

        if (cardValue >= 0)
        {
            movingForward = true;
        }
        else
        {
            movingForward = false;
        }

        while (numberOfBoardSpotsToMove != 0)
        {
            numberOfBoardSpotsToMove--;

            if (movingForward == true)
            {
                nextSpotNumber += 1;
            }
            else
            {
                nextSpotNumber -= 1;
            }

            // Handle wrapping values around the game board
            if (nextSpotNumber >= AlleysGame.MaxNumberOfSpots)
            {
                nextSpotNumber -= AlleysGame.MaxNumberOfSpots;
                nextSpotNumber += AlleysGame.FirstBoardSpot;
            }
            else if (nextSpotNumber < AlleysGame.FirstBoardSpot)
            {
                nextSpotNumber -= AlleysGame.FirstBoardSpot;
                nextSpotNumber += AlleysGame.MaxNumberOfSpots;
            }

            Spot spot = boardSpots.get(nextSpotNumber);

            if (spot.isOccupied() == true)
            {
                if (spot.getOccupyingMarble().isProtected() == true)
                {
                    System.out.println("MarbleStateMoving: Bumped up against a protected marble, cannot move!");

                    // Stay in the same state
                    marble.setMoveResultSuccess(false);
                    return null;
                }

                // Check if we need to send the landed spot's marble home
                if ((numberOfBoardSpotsToMove == 0) && (numberOfFinishSpotsNeeded == 0))
                {
                    // If not protected, send it home!
                    spot.getOccupyingMarble().setState(new MarbleStateHome());
                }

            }

            if (numberOfBoardSpotsToMove == 0)
            {
                finalSpotToMoveTo = spot;
            }
        }

        // Handle any finishing spots
        if (numberOfFinishSpotsNeeded > 0)
        {
            System.out.println("MarbleStateMoving: Handling Finishing Spots...");

            List<Spot> finishSpots = marble.getOwner().getFinishSpots();

            for (int i = 0; i < numberOfFinishSpotsNeeded; i++)
            {
                Spot finishSpot = finishSpots.get(i);

                if (finishSpot.isOccupied() == true)
                {
                    // Finish spots are always protected...
                    System.out
                            .println("MarbleStateMoving: Bumped up against a marble already in home, unable to move!");
                    marble.setMoveResultSuccess(false);
                    return null;
                }
            }

            // ArrayIndex uses zero based indexing
            finalSpotToMoveTo = finishSpots.get(numberOfFinishSpotsNeeded - 1);

            System.out.println("MarbleStateMoving: New state is MarbleStateFinishing");
            newMarbleState = new MarbleStateFinish(numberOfFinishSpotsNeeded);

        }

        // We're able to move, the first time the marble is moved, we transition out of the protected state
        if (_IsProtected == true)
        {
            _IsProtected = false;
        }

        marble.move(finalSpotToMoveTo);

        // After the move is successful, then handle the number of spots left to move
        numberOfBoardSpotsToGo -= cardValue;

        // handle the case where a 4 is played in start
        if (numberOfBoardSpotsToGo >= AlleysGame.TotalNumberOfBoardSpots)
        {
            numberOfBoardSpotsToGo -= AlleysGame.TotalNumberOfBoardSpots;
        }

        marble.setNumberOfBoardSpotsToGo(numberOfBoardSpotsToGo);

        System.out.println("Player: Exposed Marble is currently on spot: " + currentSpotNumber + " and moving to "
                + finalSpotToMoveTo.getSpotNumber() + " and has " + marble.getNumberOfBoardSpotsToGo()
                + " number of spots to go!");

        marble.setMoveResultSuccess(true);

        return newMarbleState;
    }

    @Override
    public MarbleState playJack(Marble marbleToMove, Card card, Marble marbleToMoveTo)
    {
        // Make sure the marble we're moving to isn't protected
        if (marbleToMoveTo.isProtected() == true)
        {
            System.out.println("MarbleStateMoving: Cannot swap with a protected marble!");
            marbleToMove.setMoveResultSuccess(false);
            return null;
        }
        Spot marbleToMoveSpot = marbleToMove.getCurrentSpot();
        Spot marbleToMoveToSpot = marbleToMoveTo.getCurrentSpot();

        int marbleToMoveSpotsToGo = calculateNumberOfSpotsToGo(marbleToMove, marbleToMoveTo);
        int marbleToMoveToSpotsToGo = calculateNumberOfSpotsToGo(marbleToMoveTo, marbleToMove);

        System.out.println("MarbleStateMoving: MarbleToMove used to have " + marbleToMove.getNumberOfBoardSpotsToGo()
                + " spots to go and now has " + marbleToMoveSpotsToGo + " spots to go!");

        // TODO Need some way to set this on the marble!
        System.out.println("MarbleStateMoving: MarbleToMoveTo now has " + marbleToMoveToSpotsToGo + " spots to go!");

        marbleToMove.setNumberOfBoardSpotsToGo(marbleToMoveSpotsToGo);
        marbleToMove.move(marbleToMoveToSpot);

        marbleToMoveTo.setNumberOfBoardSpotsToGo(marbleToMoveToSpotsToGo);
        marbleToMoveTo.move(marbleToMoveSpot);

        // We're able to move, the first time the marble is moved, we transition out of the protected state
        if (_IsProtected == true)
        {
            _IsProtected = false;
        }

        // swap spots and IDs with the other marble, calculate the new values to move
        marbleToMove.setMoveResultSuccess(true);
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
        marble.setNumberOfBoardSpotsToGo(_StartingNumberOfSpotsToGo);

        System.out.println(
                "MarbleState: Marble on the board! I have " + marble.getNumberOfBoardSpotsToGo() + " spots to go!");
        marble.move(marble.getStartingSpot());
    }

    @Override
    public void exit(Marble marble)
    {
        marble.setNumberOfBoardSpotsToGo(0);
        System.out.println("MarbleState: Marble leaving the board and has " + marble.getNumberOfBoardSpotsToGo()
                + " spots to go.");
    }

    private int calculateNumberOfSpotsToGo(Marble marbleToMove, Marble marbleToMoveTo)
    {
        int numberOfSpotsToGo = 0;
        int newSpotNumber = marbleToMoveTo.getCurrentSpot().getSpotNumber();
        int startingSpotNumber = marbleToMove.getStartingSpot().getSpotNumber();

        if (newSpotNumber < startingSpotNumber)
        {
            // If the new marble spot is less than the current marble spot;
            // TODO magic number of one added to new spot number, why?
            numberOfSpotsToGo = startingSpotNumber - (newSpotNumber + 1);
        }
        else if (newSpotNumber >= startingSpotNumber)
        {
            // otherwise, the new marble spot is greater than the current marble spot number
            int numberOfSpotsAdvanced = newSpotNumber - startingSpotNumber;
            numberOfSpotsToGo = _StartingNumberOfSpotsToGo - numberOfSpotsAdvanced;
        }

        return numberOfSpotsToGo;
    }
}