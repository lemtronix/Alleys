package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import model.Deck;

public class DeckTest
{
    DeckSpy _Deck;
    
    @Before
    public void setUp()
    {
        _Deck = new DeckSpy();
    }
    
    @Test
    public void NumberOfCardsIs52()
    {
        assertEquals(52, Deck.MaxNumberOfCardsInDeck);
    }
    
    @Test
    public void AnyCardsLeftIsTrueOnCreation()
    {
        assertEquals(true, _Deck.AnyCardsLeft());
    }
    
    @Test
    public void GetRandomCardsFromDeckExhaustsInAcceptableTime()
    {
        final int ExpectedNumberOfAttemptsLimit = 400;

        while (_Deck.AnyCardsLeft())
        {
            _Deck.GetRandomCard();
        }

        boolean Result = (_Deck.GetNumberOfAttemptsToClearDeckSpy() <= ExpectedNumberOfAttemptsLimit);
        assertTrue("Deck exhausted in " + _Deck.GetNumberOfAttemptsToClearDeckSpy() + " attempts.", Result);
    }
}

