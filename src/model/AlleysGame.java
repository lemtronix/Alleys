package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import controller.MarbleListener;

public class AlleysGame
{
    public static final int MaxNumberOfMarbles = 16;
    public static final int MaxNumberOfSpots = 96;

    public static final int FirstBoardSpot = 32;
    public static final int TotalNumberOfFinishSpots = 4;
    public static final int TotalNumberOfBoardSpots = 64;

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

        CreatePlayers(NumberOfPlayers);

        LinkMarblesToSpotsAndPlayers();
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

        // Move marbles to their home spot
        _Marbles.get(0).setState(new MarbleStateHome());
        _Marbles.get(1).setState(new MarbleStateHome());
        _Marbles.get(2).setState(new MarbleStateHome());
        _Marbles.get(3).setState(new MarbleStateHome());

        _Marbles.get(4).setState(new MarbleStateHome());
        _Marbles.get(5).setState(new MarbleStateHome());
        _Marbles.get(6).setState(new MarbleStateHome());
        _Marbles.get(7).setState(new MarbleStateHome());

        _Marbles.get(8).setState(new MarbleStateHome());
        _Marbles.get(9).setState(new MarbleStateHome());
        _Marbles.get(10).setState(new MarbleStateHome());
        _Marbles.get(11).setState(new MarbleStateHome());

        _Marbles.get(12).setState(new MarbleStateHome());
        _Marbles.get(13).setState(new MarbleStateHome());
        _Marbles.get(14).setState(new MarbleStateHome());
        _Marbles.get(15).setState(new MarbleStateHome());
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

        if (player.getMarblesNeededToFinish() == 0)
        {
            System.out.println("Player " + player.getName() + " has won the game!");
        }

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

    private void LinkMarblesToSpotsAndPlayers()
    {
        final int player0Index = 0;
        final int player1Index = 1;
        final int player2Index = 2;
        final int player3Index = 3;

        final int player0StartingSpot = 32;
        final int player1StartingSpot = 48;
        final int player2StartingSpot = 64;
        final int player3StartingSpot = 80;

        // Set Home spot for marbles
        for (int i = 0; i < MaxNumberOfMarbles; i++)
        {
            _Marbles.get(i).setHomeSpot(_Spots.get(i));
            ;
        }

        // Set Starting spot for marbles
        _Marbles.get(0).setStartingSpot(_Spots.get(player0StartingSpot));
        _Marbles.get(1).setStartingSpot(_Spots.get(player0StartingSpot));
        _Marbles.get(2).setStartingSpot(_Spots.get(player0StartingSpot));
        _Marbles.get(3).setStartingSpot(_Spots.get(player0StartingSpot));

        _Marbles.get(4).setStartingSpot(_Spots.get(player1StartingSpot));
        _Marbles.get(5).setStartingSpot(_Spots.get(player1StartingSpot));
        _Marbles.get(6).setStartingSpot(_Spots.get(player1StartingSpot));
        _Marbles.get(7).setStartingSpot(_Spots.get(player1StartingSpot));

        _Marbles.get(8).setStartingSpot(_Spots.get(player2StartingSpot));
        _Marbles.get(9).setStartingSpot(_Spots.get(player2StartingSpot));
        _Marbles.get(10).setStartingSpot(_Spots.get(player2StartingSpot));
        _Marbles.get(11).setStartingSpot(_Spots.get(player2StartingSpot));

        _Marbles.get(12).setStartingSpot(_Spots.get(player3StartingSpot));
        _Marbles.get(13).setStartingSpot(_Spots.get(player3StartingSpot));
        _Marbles.get(14).setStartingSpot(_Spots.get(player3StartingSpot));
        _Marbles.get(15).setStartingSpot(_Spots.get(player3StartingSpot));

        // Assign marbles to a player
        _Marbles.get(0).setOwner(_Players.get(player0Index));
        _Marbles.get(1).setOwner(_Players.get(player0Index));
        _Marbles.get(2).setOwner(_Players.get(player0Index));
        _Marbles.get(3).setOwner(_Players.get(player0Index));

        _Marbles.get(4).setOwner(_Players.get(player1Index));
        _Marbles.get(5).setOwner(_Players.get(player1Index));
        _Marbles.get(6).setOwner(_Players.get(player1Index));
        _Marbles.get(7).setOwner(_Players.get(player1Index));

        _Marbles.get(8).setOwner(_Players.get(player2Index));
        _Marbles.get(9).setOwner(_Players.get(player2Index));
        _Marbles.get(10).setOwner(_Players.get(player2Index));
        _Marbles.get(11).setOwner(_Players.get(player2Index));

        _Marbles.get(12).setOwner(_Players.get(player3Index));
        _Marbles.get(13).setOwner(_Players.get(player3Index));
        _Marbles.get(14).setOwner(_Players.get(player3Index));
        _Marbles.get(15).setOwner(_Players.get(player3Index));

    }

    private void CreatePlayers(int numberOfPlayers)
    {
        // Create the player's finish spots
        Spot[] Player0FinishSpots = { _Spots.get(16), _Spots.get(17), _Spots.get(18), _Spots.get(19) };
        Spot[] Player1FinishSpots = { _Spots.get(20), _Spots.get(21), _Spots.get(22), _Spots.get(23) };
        Spot[] Player2FinishSpots = { _Spots.get(24), _Spots.get(25), _Spots.get(26), _Spots.get(27) };
        Spot[] Player3FinishSpots = { _Spots.get(28), _Spots.get(29), _Spots.get(30), _Spots.get(31) };

        ArrayList<Spot> player0FinishSpots = new ArrayList<Spot>(Arrays.asList(Player0FinishSpots));
        ArrayList<Spot> player1FinishSpots = new ArrayList<Spot>(Arrays.asList(Player1FinishSpots));
        ArrayList<Spot> player2FinishSpots = new ArrayList<Spot>(Arrays.asList(Player2FinishSpots));
        ArrayList<Spot> player3FinishSpots = new ArrayList<Spot>(Arrays.asList(Player3FinishSpots));

        _Players.add(0, new Player(player0FinishSpots));
        _Players.add(1, new Player(player1FinishSpots));
        _Players.add(2, new Player(player2FinishSpots));
        _Players.add(3, new Player(player3FinishSpots));

        // TODO test code
        // For testing, always give the King of Clubs, a 4 and a Queen
        _Players.get(0).addCard(_Deck.GetCardOfKnownValue(12));
        _Players.get(0).addCard(_Deck.GetCardOfKnownValue(3));
        _Players.get(0).addCard(_Deck.GetCardOfKnownValue(11));
        _Players.get(0).addCard(_Deck.GetCardOfKnownValue(0));

        for (int i = 0; i < 4; i++)
        {
            _Players.get(0).addCard(_Deck.GetRandomCard());
        }
    }
}
