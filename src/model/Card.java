package model;

public class Card { 
    private CardValue _CardValue;
    private CardSuit _CardSuit;
    
    public Card(CardValue Value, CardSuit Suit)
    {
        _CardValue = Value;
        _CardSuit = Suit;
    }
    
    public String toString()
    {
        return "" + _CardValue + " of " + _CardSuit;
    }
}
