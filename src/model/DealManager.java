package model;

import java.util.List;

public class DealManager
{
    private Deck deck;
    // arrays of different numbers of cards to be dealt, according to
    // how many people are in the game; we handle 2, 3, and 4, so the
    // index is (# people - 2). 0 at the end of each array indicates
    // the deck is gone and needs to be reshuffled.
    private int[][] _NumberOfCardsToDeal = { { 5, 5, 4, 4, 4, 4, 0 }, { 5, 4, 4, 4, 4, 0 }, { 5, 4, 4, 0 } };
    private int round;
    private Messager messager;
    
    private void message(String s) { messager.message(s); }
    private void message(String formatS, String... params) { messager.message(formatS, params); }

    public DealManager(Messager messager)
    {
        this.messager = messager;
        deck = new Deck();
        round = 0;
    }

    // TODO Dealer is determined by the player that draws the highest card. Ace high wins?
    // TODO rules actually state that red is the first dealer, but I can see this as a local
    // rule preference. The deal rotates no matter who starts it, so it shouldn't complicate the
    // logic much.
    public void deal(List<Player> players)
    {
        round++;
        
        // Get number of cards to deal
        int numberOfCardsToDeal = getNumberOfCardsToDeal(players.size(), round);

        // If the number is zero we've dealt all the cards in this shuffle;
        // shuffle the cards and get the number of cards to deal again
        if (numberOfCardsToDeal == 0)
        {
            deck.shuffle();
            message("info.newDeal");
            round = 1;
            numberOfCardsToDeal = getNumberOfCardsToDeal(players.size(), round);
        }

        message("info.dealing", Integer.toString(numberOfCardsToDeal));

        for (int i = 0; i < players.size(); i++)
        {
            Player player = players.get(i);

            // Add cards to the players hand
            for (int j = 0; j < numberOfCardsToDeal; j++)
            {
                Card randomCard = deck.GetRandomCard();

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
    }

    private int getNumberOfCardsToDeal(int numberOfPlayers, int roundNumber)
    {
        int numberOfCardsToDeal = _NumberOfCardsToDeal[numberOfPlayers - 2][roundNumber - 1];
        return numberOfCardsToDeal;
    }
}
