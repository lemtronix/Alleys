package model;

/**
 * defines the state of a move, and of a turn. A turn is made up of moves; choosing a card and choosing
 * a marble are examples of moves; all the moves for one turn define a turn. These states correspond to
 * messages, so that the move validation logic can return a state which defines a message that may be
 * displayed for the user. The TurnState indicates the status of the turn indicated by the MoveState:
 * <ul>
 * <li>CONTINUING - last move was valid, there is more to come.
 * <li>ERROR - last move was invalid, display a message for the user and await correction.
 * <li>CONTINUING_GET_NUMBER - specific to the 7 card; the game is ready for the player to enter the
 * number of moves he wishes to move the first marble after playing a 7.
 * <li>READY - the turn is ready to be executed; all validations are done.
 * sometimes one move is valid and there is at least one more action to do to complete the move, so that
 * move is CONTINUING; sometimes a turn is invalid and the turn needs to inform the user, that is an ERROR.
 * 
 * @author rcook
 *
 */
public enum MoveState
{
     AWAITING_CARD              (TurnState.CONTINUING,  "info.needCard")
    ,AWAITING_SWAP_MARBLE       (TurnState.CONTINUING,  "info.needSwapMarble")
    
    ,NO_LEGAL_MOVE              (TurnState.ERROR,       "error.noLegalMove")
    
    ,MARBLE_NOT_CURRENT_PLAYER  (TurnState.ERROR,       "error.marbleNotCurrentPlayer")
    ,MARBLE_NOT_IN_BANK         (TurnState.ERROR,       "error.marbleNotInBank")
    ,STARTING_SPOT_OCCUPIED     (TurnState.ERROR,       "error.homeSpotOccupied")
    ,CANNOT_MOVE_FROM_BANK      (TurnState.ERROR,       "error.cannotMoveFromBank")
    
    ,CANNOT_SWAP_FROM_SPOT      (TurnState.ERROR,       "error.cannotSwapFromSpot")
    ,CANNOT_SWAP_ONTO_SPOT      (TurnState.ERROR,       "error.cannotSwapOntoSpot")
    ,MUST_CHOOSE_MARBLE         (TurnState.ERROR,       "error.mustChooseMarble")
    ,CANNOT_SWAP_SAME_MARBLE    (TurnState.ERROR,       "error.cannotChooseSameMarble")
    
    ,ENTER_FIRST_MOVE_COUNT     (TurnState.CONTINUING_GET_NUMBER,  "info.enterNumberToMove")
    ,VALID_MOVE_SEVEN           (TurnState.READY,       "info.validMoveSeven")
    ,COUNT_1_TO_7               (TurnState.ERROR,       "error.count1to7")
    ,NEED_DIFFERENT_MARBLE      (TurnState.ERROR,       "error.needDifferentMarble")
    
    ,MOVE_TOO_LONG              (TurnState.ERROR,       "error.moveTooLong")
    ,BLOCKED_BY_PROTECTED_MARBLE(TurnState.ERROR,       "error.blockedByProtectedMarble")
    ,BLOCKED_BY_HOMEBASE_MARBLE (TurnState.ERROR,       "error.blockedByHomebaseMarble")
    
    ,VALID_MARBLE_START         (TurnState.READY,       "info.validMarbleStart")   // valid to move chosen marble to home spot
    ,VALID_MARBLE_MOVE          (TurnState.READY,       "info.validMarbleMove")   // valid to move chosen marble to square indicated by card count
    ,VALID_MARBLE_BUMP          (TurnState.READY,       "info.validMarbleBump")   // valid move with marble to bump back to bank
    ,VALID_MARBLE_SWAP          (TurnState.READY,       "info.validMarbleSwap")
    
    ,WAITING_FOR_SWAP_MARBLE    (TurnState.CONTINUING,  "info.chooseMarbleSwap")
    ,WAITING_FOR_SECOND_MARBLE  (TurnState.CONTINUING,  "info.chooseSecondMarble")
    
    ;
    
    TurnState turnState;
    String messageKey;
     
    MoveState(TurnState turnState, String key) { this.turnState = turnState; messageKey = key; }
    
    public TurnState getTurnState() { return turnState; }
    public String  getMessageKey() { return messageKey; }
}

