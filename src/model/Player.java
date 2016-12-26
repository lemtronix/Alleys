package model;

import java.util.ArrayList;
import java.util.List;

public class Player
{
    private ArrayList<Card> _CardsInHand = new ArrayList<Card>(5);
    private ArrayList<Spot> _FinishSpots = null;
    private int _MarblesLeftToFinish = 4;

    // They need to know what kind of card they have?
    // Need to be able to play a card
    //
    // Always has 4 marbles, which marbles are they?
    // Need to be able to move their own marble.

    public Player(ArrayList<Spot> finishSpots)
    {
        _FinishSpots = finishSpots;
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

    public int getMarblesLeftToFinish()
    {
        return _MarblesLeftToFinish;
    }

    public boolean play(Card playedCard, Marble marble)
    {
        // Check that the card is owned by the player and marble is owned by the player
        if (hasCard(playedCard) == false || marble.isOwner(this) == false)
        {
            System.err.println("Invalid card or marble played.");
            return false;
        }

        // System.out.println("Game: Player is attempting to play the: " + playedCard.toString() + " on marble #" + marble);

        marble.play(playedCard);

        // And they have enough spots left on the game board
        // And the spot they're going to doesn't jump any of their own marbles
        // Then move the marble to the appropriate spot
        // and notify anyone interested in cards and marbles that there's an update.
        // End the turn?
        return true;
    }

    public List<Spot> getFinishSpots()
    {
        return _FinishSpots;
    }
}
