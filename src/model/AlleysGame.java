package model;

import java.util.ArrayList;
import java.util.List;

import view.viewinterface.AlleysUI;
import controller.Constants;
import controller.MarbleModelListener;

public class AlleysGame implements Messager
{
    public static final int MaxNumberOfMarbles = Constants.TOTAL_MARBLES;
    public static final int MaxNumberOfSpots = Constants.TOTAL_SPOTS; // 96;

    public static final int FirstBoardSpot = 32;
    public static final int TotalNumberOfFinishSpots = 4;
    public static final int TotalNumberOfBoardSpots = 64;

    private static final int MIN_NUMBER_OF_PLAYERS = 2;
    private static final int MAX_NUMBER_OF_PLAYERS = 4;
    
    private static void say(String msg) { System.out.println(msg); }

    private AlleysUI        alleysUI;
    private Board           board;          public Board getBoard() { return board; }
    private TurnManager     turnManager;
//    public static Board getBoard() { return board; }

    private int _NumberOfPlayers = 0;
    private int _CurrentPlayersTurn = 0;

    private ArrayList<Player> _Players = new ArrayList<Player>();

//    private DealManager _Dealer;
    
    public static AlleysGame getNewInstance()
    {
        return new AlleysGame();
    }

    public AlleysGame() 
    {
        board = new Board(this);
        board.initializeBoard();
    }
    
    public void setAlleysUI(AlleysUI alleysUI)
    {
        this.alleysUI = alleysUI;
        board.setAlleysUI(alleysUI);
    }

    public AlleysUI getAlleysUI()
    {
        return alleysUI;
    }
    
    public void startGame(List<Player> players)
    {
        turnManager = new TurnManager(this, board, players);
        board.setUpMarbles(players);
        startNewTurn();
    }
    
    private void startNewTurn() 
    {
        Player player = turnManager.startNewTurn();
        alleysUI.startTurn(player);
    }

    public void cardChosen(Card card)    
    { 
        say("chose " + card.toString());
        Turn currentTurn = turnManager.getCurrentTurn();
        currentTurn.setCard(card, this);    // TODO: figure a way to do this without passing in the messager
    }

    public void marbleChosen(int marbleSpotIndex)
    {
        Spot marbleSpot = board.getSpot(marbleSpotIndex);
        Turn currentTurn = turnManager.getCurrentTurn();
        if (marbleSpot == null || currentTurn == null) { throw new InternalLogicError("either marbleSpot or currentTurn null at start of AlleysGame.marbleChosen"); }
        
        if (currentTurn.getCard() == null)
        {
            message("error.chooseCardFirst");
        }
        else
        {
            
            currentTurn = currentTurn.validateMove(board, marbleSpot); // board.validateChosenMarble(currentTurn, marbleSpot);
            
            MoveState moveState = currentTurn.getMoveState();
            TurnState turnState = moveState.getTurnState();
            switch(turnState)
            {
            case ERROR:
                // problem -- doing things this way doesn't allow parameters to the
                // message; perhaps we should put the error *message* into the current
                // turn, instead of just its state, or along with its state.
                // Alternately, we could make a class to hold moveState and its parameters
                // and return that, or just put parameters in the currentTurn with its
                // move state.
                message(moveState.getMessageKey());
                currentTurn.clearMarbles();
                break;
            case CONTINUING:
                message(moveState.getMessageKey());
                break;
            case READY:
                // if we were ever to implement a confirmation of a move -- say,
                // on a mobile device where it might be too easy to select the 
                // wrong thing, and we wanted to give the user a chance to cancel
                // before the move was made -- here is where we could get confirmation
                // before execution.
                board.executeTurn(currentTurn);
                startNewTurn();
                break;
            case START:
                throw new InternalLogicError("Shouldn't get START state after marble is chosen");
            }
        }
    }
    
    public void cardsFolded()
    {
        Turn currentTurn = turnManager.getCurrentTurn();
        Player player = currentTurn.getPlayer();
        player.foldCards();
        message("info.playerFolded", player.toString());
        startNewTurn();
    }

    public void message(String s) { alleysUI.message(s); }
    public void message(String format, String ... parameterValues) { alleysUI.message(format, parameterValues); }

    public String getCurrentPlayerName()
    {
        String playerName = _Players.get(_CurrentPlayersTurn).getName();

        if (playerName == "")
        {
            // No name provided, use the default
            playerName = "Player " + _CurrentPlayersTurn;
        }

        return playerName;
    }

//    public List<Card> getCurrentPlayersCards()
//    {
//        int numberOfSkippedPlayers = 0;
//        Player currentPlayer = _Players.get(_CurrentPlayersTurn);
//
//        // Check and make sure the player has cards to play, otherwise skip their turn
//        while (currentPlayer.getCards().size() == 0 && numberOfSkippedPlayers != _NumberOfPlayers)
//        {
//            nextPlayer();
//            numberOfSkippedPlayers++;
//            currentPlayer = _Players.get(_CurrentPlayersTurn);
//        }
//
//        // If we've skipped all the players, then we need to deal more cards
//        if (numberOfSkippedPlayers == _NumberOfPlayers)
//        {
//            // We need to deal more cards
//            _Dealer.deal();
//            _CurrentPlayersTurn = _Dealer.getPlayerLeftOfDealer();
//            currentPlayer = _Players.get(_CurrentPlayersTurn);
//        }
//
//        return currentPlayer.getCards();
//    }

//    public void skipPlayerTurn()
//    {
//        _Players.get(_CurrentPlayersTurn).foldCards();
//        nextPlayer();
//    }

