package test;

import model.Deck;

public class DeckSpy extends Deck
{

    public void ListCardsSpy()
    {
        super.ListCards();
    }

    public int GetNumberOfAttemptsToClearDeckSpy()
    {
        return super.GetNumberOfAttemptsToClearDeck();
    }
    
}
