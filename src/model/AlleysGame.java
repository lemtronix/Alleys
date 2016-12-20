package model;

import java.util.ArrayList;
import java.util.List;

import controller.MarbleListener;

public class AlleysGame
{
    public static final int MaxNumberOfMarbles = 16;
    public static final int MaxNumberOfSpots = 64;
    
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
            System.out.println("Invalid number of players specified. Please enter a number between " + MIN_NUMBER_OF_PLAYERS + " and " + MAX_NUMBER_OF_PLAYERS);
        }
        
        CreateSpots();
        CreateMarbles();
        
        _Players.add(0, new Player());

        // TODO test code
        // For testing, always give the King of Clubs
        _Players.get(0).addCard(_Deck.GetCardOfKnownValue(12));
        
        for (int i=0; i<4; i++)
        {
            _Players.get(0).addCard(_Deck.GetRandomCard());
        }
                
        // Assign marbles to a player
        _Marbles.get(0).setOwner(_Players.get(0));
        _Marbles.get(1).setOwner(_Players.get(0));
        _Marbles.get(2).setOwner(_Players.get(0));
        _Marbles.get(3).setOwner(_Players.get(0));
        
    }
    
    public static AlleysGame getWorld()
    {
        return _AlleysGame;
    }
    
    /** Sets up the game board with initial conditions
     *  @pre setMarbleListener should be called before calling begin.
     */
    public void begin()
    {
        // Set Home spot for marbles
        _Marbles.get(0).setHomeSpot(_Spots.get(0));
        _Marbles.get(1).setHomeSpot(_Spots.get(1));
        _Marbles.get(2).setHomeSpot(_Spots.get(2));
        _Marbles.get(3).setHomeSpot(_Spots.get(3));
        
        // Set Starting spot for marbles
        _Marbles.get(0).setStartingSpot(_Spots.get(17));
        _Marbles.get(1).setStartingSpot(_Spots.get(17));
        _Marbles.get(2).setStartingSpot(_Spots.get(17));
        _Marbles.get(3).setStartingSpot(_Spots.get(17));
        
        // Move marbles to their home spot
        _Marbles.get(0).moveToHomeSpot();
        _Marbles.get(1).moveToHomeSpot();
        _Marbles.get(2).moveToHomeSpot();
        _Marbles.get(3).moveToHomeSpot();
    }
    
    public void setMarbleListener(MarbleListener marbleListener)
    {
        for (int i=0; i<MaxNumberOfMarbles; i++)
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
        for (int i=0; i<MaxNumberOfSpots; i++)
        {
            _Spots.add(i, new Spot(i));
        }
    }
    
    private void CreateMarbles()
    {
        for (int i=0; i<MaxNumberOfMarbles; i++)
        {
            _Marbles.add(i, new Marble(i));
        }
    }
}
