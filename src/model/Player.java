package model;

import java.util.ArrayList;
import java.util.List;

public class Player
{
    private ArrayList<Card> _CardsInHand = new ArrayList<Card>(5);
    private ArrayList<Spot> _FinishSpots = null;
    private int _MarblesNeededToFinish = 4;
    private String _Name = "";
    private String _Color;

    public Player(ArrayList<Spot> finishSpots, String color)
    {
        _FinishSpots = finishSpots;
        _Color = color;
    }

    public void setName(String name)
    {
        _Name = name;
    }

    public String getName()
    {
        String name = _Name;

        if (_Name == "")
        {
            name = getColor();
        }

        return name;
    }

    public String getColor()
    {
        return _Color;
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
        boolean ableToPlayCard = false;

        // Check that the card is owned by the player and marble is owned by the player
        if (hasCard(playedCard) == false || marble.isOwner(this) == false)
        {
            if (marble.isOwner(this) == false)
            {
                System.err.println("Player: Player does not own this marble.");
            }
            else
            {
                System.err.println("Player: Player does not have this card.");
            }

            return false;
        }

        ableToPlayCard = marble.play(playedCard);

        if (ableToPlayCard == true)
        {
            removeCard(playedCard);
        }

        return ableToPlayCard;
    }

    public List<Spot> getFinishSpots()
    {
        return _FinishSpots;
    }

    public void foldCards()
    {
        System.out.println(getName() + " has folded their cards.");
        _CardsInHand.clear();
    }

    private void removeCard(Card card)
    {
        _CardsInHand.remove(card);
    }
}
