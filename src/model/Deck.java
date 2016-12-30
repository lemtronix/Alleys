package model;

import java.util.Arrays;

public class Deck
{
    public static final int MaxNumberOfCardsInDeck = 52;
    private final String baseDir = "images/";
    private final String suffix = ".gif";

    private Card[] _Deck = new Card[MaxNumberOfCardsInDeck];
    private boolean[] _CardDealt = new boolean[MaxNumberOfCardsInDeck];

    private int NumberOfCardsInDeck = MaxNumberOfCardsInDeck;

    // TODO: Move to DeckSpy
    private int RandomCardAttemptCounter = 0;

    public Deck()
    {
        // Clubs
        _Deck[0] = new Card(CardValue.Ace, CardSuit.Clubs, baseDir + "ac" + suffix);
        _Deck[1] = new Card(CardValue.Two, CardSuit.Clubs, baseDir + "2c" + suffix);
        _Deck[2] = new Card(CardValue.Three, CardSuit.Clubs, baseDir + "3c" + suffix);
        _Deck[3] = new Card(CardValue.Four, CardSuit.Clubs, baseDir + "4c" + suffix);
        _Deck[4] = new Card(CardValue.Five, CardSuit.Clubs, baseDir + "5c" + suffix);
        _Deck[5] = new Card(CardValue.Six, CardSuit.Clubs, baseDir + "6c" + suffix);
        _Deck[6] = new Card(CardValue.Seven, CardSuit.Clubs, baseDir + "7c" + suffix);
        _Deck[7] = new Card(CardValue.Eight, CardSuit.Clubs, baseDir + "8c" + suffix);
        _Deck[8] = new Card(CardValue.Nine, CardSuit.Clubs, baseDir + "9c" + suffix);
        _Deck[9] = new Card(CardValue.Ten, CardSuit.Clubs, baseDir + "tc" + suffix);
        _Deck[10] = new Card(CardValue.Jack, CardSuit.Clubs, baseDir + "jc" + suffix);
        _Deck[11] = new Card(CardValue.Queen, CardSuit.Clubs, baseDir + "qc" + suffix);
        _Deck[12] = new Card(CardValue.King, CardSuit.Clubs, baseDir + "kc" + suffix);

        // Diamonds
        _Deck[13] = new Card(CardValue.Ace, CardSuit.Diamonds, baseDir + "ad" + suffix);
        _Deck[14] = new Card(CardValue.Two, CardSuit.Diamonds, baseDir + "2d" + suffix);
        _Deck[15] = new Card(CardValue.Three, CardSuit.Diamonds, baseDir + "3d" + suffix);
        _Deck[16] = new Card(CardValue.Four, CardSuit.Diamonds, baseDir + "4d" + suffix);
        _Deck[17] = new Card(CardValue.Five, CardSuit.Diamonds, baseDir + "5d" + suffix);
        _Deck[18] = new Card(CardValue.Six, CardSuit.Diamonds, baseDir + "6d" + suffix);
        _Deck[19] = new Card(CardValue.Seven, CardSuit.Diamonds, baseDir + "7d" + suffix);
        _Deck[20] = new Card(CardValue.Eight, CardSuit.Diamonds, baseDir + "8d" + suffix);
        _Deck[21] = new Card(CardValue.Nine, CardSuit.Diamonds, baseDir + "9d" + suffix);
        _Deck[22] = new Card(CardValue.Ten, CardSuit.Diamonds, baseDir + "td" + suffix);
        _Deck[23] = new Card(CardValue.Jack, CardSuit.Diamonds, baseDir + "jd" + suffix);
        _Deck[24] = new Card(CardValue.Queen, CardSuit.Diamonds, baseDir + "qd" + suffix);
        _Deck[25] = new Card(CardValue.King, CardSuit.Diamonds, baseDir + "kd" + suffix);

        // Hearts
        _Deck[26] = new Card(CardValue.Ace, CardSuit.Hearts, baseDir + "ah" + suffix);
        _Deck[27] = new Card(CardValue.Two, CardSuit.Hearts, baseDir + "2h" + suffix);
        _Deck[28] = new Card(CardValue.Three, CardSuit.Hearts, baseDir + "3h" + suffix);
        _Deck[29] = new Card(CardValue.Four, CardSuit.Hearts, baseDir + "4h" + suffix);
        _Deck[30] = new Card(CardValue.Five, CardSuit.Hearts, baseDir + "5h" + suffix);
        _Deck[31] = new Card(CardValue.Six, CardSuit.Hearts, baseDir + "6h" + suffix);
        _Deck[32] = new Card(CardValue.Seven, CardSuit.Hearts, baseDir + "7h" + suffix);
        _Deck[33] = new Card(CardValue.Eight, CardSuit.Hearts, baseDir + "8h" + suffix);
        _Deck[34] = new Card(CardValue.Nine, CardSuit.Hearts, baseDir + "9h" + suffix);
        _Deck[35] = new Card(CardValue.Ten, CardSuit.Hearts, baseDir + "th" + suffix);
        _Deck[36] = new Card(CardValue.Jack, CardSuit.Hearts, baseDir + "jh" + suffix);
        _Deck[37] = new Card(CardValue.Queen, CardSuit.Hearts, baseDir + "qh" + suffix);
        _Deck[38] = new Card(CardValue.King, CardSuit.Hearts, baseDir + "kh" + suffix);

        // Spades
        _Deck[39] = new Card(CardValue.Ace, CardSuit.Spades, baseDir + "as" + suffix);
        _Deck[40] = new Card(CardValue.Two, CardSuit.Spades, baseDir + "2s" + suffix);
        _Deck[41] = new Card(CardValue.Three, CardSuit.Spades, baseDir + "3s" + suffix);
        _Deck[42] = new Card(CardValue.Four, CardSuit.Spades, baseDir + "4s" + suffix);
        _Deck[43] = new Card(CardValue.Five, CardSuit.Spades, baseDir + "5s" + suffix);
        _Deck[44] = new Card(CardValue.Six, CardSuit.Spades, baseDir + "6s" + suffix);
        _Deck[45] = new Card(CardValue.Seven, CardSuit.Spades, baseDir + "7s" + suffix);
        _Deck[46] = new Card(CardValue.Eight, CardSuit.Spades, baseDir + "8s" + suffix);
        _Deck[47] = new Card(CardValue.Nine, CardSuit.Spades, baseDir + "9s" + suffix);
        _Deck[48] = new Card(CardValue.Ten, CardSuit.Spades, baseDir + "ts" + suffix);
        _Deck[49] = new Card(CardValue.Jack, CardSuit.Spades, baseDir + "js" + suffix);
        _Deck[50] = new Card(CardValue.Queen, CardSuit.Spades, baseDir + "qs" + suffix);
        _Deck[51] = new Card(CardValue.King, CardSuit.Spades, baseDir + "ks" + suffix);

        shuffle();
    }

