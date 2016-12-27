package model;

import java.util.List;

public class Dealer
{
    private List<Player> _Players;
    private Deck _Deck;

    private int _NumberOfPlayers = 0;
    private int _PlayerLeftOfDealer = 0;

    private int _CurrentArrayIndex = 0;
    private int _NumberOfPlayerIndex = 0;
    private int[][] _NumberOfCardsToDeal = { { 5, 5, 4, 4, 4, 4, 0 }, { 5, 4, 4, 4, 4, 0 }, { 5, 4, 4, 0 } };

    public Dealer(List<Player> players)
    {
        _Deck = new Deck();

        _Players = players;

        _NumberOfPlayers = players.size();

        // Index starts at 0, but the first player is the value of 2
        _NumberOfPlayerIndex = _NumberOfPlayers - 2;
    }

    // TODO need to change how cards are dealt; the dealer needs to start dealing to the player to the left of them?
    // TODO Dealer is determined by the player that draws the highest card. Ace high wins?
    public void deal()
    {
        // Get number of cards to deal
        int numberOfCardsToDeal = getNumberOfCardsToDeal();

        // If the index is at its max or the value is zero, TBD, then shuffle the cards and get the number of cards to deal again
        if (numberOfCardsToDeal == 0)
        {
            rotateDealerPosition();
            _Deck.shuffle();
            numberOfCardsToDeal = getNumberOfCardsToDeal();
        }

        System.out.println("Dealing " + numberOfCardsToDeal + " cards...");

        for (int i = 0; i < _NumberOfPlayers; i++)
        {
            Player player = _Players.get(i);

            // Add cards to the players hand
            for (int j = 0; j < numberOfCardsToDeal; j++)
            {
                Card randomCard = _Deck.GetRandomCard();

                if (randomCard != null)
                {
                    player.addCard(randomCard);
                }
                else
                {
                    System.out.println("Dealer: Deck needs to be shuffled!");
                }
            }
        }

        // Increment the index
        cardsDealt();
    }

    public int getPlayerLeftOfDealer()
    {
        return _PlayerLeftOfDealer;
    }

    private int getNumberOfCardsToDeal()
    {
        int numberOfCardsToDeal = _NumberOfCardsToDeal[_NumberOfPlayerIndex][_CurrentArrayIndex];

        return numberOfCardsToDeal;
    }

    private void cardsDealt()
    {
        _CurrentArrayIndex++;
    }

    private void rotateDealerPosition()
    {
        // New dealer!
        System.out.println("Dealer: Rotating Dealer...");

        _CurrentArrayIndex = 0;

        _PlayerLeftOfDealer++;

        if (_PlayerLeftOfDealer >= _NumberOfPlayers)
        {
            _PlayerLeftOfDealer = 0;
        }

    }
}