    // TODO might be able to propigate the information in playSeven into this method
//    public boolean play(Card playedCard, int marbleIdNumber, boolean splitSeven)
//    {
//        boolean playSuccessful = false;
//
//        Player player = _Players.get(_CurrentPlayersTurn);
////        Marble marble = _Marbles.get(marbleIdNumber);
//
////        playSuccessful = player.play(playedCard, marble, splitSeven);
//
//        if (player.getMarblesNeededToFinish() == 0)
//        {
//            System.out.println("Player " + player.getName() + " has won the game!");
//
//            // TODO would you like to play again?
//        }
//
//        if (playSuccessful == true)
//        {
//            nextPlayer();
//        }
//
//        return playSuccessful;
//    }

    public boolean playJack(Card playedCard, int marbleIdToMove, int marbleIdToMoveTo)
    {
        boolean playSuccessful = false;
//
//        Player player = _Players.get(_CurrentPlayersTurn);
//        Marble marbleToMove = _Marbles.get(marbleIdToMove);
//        Marble marbleToMoveTo = _Marbles.get(marbleIdToMoveTo);
//
//        playSuccessful = player.playJack(playedCard, marbleToMove, marbleToMoveTo);
//
//        if (playSuccessful == true)
//        {
//            nextPlayer();
//        }
//
        return playSuccessful;
    }

