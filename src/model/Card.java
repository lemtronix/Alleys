package model;

public class Card
{
    private CardValue _CardValue;
    private CardSuit _CardSuit;

    public Card(CardValue Value, CardSuit Suit) 
    {
        _CardValue = Value;
        _CardSuit = Suit;
    }

    public CardValue getRank()    {        return _CardValue;    }

    /**
     * return a string unique to this card across the deck.
     * @return
     */
    public String getIndex() { return _CardValue.getIndex() + _CardSuit.getIndex(); }
    
    /**
     * return the numeric value of this card; see CardValue
     * @return
     */
    public int toInt()
    {
        return _CardValue.getValue();
    }

    /**
     * return the name and suit of this card.
     */
    public String toString()
    {
        return "" + _CardValue.name() + " of " + _CardSuit.name();
    }
}
