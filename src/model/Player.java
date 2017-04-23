package model;

import java.util.ArrayList;
import java.util.List;

public class Player implements Comparable<Player>
{
    private String name = "";
    private MarbleColor color;
    private MarbleColor partnerColor;

    private ArrayList<Card> cards = new ArrayList<>();
    
    private ArrayList<Spot> _FinishSpots = null;
    private int _MarblesNeededToFinish = 4;
    
    public Player(String name, MarbleColor color, MarbleColor partnerColor)
    {
        this.name = name;
        this.color = color;
        this.partnerColor = partnerColor;
    }
    
    public String toString()
    {
        return String.format("%s (%s)", name, color.toString());
    }

//    public Player(ArrayList<Spot> finishSpots, String color) {
//        _FinishSpots = finishSpots;
//        this.color = color;
//    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        String name = this.name;

        if (name == "")
        {
            name = color.toString();
        }

        return name;
    }

    public MarbleColor getColor()    {        return color;    }
    public MarbleColor getPartnerColor() { return partnerColor; }

    public void addCard(Card dealtCard)
    {
        if (dealtCard != null)
        {
            cards.add(dealtCard);
        }
    }

    public List<Card> getCards()
    {
        return cards;
    }

    public boolean hasCard(Card card)
    {
        if (cards.contains(card) == true)
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

    public boolean play(Card playedCard, Marble marble, boolean splitSeven)
    {
        boolean ableToPlayCard = false;

        // Check that the player own the marble
//        if (marble.isOwner(this) == false)
//        {
//            System.err.println("Player: Player does not own this marble.");
//            return false;
//        }

        // When sevens are split into two cards, the player doesn't technically own those card, make checking conditional
        if (splitSeven == false)
        {
            if (hasCard(playedCard) == false)
            {
                System.err.println("Player: Player does not have this card!");
            }
        }

//        ableToPlayCard = marble.play(playedCard, splitSeven);

        if (ableToPlayCard == true)
        {
            removeCard(playedCard);
        }

        return ableToPlayCard;
    }

    public boolean playJack(Card playedCard, Marble marbleToMove, Marble marbleToMoveTo)
    {
        boolean ableToPlayCard = false;

        // Check that the card is owned by the player and marble is owned by the player
//        if (hasCard(playedCard) == false || marbleToMove.isOwner(this) == false)
//        {
//            if (marbleToMove.isOwner(this) == false)
//            {
//                System.err.println("Player: Player does not own this marble.");
//            }
//            else
//            {
//                System.err.println("Player: Player does not have this card.");
//            }
//
//            return false;
//        }

//        ableToPlayCard = marbleToMove.playJack(playedCard, marbleToMoveTo);

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
        cards.clear();
    }

    public void removeCard(Card card)
    {
        cards.remove(card);
    }

    @Override
    public int compareTo(Player otherPlayer)
    {
        int result = 0;
        try { result = color.compareTo(otherPlayer.color); } 
        catch (Exception e) { e.printStackTrace(); }  // TODO: figure out what to do with model exceptions
        
        return result;
    }
        
}
