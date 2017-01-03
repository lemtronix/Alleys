package model;

public class MarbleStateHome implements MarbleState
{

    @Override
    public MarbleState play(Marble marble, Card card)
    {
        Spot startingSpot = marble.getStartingSpot();

        // User must play a K or an A to advance.
        if (card.getRank() == CardValue.King || card.getRank() == CardValue.Ace)
        {
            if (startingSpot.isOccupied() == true)
            {
                // System.out.println("MarbleStateHome: Starting spot is occupied.");

                if (startingSpot.getOccupyingMarble().isProtected() == true)
                {
                    // Protected marble on get starting spot.
                    System.out.println("MarbleStateHome: Unable to move to starting spot!");
                    marble.setMoveResultSuccess(false);
                    return null;
                }
                else
                {
                    // Marble on the starting spot is not protected, send it home!
                    System.out.println("MarbleStateHome: Marble on the starting spot is not protected, sending it home!");
                    startingSpot.getOccupyingMarble().setState(new MarbleStateHome());
                }
            }
            else
            {
                // System.out.println("MarbleStateHome: Starting spot not occupied.");
            }
        }
        else
        {
            // Stay in this same state
            System.out.println("MarbleStateHome: Must play a King or an Ace to get out of start!");
            marble.setMoveResultSuccess(false);
            return null;
        }

        marble.setMoveResultSuccess(true);
        return new MarbleStateMoving();
    }

    @Override
    public MarbleState playJack(Marble marble, Card card, Marble marbleToMoveTo)
    {
        System.out.println("MarbleStateHome: Jacks cannot be played on a marble in home.");
        marble.setMoveResultSuccess(false);
        return null;
    }

    @Override
    public boolean isProtected()
    {
        return true;
    }

    @Override
    public void enter(Marble marble)
    {
        // System.out.println("MarbleStateHome: Entering...");
        marble.move(marble.getHomeSpot());
    }

    @Override
    public void exit(Marble marble)
    {
        // System.out.println("MarbleStateHome: Exiting...");
    }

}