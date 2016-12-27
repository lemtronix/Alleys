package model;

import java.util.ArrayList;
import java.util.List;

public class Player
{
    private ArrayList<Card> _CardsInHand = new ArrayList<Card>(5);
    private ArrayList<Spot> _FinishSpots = null;
    private int _MarblesNeededToFinish = 4;
    private String _Name = "";

    public Player(ArrayList<Spot> finishSpots)
    {
        _FinishSpots = finishSpots;
    }

    public void setName(String name)
    {
        _Name = name;
    }

    public String getName()
    {
        return _Name;
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

    public int getMarblesNeededToFinish()
    {
        return _MarblesNeededToFinish;
    }

    public void decrementMarblesNeededToFinish()
    {
        _MarblesNeededToFinish--;
    }

    public boolean play(Card playedCard, Marble marble)
    {
        // Check that the card is owned by the player and marble is owned by the player
        if (hasCard(playedCard) == false || marble.isOwner(this) == false)
        {
            System.err.println("Invalid card or marble played.");
            return false;
        }

        marble.play(playedCard);

        return true;
    }

    public List<Spot> getFinishSpots()
    {
        return _FinishSpots;
    }
}
