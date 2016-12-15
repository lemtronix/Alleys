package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import model.Deck;

public class DeckTest
{
    
    @Test
    public void NumberOfCardsIs52()
    {
        assertEquals(52, Deck.MaxNumberOfCardsInDeck);
    }
    
    @Test
    public void AnyCardsLeftIsTrueOnCreation()
    {
        Deck _NewDeck = new Deck();
        assertEquals(true, _NewDeck.AnyCardsLeft());
    }
    
    @Test
    public void GetRandomCardsFromDeckExhaustsInAcceptableTime()
    {
        final int ExpectedNumberOfAttemptsLimit = 350;
        
        DeckSpy _Deck = new DeckSpy();

        while (_Deck.AnyCardsLeft())
        {
            _Deck.GetRandomCard();
        }

        boolean Result = (_Deck.GetNumberOfAttemptsToClearDeck() <= ExpectedNumberOfAttemptsLimit);
        assertTrue("Deck exhausted in " + _Deck.GetNumberOfAttemptsToClearDeck() + " attempts.", Result);
    }
}

