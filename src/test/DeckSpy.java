package test;

import model.Deck;

public class DeckSpy extends Deck
{

    @Override
    public void ListCards()
    {
        super.ListCards();
    }

    @Override
    public int GetNumberOfAttemptsToClearDeck()
    {
        return super.GetNumberOfAttemptsToClearDeck();
    }
    
}
