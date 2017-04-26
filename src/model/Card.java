package model;

public class Card
{
    private static final int ORDINAL_OFFSET = 1;
    private CardValue _CardValue;
    private CardSuit _CardSuit;

    public Card(CardValue Value, CardSuit Suit) 
    {
        _CardValue = Value;
        _CardSuit = Suit;
    }

    public CardValue getRank()
    {
        return _CardValue;
    }

    public String getIndex() { return _CardValue.getIndex() + _CardSuit.getIndex(); }
    
    public int toInt(boolean useAbsoluteValue)
    {
        int CardValue = -1;

        switch (_CardValue)
        {
            case Four:
                if (useAbsoluteValue == false)
                {
                    CardValue = -4;
                }
                else
                {
                    CardValue = 4;
                }
            break;
            case Jack:
                CardValue = -1;
            break;
            case Queen:
                CardValue = 12;
            break;
            case King:
                CardValue = 0;
            break;
            default:
                CardValue = _CardValue.ordinal() + ORDINAL_OFFSET;
            break;
        }

        return CardValue;
    }

    public String toString()
    {
        return "" + _CardValue.name() + " of " + _CardSuit;
    }
}
