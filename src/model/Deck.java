package model;

import java.util.Arrays;

public class Deck {
    public static final int MaxNumberOfCardsInDeck = 52;
    
    Card[] _Deck = new Card[MaxNumberOfCardsInDeck];
    boolean[] _CardDealt = new boolean[MaxNumberOfCardsInDeck];
    
    int NumberOfCardsInDeck = MaxNumberOfCardsInDeck;
    int RandomCardAttemptCounter = 0;
    
    public Deck()
    {
        // Mark the array as 
        Arrays.fill(_CardDealt, false);
        
        // Clubs
        _Deck[0] = new Card(CardValue.Ace, CardSuit.Clubs);
        _Deck[1] = new Card(CardValue.Two, CardSuit.Clubs);
        _Deck[2] = new Card(CardValue.Three, CardSuit.Clubs);
        _Deck[3] = new Card(CardValue.Four, CardSuit.Clubs);
        _Deck[4] = new Card(CardValue.Five, CardSuit.Clubs);
        _Deck[5] = new Card(CardValue.Six, CardSuit.Clubs);
        _Deck[6] = new Card(CardValue.Seven, CardSuit.Clubs);
        _Deck[7] = new Card(CardValue.Eight, CardSuit.Clubs);
        _Deck[8] = new Card(CardValue.Nine, CardSuit.Clubs);
        _Deck[9] = new Card(CardValue.Ten, CardSuit.Clubs);
        _Deck[10] = new Card(CardValue.Jack, CardSuit.Clubs);
        _Deck[11] = new Card(CardValue.Queen, CardSuit.Clubs);
        _Deck[12] = new Card(CardValue.King, CardSuit.Clubs);
        
        // Diamonds
        _Deck[13] = new Card(CardValue.Ace, CardSuit.Diamonds);
        _Deck[14] = new Card(CardValue.Two, CardSuit.Diamonds);
        _Deck[15] = new Card(CardValue.Three, CardSuit.Diamonds);
        _Deck[16] = new Card(CardValue.Four, CardSuit.Diamonds);
        _Deck[17] = new Card(CardValue.Five, CardSuit.Diamonds);
        _Deck[18] = new Card(CardValue.Six, CardSuit.Diamonds);
        _Deck[19] = new Card(CardValue.Seven, CardSuit.Diamonds);
        _Deck[20] = new Card(CardValue.Eight, CardSuit.Diamonds);
        _Deck[21] = new Card(CardValue.Nine, CardSuit.Diamonds);
        _Deck[22] = new Card(CardValue.Ten, CardSuit.Diamonds);
        _Deck[23] = new Card(CardValue.Jack, CardSuit.Diamonds);
        _Deck[24] = new Card(CardValue.Queen, CardSuit.Diamonds);
        _Deck[25] = new Card(CardValue.King, CardSuit.Diamonds);
        
        // Hearts
        _Deck[26] = new Card(CardValue.Ace, CardSuit.Hearts);
        _Deck[27] = new Card(CardValue.Two, CardSuit.Hearts);
        _Deck[28] = new Card(CardValue.Three, CardSuit.Hearts);
        _Deck[29] = new Card(CardValue.Four, CardSuit.Hearts);
        _Deck[30] = new Card(CardValue.Five, CardSuit.Hearts);
        _Deck[31] = new Card(CardValue.Six, CardSuit.Hearts);
        _Deck[32] = new Card(CardValue.Seven, CardSuit.Hearts);
        _Deck[33] = new Card(CardValue.Eight, CardSuit.Hearts);
        _Deck[34] = new Card(CardValue.Nine, CardSuit.Hearts);
        _Deck[35] = new Card(CardValue.Ten, CardSuit.Hearts);
        _Deck[36] = new Card(CardValue.Jack, CardSuit.Hearts);
        _Deck[37] = new Card(CardValue.Queen, CardSuit.Hearts);
        _Deck[38] = new Card(CardValue.King, CardSuit.Hearts);
        
        // Spades
        _Deck[39] = new Card(CardValue.Ace, CardSuit.Spades);
        _Deck[40] = new Card(CardValue.Two, CardSuit.Spades);
        _Deck[41] = new Card(CardValue.Three, CardSuit.Spades);
        _Deck[42] = new Card(CardValue.Four, CardSuit.Spades);
        _Deck[43] = new Card(CardValue.Five, CardSuit.Spades);
        _Deck[44] = new Card(CardValue.Six, CardSuit.Spades);
        _Deck[45] = new Card(CardValue.Seven, CardSuit.Spades);
        _Deck[46] = new Card(CardValue.Eight, CardSuit.Spades);
        _Deck[47] = new Card(CardValue.Nine, CardSuit.Spades);
        _Deck[48] = new Card(CardValue.Ten, CardSuit.Spades);
        _Deck[49] = new Card(CardValue.Jack, CardSuit.Spades);
        _Deck[50] = new Card(CardValue.Queen, CardSuit.Spades);
        _Deck[51] = new Card(CardValue.King, CardSuit.Spades);
    }
    
    public void ListCards()
    {
        int Count = 0;
        
        for (int i=0; i<MaxNumberOfCardsInDeck; i++)
        {
            Count++;
            System.out.println(_Deck[i].toString());
        }
        
        System.out.println(Count + " cards in the deck");
    }
    
    public Card GetRandomCard()
    {
        Card CardToDeal = null;
        
        if (NumberOfCardsInDeck > 0)
        {
            while (CardToDeal == null)
            {
                int CardIndex = (int)(Math.random()*MaxNumberOfCardsInDeck);
                
                if (_CardDealt[CardIndex] == false)
                {
                    CardToDeal = _Deck[CardIndex];
                    _CardDealt[CardIndex] = true;
                    NumberOfCardsInDeck--;
                }
                
                RandomCardAttemptCounter++;
            }
        }
        
        return CardToDeal;
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
    
    public int GetNumberOfAttemptsToClearDeck()
    {
        return RandomCardAttemptCounter;
    }
}
