package model;

import java.util.Arrays;

public class Deck
{
    public static void say(String msg) { System.out.println(msg); }
    
    public static final int MaxNumberOfCardsInDeck = 52;

    private Card[] _Deck = new Card[MaxNumberOfCardsInDeck];
    private boolean[] _CardDealt = new boolean[MaxNumberOfCardsInDeck];

    private int NumberOfCardsInDeck = MaxNumberOfCardsInDeck;

    // TODO: Move to DeckSpy
    private int RandomCardAttemptCounter = 0;

    public Deck()
    {
        say("creating deck of cards");
        
        int deckIndex = 0;
        for (CardSuit suit : CardSuit.values())
        {
            for (CardValue value : CardValue.values())
            {
                _Deck[deckIndex++] = new Card(value, suit);
            }
        }

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
