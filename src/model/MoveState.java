package model;

public enum MoveState
{
     AWAITING_CARD              (TurnState.CONTINUING,  "info.needCard")
    ,AWAITING_SWAP_MARBLE       (TurnState.CONTINUING,  "info.needSwapMarble")
    
    ,NO_LEGAL_MOVE              (TurnState.ERROR,  "error.noLegalMove")
    
    ,MARBLE_NOT_CURRENT_PLAYER  (TurnState.ERROR,  "error.marbleNotCurrentPlayer")
    ,MARBLE_NOT_IN_BANK         (TurnState.ERROR,  "error.marbleNotInBank")
    ,HOME_SPOT_OCCUPIED         (TurnState.ERROR,  "error.homeSpotOccupied")
    
    ,CANNOT_SWAP_FROM_SPOT      (TurnState.ERROR,  "error.cannotSwapFromSpot")
    ,CANNOT_SWAP_ONTO_SPOT      (TurnState.ERROR,  "error.cannotSwapOntoSpot")
    ,MUST_CHOOSE_MARBLE         (TurnState.ERROR,  "error.mustChooseMarble")
    ,CANNOT_SWAP_SAME_MARBLE    (TurnState.ERROR,  "error.cannotChooseSameMarble")
    
    ,MOVE_TOO_LONG              (TurnState.ERROR,  "error.moveTooLong")
    ,BLOCKED_BY_HOME_MARBLE     (TurnState.ERROR,  "error.blockedByHomeMarble")
    ,BLOCKED_BY_FINISHING_MARBLE (TurnState.ERROR, "error.blockedByFinishingMarble")
    
    ,VALID_MARBLE_START         (TurnState.READY, "info.validMarbleStart")   // valid to move chosen marble to home spot
    ,VALID_MARBLE_MOVE          (TurnState.READY, "info.validMarbleMove")   // valid to move chosen marble to square indicated by card count
    ,VALID_MARBLE_BUMP          (TurnState.READY, "info.validMarbleBump")   // valid move with marble to bump back to bank
    ,VALID_MARBLE_SWAP          (TurnState.READY, "info.validMarbleSwap")
    
    ,WAITING_FOR_SWAP_MARBLE    (TurnState.CONTINUING, "info.chooseMarbleSwap")
    ,WAITING_FOR_SECOND_MARBLE  (TurnState.CONTINUING, "info.chooseSecondMarble")
    
    ;
    
    TurnState turnState;
    String messageKey;
     
    MoveState(TurnState turnState, String key) { this.turnState = turnState; messageKey = key; }
    
    public TurnState getTurnState() { return turnState; }
    public String  getMessageKey() { return messageKey; }
}

