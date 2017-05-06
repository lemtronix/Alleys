package model;

import java.util.List;

import view.viewinterface.AlleysUI;

public class AlleysGame implements Messager
{
//    private static void say(String msg) { System.out.println(msg); }

    private AlleysUI        alleysUI;
    private Board           board;          public Board getBoard() { return board; }
    private TurnManager     turnManager;

    /**
     * return a fresh instance of the Alleys game model.
     * @return
     */
    public static AlleysGame getNewInstance()
    {
        return new AlleysGame();
    }

    /**
     * construct the Alleys game model.
     */
    public AlleysGame() 
    {
        board = new Board(this);
        board.initializeBoard();
    }
    
    /**
     * set the view class for this alleys game model.
     * @param alleysUI
     */
    public void setAlleysUI(AlleysUI alleysUI)
    {
        this.alleysUI = alleysUI;
        board.setAlleysUI(alleysUI);
    }

    /**
     * return the view class from this Alleys game model.
     * @return
     */
    public AlleysUI getAlleysUI()
    {
        return alleysUI;
    }
    
    /**
     * Initialize a game with the given list of players. Sets marbles on the board
     * and starts the first turn.
     * @param players
     */
    public void startGame(List<Player> players)
    {
        turnManager = new TurnManager(this, players);
        board.setUpMarbles(players);
        startNewTurn();
    }
    
    /**
     * Starts the next turn in the game; used for the very first turn as well as all
     * others.
     */
    private void startNewTurn() 
    {
        Player player = turnManager.startNewTurn();
        alleysUI.startTurn(player);
    }

    /**
     * Called when a card has been chosen in the view.
     * @param card
     */
    public void cardChosen(Card card)    
    { 
//        say("chose " + card.toString());
        Turn currentTurn = turnManager.getCurrentTurn();
        Player player = currentTurn.getPlayer();
        
        // first deal with the previous card if it's there
        Card previousCard = currentTurn.getCard();
        
        // if we had a previous card, reverse the effects of
        // it being chosen.
        if (previousCard != null) { unChooseCard(currentTurn, card); }
        
        // now we can set the marble in the turn
        currentTurn.setCard(card);
        
        // remove the just-played card from the player's hand,
        // and execute a 'choose' for it on the board (moves card
        // out of the hand and onto the middle.
        player.removeCard(card);
        alleysUI.chooseCard(card);
    }
    
    /**
     * remove this card from the turn and replace it in the player's hand.
     * Tell the UI to un-choose it as well.
     * @param currentTurn
     * @param card
     */
    private void unChooseCard(Turn currentTurn, Card card)
    {
        currentTurn.setCard(null);
        currentTurn.getPlayer().addCard(card);
        alleysUI.unChooseCard();
    }

    /**
     * Called when a marble has been chosen. It is only called when a spot is chosen
     * that has a marble on it; we don't have to validate marble existence here.
     * @param marbleSpotIndex
     */
    public void marbleChosen(int marbleSpotIndex)
    {
        Spot marbleSpot = board.getSpot(marbleSpotIndex);
        Turn currentTurn = turnManager.getCurrentTurn();
        if (marbleSpot == null || currentTurn == null) { throw new InternalLogicError("either marbleSpot or currentTurn null at start of AlleysGame.marbleChosen"); }
        
        if (!currentTurn.hasCard())
        {
            message("error.chooseCardFirst");
        }
        else
        {
            currentTurn = currentTurn.validateMarbleMove(board, marbleSpot); 
            dispatchTurn(currentTurn);
        }
    }
        
    /**
     * Determine next action depending on the state of the given turn.
     * @param currentTurn
     */
    private void dispatchTurn(Turn currentTurn)
    {
        MoveState moveState = currentTurn.getMoveState();   // move state includes success or error state from last user action.
        TurnState turnState = moveState.getTurnState();     // turn state indicates mostly what the game is going to do based on move state.
                                                            // turn state is a characteristic of moveState, i.e.,
                                                            // each move state has a constant turn state. see TurnState.
        switch(turnState)
        {
        case ERROR:
            // problem -- doing things this way doesn't allow parameters to the
            // message; perhaps we should format the message within the turn and
            // put the whole *message* so we can output it here.
            // TODO: consider making a noise on a move error.
            message(moveState.getMessageKey());
            currentTurn.clearMarblesAndDestinations();
            break;
        case CONTINUING:
            message(moveState.getMessageKey());
            break;
        case CONTINUING_GET_NUMBER:
            message(moveState.getMessageKey());
            int moveCount = alleysUI.getMoveCount();    // 7 being played, this gets num of spots to move first marble.
            if (moveCount == 0) 
                 { /* user cancelled the 7; message already delivered (?) */ 
                   // TODO: message to choose another card?
                   currentTurn.clearMarblesAndDestinations();
                   unChooseCard(currentTurn, currentTurn.getCard());
                 }
            else { moveCountForSevenChosen(moveCount);  }
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
    
    /**
     * Called when the user has chosen a number to move the first marble after
     * chosing a 7.
     * @param moveCount
     */
    public void moveCountForSevenChosen(int moveCount)
    {
        Turn currentTurn = turnManager.getCurrentTurn();
        currentTurn = currentTurn.validateMoveCountForSeven(board, moveCount);
        dispatchTurn(currentTurn);
    }
    
    /**
     * Called when a user has folded their hand.
     */
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
    
}
