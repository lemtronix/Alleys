package model;

public enum MoveState
{
     AWAITING_CARD              (true,  "info.needCard")
    ,AWAITING_MARBLE            (true,  "info.needMarble")
    ,NO_LEGAL_MOVE              (true,  "info.noLegalMove")
    ,AWAITING_2ND_MARBLE        (true,  "info.needSecondMarble")
    
    ,MARBLE_NOT_CURRENT_PLAYER  (true,  "error.marbleNotCurrentPlayer")
    ,MARBLE_NOT_IN_BANK         (true,  "error.marbleNotInBank")
    ,HOME_SPOT_OCCUPIED         (true,  "error.homeSpotOccupied")
    
    ,MOVE_TOO_LONG              (true,  "error.moveTooLong")
    ,BLOCKED_BY_HOME_MARBLE     (true,  "error.blockedByHomeMarble")
    ,BLOCKED_BY_FINISHING_MARBLE (true, "error.blockedByFinishingMarble")
    
    ,VALID_MARBLE_START         (false, "info.validMarbleStart")   // valid to move chosen marble to home spot
    ,VALID_MARBLE_MOVE          (false, "info.validMarbleMove")   // valid to move chosen marble to square indicated by card count
    ,VALID_MARBLE_BUMP          (false, "info.validMarbleBump")   // valid move with marble to bump back to bank
    ,VALID_MARBLE_SWAP          (false, "info.validMarbleSwap")   // valid move, swapping marble at target with chosen marble.
    ;
    
    boolean error;
    String messageKey;
     
    MoveState(boolean error, String key) { this.error = error; messageKey = key; }
    
    public boolean isError() { return error; }
    public String  getMessageKey() { return messageKey; }
}