    public Card GetRandomCard()
    {
        Card CardToDeal = null;

        if (NumberOfCardsInDeck > 0)
        {
            while (CardToDeal == null)
            {
                int CardIndex = (int) (Math.random() * MaxNumberOfCardsInDeck);

                if (_CardDealt[CardIndex] == false)
                {
                    CardToDeal = _Deck[CardIndex];
                    _CardDealt[CardIndex] = true;
                    NumberOfCardsInDeck--;
                }

                RandomCardAttemptCounter++;
            }

            // System.out.println("Deck: There are " + NumberOfCardsInDeck + " cards left in deck.");
        }

        return CardToDeal;
    }

    @Deprecated
    public Card GetCardOfKnownValue(int cardNumber)
    {
        return _Deck[cardNumber];
    }

    public boolean AnyCardsLeft()
    {
        if (NumberOfCardsInDeck > 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void shuffle()
    {
        System.out.println("Deck: Shuffling deck...");

        NumberOfCardsInDeck = MaxNumberOfCardsInDeck;
        Arrays.fill(_CardDealt, false);
    }

    protected void ListCards()
    {
        int Count = 0;

        for (int i = 0; i < MaxNumberOfCardsInDeck; i++)
        {
            Count++;
            System.out.println(i + ".) " + _Deck[i].toString());
        }

        System.out.println(Count + " cards in the deck");
    }

    protected int GetNumberOfAttemptsToClearDeck()
    {
        return RandomCardAttemptCounter;
    }
}
