package controller;

import java.util.List;

import model.AlleysGame;
import model.Card;

public class Controller
{
    private AlleysGame _AlleysGame;
    
    public Controller()
    {
        _AlleysGame = new AlleysGame(1);
    }
    
    /// @pre Make sure to call setMarbleListener() before calling this function so that the game board gets initalized properly!
    public void begin()
    {
        _AlleysGame.begin();
    }
    
    public int GetMaxNumberOfMarbles()
    {
        return AlleysGame.MaxNumberOfMarbles;
    }
    
    public int GetMaxNumberOfSpots()
    {
        return AlleysGame.MaxNumberOfSpots;
    }
    
    public List<Card> getCurrentPlayersCards()
    {
        return _AlleysGame.getCurrentPlayersCards();
    }

    public void currentPlayerPlays(int marbleIdNumber, Card playedCard)
    {
        _AlleysGame.play(marbleIdNumber, playedCard);
    }
    
    /// @pre Should be called before calling begin
    public void setMarbleListener(MarbleListener marbleListener)
    {
        _AlleysGame.setMarbleListener(marbleListener);
    }
}
