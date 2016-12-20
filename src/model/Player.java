package model;

import java.util.ArrayList;
import java.util.List;

public class Player
{
    private ArrayList<Card> _CardsInHand = new ArrayList<Card>(5);
    
    // They need to know what kind of card they have?
    // Need to be able to play a card
    // 
    // Always has 4 marbles, which marbles are they?
    // Need to be able to move their own marble.
    
    // 
    
    public Player()
    {
    }
    
    public void addCard(Card dealtCard)
    {
        if (dealtCard != null)
        {
            _CardsInHand.add(dealtCard);
        }
    }
    
    public List<Card> getCards()
    {
        return _CardsInHand;
    }
    
    public boolean hasCard(Card card)
    {
        if (_CardsInHand.contains(card) == true)
        {
            return true;
        }
        
        return false;
    }
        
    public boolean play(Card playedCard, Marble marble)
    {
        // Check that the card is owned by the player and marble is owned by the player
        if (hasCard(playedCard) == false || marble.IsOwner(this) == false)
        {
            System.err.println("Invalid card or marble played.");
            return false;
        }
        
        System.out.println("Game: Player is attempting to play the: " + playedCard.toString() + " on marble #" + marble);
        
        // TODO This logic depends on the state of the marble
        // User must play a K or an A to get out of their home base
        if (marble.isHome())
        {
            if (playedCard.getRank() == CardValue.King || playedCard.getRank() == CardValue.Ace)
            {
                // TODO: Move the marble to the starting spot
                marble.moveToStartingSpot();
            }
            else
            {
                System.out.println("Need to play a King or an Ace to get out of home!");
                return false;
            }
        }
        else
        {
            // If not home, then add the card value to the marble's current position and move it to the new position.
            int currentSpotNumber = marble.getCurrentSpot().getSpotNumber();
            int cardValue = playedCard.toInt();
            int newSpotNumber = currentSpotNumber + cardValue;
            
            System.out.println("Player: Marble is currently on spot: " + currentSpotNumber + " and moving to " + newSpotNumber);
            
            // TODO is this okay to getWorld or am I breaking encapsulation?
            marble.move(AlleysGame.getWorld()._Spots.get(newSpotNumber));
        }
        
        // And they have enough spots left on the game board
        // And the spot they're going to doesn't jump any of their own marbles
        // Then move the marble to the appropriate spot
        // and notify anyone interested in cards and marbles that there's an update.
        // End the turn?
        return true;
    }
}
