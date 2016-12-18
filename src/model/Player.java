package model;

import java.util.ArrayList;
import java.util.Iterator;

public class Player
{
    // At most player can have 5 cards
    @Deprecated
    public static final int MaxNumberOfCards = 5;
    
    private ArrayList<Card> _CardsInHand = new ArrayList<Card>();
    
    // They need to know what kind of card they have?
    // Need to be able to play a card
    // 
    // Always has 4 marbles, which marbles are they?
    // Need to be able to move their own marble.
    
    // 
    
    public Player()
    {
        
    }
    
    public void AddCard(Card DealtCard)
    {
        if (DealtCard != null)
        {
            _CardsInHand.add(DealtCard);
        }
    }
    
    @Deprecated
    public Card GetCardAtIndex(int Index)
    {
        Card _CardToReturn = null;
        
        if (_CardsInHand.size() != 0)
        {
            try
            {
                _CardToReturn = _CardsInHand.get(Index);
            } 
            catch (IndexOutOfBoundsException e)
            {
                System.err.println("GetCardAtIndex specified an invalid index.");
            }
        }
        
        return _CardToReturn;
    }
    
    @Deprecated
    public void RemoveCardAtIndex(int Index)
    {
        if (_CardsInHand.size() != 0)
        {
            try
            {
                _CardsInHand.remove(Index);
            }
            catch (IndexOutOfBoundsException e)
            {
                System.err.println("GetCardAtIndex specified an invalid index.");
            }
        }
    }
    
    public void RemoveCard(Card CardToRemove)
    {
        if (_CardsInHand.size() != 0)
        {
            _CardsInHand.remove(CardToRemove);
        }
    }
    
    public int GetNumberOfCardsInHand()
    {
        return _CardsInHand.size();
    }
    
    public Iterator<Card> GetCardIterator()
    {
        return _CardsInHand.iterator();
    }
}
