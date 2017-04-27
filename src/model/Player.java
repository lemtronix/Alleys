package model;

import java.util.ArrayList;
import java.util.List;

public class Player implements Comparable<Player>
{
    private String name = "";
    private MarbleColor color;
    private MarbleColor partnerColor;

    private ArrayList<Card> cards = new ArrayList<>();
    
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

    public void foldCards()
    {
        System.out.println(getName() + " has folded this hand.");
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