    public boolean playSeven(Card playedCard, int firstMarbleId, int numberOfSpotsToMoveFirstMarble, int secondMarbleId)
    {
        return false;
//        if (playedCard.getRank() != CardValue.Seven)
//        {
//            System.out.println("Player: Seven not played.");
//            return false;
//        }
//
//        if (numberOfSpotsToMoveFirstMarble > 7)
//        {
//            System.out.println("Player: " + numberOfSpotsToMoveFirstMarble + " is greater than 7!");
//            return false;
//        }
//
//        boolean splitSeven = false;
//
//        if (numberOfSpotsToMoveFirstMarble == 7)
//        {
//            // Player wants to use all 7 spots on a single marble, this is a standard move
//            splitSeven = false;
//            return play(playedCard, firstMarbleId, false);
//        }
//        else
//        {
//            splitSeven = true;
//            int numberOfSpotsToMoveSecondMarble = 7 - numberOfSpotsToMoveFirstMarble;
//
//            Player player = _Players.get(_CurrentPlayersTurn);
//            Marble firstMarble = _Marbles.get(firstMarbleId);
//            Marble secondMarble = _Marbles.get(secondMarbleId);
//
//            Card firstCard = _Dealer.GetCardOfKnownValue(numberOfSpotsToMoveFirstMarble - 1);
//            Card secondCard = _Dealer.GetCardOfKnownValue(numberOfSpotsToMoveSecondMarble - 1);
//
//            MarbleMemento firstMarbleMemento = firstMarble.getMarbleMemento();
//            MarbleMemento secondMarbleMemento = secondMarble.getMarbleMemento();
//
//            boolean firstMarbleResult = player.play(firstCard, firstMarble, splitSeven);
//            boolean secondMarbleResult = player.play(secondCard, secondMarble, splitSeven);
//
//            if (firstMarbleResult == false || secondMarbleResult == false)
//            {
//                // One of the moves wasn't valid, set them back and report that it didn't work!
//                firstMarble.setMarbleMemento(firstMarbleMemento);
//                secondMarble.setMarbleMemento(secondMarbleMemento);
//                return false;
//            }
//            else
//            {
//                // Both moves valid
//                nextPlayer();
//                player.removeCard(playedCard);
//                return true;
//            }
//        }
    }

//    private void CreateSpots()
//    {
//        for (int i = 0; i < MaxNumberOfSpots; i++)
//        {
//            _Spots.add(i, new Spot(i));
//        }
//    }

//    private void CreateMarbles()
//    {
//        for (int i = 0; i < MaxNumberOfMarbles; i++)
//        {
//            _Marbles.add(i, new Marble(i));
//        }
//    }
//
//    private void LinkMarblesToSpotsAndPlayers()
//    {
//        final int player0Index = 0;
//        final int player1Index = 1;
//        final int player2Index = 2;
//        final int player3Index = 3;
//
//        final int player0StartingSpot = 32;
//        final int player1StartingSpot = 48;
//        final int player2StartingSpot = 64;
//        final int player3StartingSpot = 80;
//
//        // Set Home spot for marbles
//        int startStart = 88;        // temporary, to allow the new board to be drawn...
//        for (int i = 0; i < MaxNumberOfMarbles; i++)
//        {
//            _Marbles.get(i).setHomeSpot(_Spots.get(startStart++));
//        }
//
//        // Set Starting spot for marbles
//        _Marbles.get(0).setStartingSpot(_Spots.get(player0StartingSpot));
//        _Marbles.get(1).setStartingSpot(_Spots.get(player0StartingSpot));
//        _Marbles.get(2).setStartingSpot(_Spots.get(player0StartingSpot));
//        _Marbles.get(3).setStartingSpot(_Spots.get(player0StartingSpot));
//
//        _Marbles.get(4).setStartingSpot(_Spots.get(player1StartingSpot));
//        _Marbles.get(5).setStartingSpot(_Spots.get(player1StartingSpot));
//        _Marbles.get(6).setStartingSpot(_Spots.get(player1StartingSpot));
//        _Marbles.get(7).setStartingSpot(_Spots.get(player1StartingSpot));
//
//        _Marbles.get(8).setStartingSpot(_Spots.get(player2StartingSpot));
//        _Marbles.get(9).setStartingSpot(_Spots.get(player2StartingSpot));
//        _Marbles.get(10).setStartingSpot(_Spots.get(player2StartingSpot));
//        _Marbles.get(11).setStartingSpot(_Spots.get(player2StartingSpot));
//
//        _Marbles.get(12).setStartingSpot(_Spots.get(player3StartingSpot));
//        _Marbles.get(13).setStartingSpot(_Spots.get(player3StartingSpot));
//        _Marbles.get(14).setStartingSpot(_Spots.get(player3StartingSpot));
//        _Marbles.get(15).setStartingSpot(_Spots.get(player3StartingSpot));
//
//        // Assign marbles to a player
//        _Marbles.get(0).setOwner(_Players.get(player0Index));
//        _Marbles.get(1).setOwner(_Players.get(player0Index));
//        _Marbles.get(2).setOwner(_Players.get(player0Index));
//        _Marbles.get(3).setOwner(_Players.get(player0Index));
//
//        _Marbles.get(4).setOwner(_Players.get(player1Index));
//        _Marbles.get(5).setOwner(_Players.get(player1Index));
//        _Marbles.get(6).setOwner(_Players.get(player1Index));
//        _Marbles.get(7).setOwner(_Players.get(player1Index));
//
//        _Marbles.get(8).setOwner(_Players.get(player2Index));
//        _Marbles.get(9).setOwner(_Players.get(player2Index));
//        _Marbles.get(10).setOwner(_Players.get(player2Index));
//        _Marbles.get(11).setOwner(_Players.get(player2Index));
//
//        _Marbles.get(12).setOwner(_Players.get(player3Index));
//        _Marbles.get(13).setOwner(_Players.get(player3Index));
//        _Marbles.get(14).setOwner(_Players.get(player3Index));
//        _Marbles.get(15).setOwner(_Players.get(player3Index));
//
//    }

//    private void CreatePlayers(int numberOfPlayers)
//    {
//        // Create the player's finish spots
//        Spot[] Player0FinishSpots = { _Spots.get(16), _Spots.get(17), _Spots.get(18), _Spots.get(19) };
//        Spot[] Player1FinishSpots = { _Spots.get(20), _Spots.get(21), _Spots.get(22), _Spots.get(23) };
//        Spot[] Player2FinishSpots = { _Spots.get(24), _Spots.get(25), _Spots.get(26), _Spots.get(27) };
//        Spot[] Player3FinishSpots = { _Spots.get(28), _Spots.get(29), _Spots.get(30), _Spots.get(31) };
//
//        ArrayList<Spot> player0FinishSpots = new ArrayList<Spot>(Arrays.asList(Player0FinishSpots));
//        ArrayList<Spot> player1FinishSpots = new ArrayList<Spot>(Arrays.asList(Player1FinishSpots));
//        ArrayList<Spot> player2FinishSpots = new ArrayList<Spot>(Arrays.asList(Player2FinishSpots));
//        ArrayList<Spot> player3FinishSpots = new ArrayList<Spot>(Arrays.asList(Player3FinishSpots));
//
//        _Players.add(0, new Player(player0FinishSpots, "Yellow"));
//        _Players.add(1, new Player(player1FinishSpots, "Red"));
//        _Players.add(2, new Player(player2FinishSpots, "Blue"));
//        _Players.add(3, new Player(player3FinishSpots, "Green"));
//    }

//    private void nextPlayer()
//    {
//        // Advance the number of turns, then advance the counter to the next player
//        _CurrentPlayersTurn++;
//
//        if (_CurrentPlayersTurn >= _NumberOfPlayers)
//        {
//            _CurrentPlayersTurn = 0;
//        }
//
//        // System.out.println("Now player " + _CurrentPlayersTurn + "'s turn.");
//    }
}
