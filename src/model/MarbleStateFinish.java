package model;

import java.util.List;

public class MarbleStateFinish implements MarbleState
{
    // 1 is the first spot
    // 4 is the last spot (no more moves needed)

    private int _FinishSpotNumberOccupied = 0;

    public MarbleStateFinish(int finishSpotNumberOccupied)
    {
        _FinishSpotNumberOccupied = finishSpotNumberOccupied;
    }

    @Override
    public MarbleState play(Marble marble, Card card)
    {

        // Jacks and 4s cannot be played in finish!
        CardValue cardRank = card.getRank();

        if (cardRank == CardValue.Jack || cardRank == CardValue.Four)
        {
            System.out.println("MarbleStateFinish: Jacks and Fours cannot be played in home!");
            marble.setMoveResultSuccess(false);
            return null;
        }

        // Make sure the cardValue is able to fit within the remaining spaces
        int cardValue = card.toInt();

        if (cardValue > (AlleysGame.TotalNumberOfFinishSpots - _FinishSpotNumberOccupied))
        {
            System.out.println("MarbleStateFinish: Card value too great to play on this marble!");
            marble.setMoveResultSuccess(false);
            return null;
        }

        // Figure out if we're able to move forward
        List<Spot> finishSpots = marble.getOwner().getFinishSpots();

        int currentFinishSpotNumber = _FinishSpotNumberOccupied - 1;
        int nextFinishSpotNumber = currentFinishSpotNumber + 1;
        int newFinishSpotNumber = currentFinishSpotNumber + cardValue;

        Spot finalSpotToMoveTo = null;

        for (int i = nextFinishSpotNumber; i <= newFinishSpotNumber; i++)
        {
            Spot spot = finishSpots.get(i);

            if (spot.isOccupied() == true)
            {
                if (spot.getOccupyingMarble().isProtected() == true)
                {
                    System.out.println("MarbleStateMoving: Bumped up against a protected marble, cannot move!");

                    // Stay in the same state
                    marble.setMoveResultSuccess(false);
                    return null;
                }
            }

            if (i == newFinishSpotNumber)
            {
                finalSpotToMoveTo = spot;
            }
        }

        marble.move(finalSpotToMoveTo);

        _FinishSpotNumberOccupied += cardValue;

        System.out.println("MarbleStateFinish: Marble has advanced " + cardValue + " number of finish spots!");

        marble.setMoveResultSuccess(true);
        return null;
    }

    @Override
    public void enter(Marble marble)
    {
        Player owner = marble.getOwner();
        // When we enter this state, decrement the number of marbles needed to finish.
        owner.decrementMarblesNeededToFinish();
        System.out.println("Player now needs " + owner.getMarblesNeededToFinish() + " marbles in finish to end the game!");
    }

    @Override
    public void exit(Marble marble)
    {

    }

    @Override
    public boolean isProtected()
    {
        return true;
    }

}
