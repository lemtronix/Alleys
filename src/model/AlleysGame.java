package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import controller.MarbleListener;

public class AlleysGame
{
    public static final int MaxNumberOfMarbles = 16;
    public static final int MaxNumberOfSpots = 96;
    public static final int FirstMoveableSpot = 32;

    private static final int MIN_NUMBER_OF_PLAYERS = 1;
    private static final int MAX_NUMBER_OF_PLAYERS = 4;

    private static AlleysGame _AlleysGame;
    private int _CurrentPlayersTurn = 0;

    ArrayList<Spot> _Spots = new ArrayList<Spot>();
    ArrayList<Marble> _Marbles = new ArrayList<Marble>();
    ArrayList<Player> _Players = new ArrayList<Player>();

    Deck _Deck;

    public AlleysGame(int NumberOfPlayers)
    {
        _AlleysGame = this;

        _Deck = new Deck();

        if (NumberOfPlayers < MIN_NUMBER_OF_PLAYERS && NumberOfPlayers > MAX_NUMBER_OF_PLAYERS)
        {
            System.out.println("Invalid number of players specified. Please enter a number between " + MIN_NUMBER_OF_PLAYERS + " and "
                    + MAX_NUMBER_OF_PLAYERS);
        }

        CreateSpots();
        CreateMarbles();

        // Create the player's finish spots
        Spot[] Player0FinishSpots = { _Spots.get(16), _Spots.get(17), _Spots.get(18), _Spots.get(19) };

        ArrayList<Spot> player0FinishSpots = new ArrayList<Spot>(Arrays.asList(Player0FinishSpots));

        _Players.add(0, new Player(player0FinishSpots));

        // TODO test code
        // For testing, always give the King of Clubs, a 4 and a Queen
        _Players.get(0).addCard(_Deck.GetCardOfKnownValue(12));
        _Players.get(0).addCard(_Deck.GetCardOfKnownValue(3));
        _Players.get(0).addCard(_Deck.GetCardOfKnownValue(11));

        for (int i = 0; i < 4; i++)
        {
            _Players.get(0).addCard(_Deck.GetRandomCard());
        }

        // Assign marbles to a player
        _Marbles.get(0).setOwner(_Players.get(0));
        _Marbles.get(1).setOwner(_Players.get(0));
        _Marbles.get(2).setOwner(_Players.get(0));
        _Marbles.get(3).setOwner(_Players.get(0));

        _Marbles.get(4).setOwner(_Players.get(0));
        _Marbles.get(5).setOwner(_Players.get(0));
        _Marbles.get(6).setOwner(_Players.get(0));
        _Marbles.get(7).setOwner(_Players.get(0));

    }

    public static AlleysGame getWorld()
    {
        return _AlleysGame;
    }

    /**
     * Sets up the game board with initial conditions
     * 
     * @pre setMarbleListener should be called before calling begin.
     */
    public void begin()
    {
        // Set Home spot for marbles
        _Marbles.get(0).setHomeSpot(_Spots.get(0));
        _Marbles.get(1).setHomeSpot(_Spots.get(1));
        _Marbles.get(2).setHomeSpot(_Spots.get(2));
        _Marbles.get(3).setHomeSpot(_Spots.get(3));

        // Set Starting spot for marbles
        _Marbles.get(0).setStartingSpot(_Spots.get(32));
        _Marbles.get(1).setStartingSpot(_Spots.get(32));
        _Marbles.get(2).setStartingSpot(_Spots.get(32));
        _Marbles.get(3).setStartingSpot(_Spots.get(32));

        // Move marbles to their home spot
        _Marbles.get(0).setState(new MarbleStateHome());
        _Marbles.get(1).setState(new MarbleStateHome());
        _Marbles.get(2).setState(new MarbleStateHome());
        _Marbles.get(3).setState(new MarbleStateHome());

        // Add some competitive marbles
        _Marbles.get(4).setHomeSpot(_Spots.get(4));
        _Marbles.get(5).setHomeSpot(_Spots.get(5));
        _Marbles.get(6).setHomeSpot(_Spots.get(6));
        _Marbles.get(7).setHomeSpot(_Spots.get(7));

        _Marbles.get(4).setStartingSpot(_Spots.get(48));
        _Marbles.get(5).setStartingSpot(_Spots.get(48));
        _Marbles.get(6).setStartingSpot(_Spots.get(48));
        _Marbles.get(7).setStartingSpot(_Spots.get(48));

        _Marbles.get(4).setState(new MarbleStateHome());
        _Marbles.get(5).setState(new MarbleStateHome());
        _Marbles.get(6).setState(new MarbleStateHome());
        _Marbles.get(7).setState(new MarbleStateHome());
    }

    public void setMarbleListener(MarbleListener marbleListener)
    {
        for (int i = 0; i < MaxNumberOfMarbles; i++)
        {
            // Add marble listener to the marble
            _Marbles.get(i).setMarbleListener(marbleListener);
        }
    }

    public List<Card> getCurrentPlayersCards()
    {
        return _Players.get(_CurrentPlayersTurn).getCards();
    }

    public boolean play(int marbleIdNumber, Card playedCard)
    {
        Player player = _Players.get(_CurrentPlayersTurn);
        Marble marble = _Marbles.get(marbleIdNumber);

        player.play(playedCard, marble);

        return true;
    }

    private void CreateSpots()
    {
        for (int i = 0; i < MaxNumberOfSpots; i++)
        {
            _Spots.add(i, new Spot(i));
        }
    }

    private void CreateMarbles()
    {
        for (int i = 0; i < MaxNumberOfMarbles; i++)
        {
            _Marbles.add(i, new Marble(i));
        }
    }
}
