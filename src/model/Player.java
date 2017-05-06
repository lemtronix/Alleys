package model;

import java.util.ArrayList;
import java.util.List;

public class Player implements Comparable<Player>
{
    private String      name = "<unknown>";
    private MarbleColor color;
    private Player      partner;
//    private MarbleColor partnerColor;
    
    // set to true when all four marbles are in Homebase.
    private boolean     allMarblesIn = false;   
    
    public void setAllMarblesIn(boolean b) { allMarblesIn = b; }
    public boolean getAllMarblesIn() { return allMarblesIn; }

    private ArrayList<Card> cards = new ArrayList<>();
    
    public Player(String name, MarbleColor color) // , Player partner)
    {
        this.name = name;
        this.color = color;
    }
    
    public void setPartner(Player p) { partner = p; }
    public Player getPartner() { return partner; }
    
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

    public MarbleColor getColor()        { return color;    }
    public MarbleColor getPartnerColor() { return partner.getColor(); }
    
    /**
     * return the color being played by this player; if all his marbles
     * are in and the game is a partnered game, then this color is his
     * partner's color, otherwise his own color.
     * @return
     */
    public MarbleColor getPlayingColor()
    {
        MarbleColor partnerColor = getPartnerColor();
        if (allMarblesIn && (partnerColor != null)) 
             { return partnerColor; }
        else { return color; }
    }

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
