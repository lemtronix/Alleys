package controller;

import java.util.List;

import model.AlleysGame;
import model.Card;

public class Controller
{
    private AlleysGame _AlleysGame;

    public Controller()
    {
        _AlleysGame = new AlleysGame(4);
    }

    /// @pre Make sure to call setMarbleListener() before calling this function so that the game board gets initialized properly!
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

    public boolean currentPlayerPlays(Card playedCard, int marbleIdNumber)
    {
        return _AlleysGame.play(playedCard, marbleIdNumber);
    }

    public boolean currentPlayerPlaysJack(Card playedCard, int marbleToMove, int marbleToMoveTo)
    {
        return _AlleysGame.playJack(playedCard, marbleToMove, marbleToMoveTo);
    }

    /// @pre Should be called before calling begin
    public void setMarbleListener(MarbleModelListener marbleListener)
    {
        _AlleysGame.setMarbleListener(marbleListener);
    }

    public void skipPlayerTurn()
    {
        _AlleysGame.skipPlayerTurn();
    }

    public String getCurrentPlayersName()
    {
        return _AlleysGame.getCurrentPlayerName();
    }
}
