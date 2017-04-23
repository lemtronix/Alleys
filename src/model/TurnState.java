package model;

/**
 * The states of a MoveState object
 * @author rcook
 *
 */
public enum TurnState
{
    START               // initial turn state, ready for card, then marble
    , ERROR             // error made; marbles cleared, card remains
    , CONTINUING        // all right so far, waiting for swap marble (Jack) or 2nd marble (7)
    , CONTINUING_GET_NUMBER  // waiting for count (7) 
    , READY             // turn is complete and can be executed.
}
