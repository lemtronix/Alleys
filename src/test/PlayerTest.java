package test;

import static org.junit.Assert.assertEquals;
import model.Card;
import model.Deck;
import model.Player;

import org.junit.Before;
import org.junit.Test;

public class PlayerTest
{
    Player _Player;
    Deck _Deck;
    
    @Before
    public void setUp()
    {
        _Player = new Player();
        _Deck = new Deck();
    }
    
    @Test
    public void NumberOfCardsOnCreationIsZero()
    {
        assertEquals(0, _Player.GetNumberOfCardsInHand());
    }
    
    @Test
    public void AddingNullCardObjectDoesNotError()
    {
        _Player.AddCard(null);
        assertEquals(0, _Player.GetNumberOfCardsInHand());
    }
    
    @Test
    public void GettingACardFromAnEmptyHandReturnsNull()
    {
        assertEquals(null, _Player.GetCardAtIndex(0));
        assertEquals(null, _Player.GetCardAtIndex(-1));
        assertEquals(null, _Player.GetCardAtIndex(1));
    }
    
    @Test
    public void GetCardSuccessful()
    {
        Card _NewCard = _Deck.GetRandomCard();
        _Player.AddCard(_NewCard);
        
        assertEquals(1, _Player.GetNumberOfCardsInHand());
        assertEquals(_NewCard, _Player.GetCardAtIndex(0));
    }
    
    @Test
    public void GetCardAtWrongIndexDoesNotError()
    {
        Card _NewCard = _Deck.GetRandomCard();
        _Player.AddCard(_NewCard);
        
        assertEquals(1, _Player.GetNumberOfCardsInHand());
        assertEquals(null, _Player.GetCardAtIndex(1));
    }
    
    @Test
    public void AddTwoCardsReturnsSizeOfTwoAndInProperOrder()
    {
        Card _Card1 = _Deck.GetRandomCard();
        Card _Card2 = _Deck.GetRandomCard();
        
        _Player.AddCard(_Card1);
        _Player.AddCard(_Card2);
        
        assertEquals(2, _Player.GetNumberOfCardsInHand());
        assertEquals(_Card1, _Player.GetCardAtIndex(0));
        assertEquals(_Card2, _Player.GetCardAtIndex(1));
    }
    
    @Test
    public void RemovingTwoCardsThenReturnsNull()
    {        
        _Player.AddCard(_Deck.GetRandomCard());
        _Player.AddCard(_Deck.GetRandomCard());
        
        _Player.RemoveCardAtIndex(1);
        _Player.RemoveCardAtIndex(0);
        
        assertEquals(null, _Player.GetCardAtIndex(0));
        assertEquals(null, _Player.GetCardAtIndex(1));
    }
    
    @Test
    public void RemoveCardSuccess()
    {
        Card _Card1 = _Deck.GetRandomCard();
        Card _Card2 = _Deck.GetRandomCard();
        
        _Player.AddCard(_Card1);
        _Player.AddCard(_Card2);
        
        assertEquals(2, _Player.GetNumberOfCardsInHand());
        
        _Player.RemoveCard(_Card1);
        assertEquals(1, _Player.GetNumberOfCardsInHand());
        
        _Player.RemoveCard(_Card2);
        assertEquals(0, _Player.GetNumberOfCardsInHand());
    }
}
