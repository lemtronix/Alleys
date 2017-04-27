package model;

/**
 * A TurnState indicates the action to be taken after the last move made:
 * error, continuing, ready for execution, etc.
 * @author rcook
 *
 */
public enum TurnState
{
    START                   // initial turn state, ready for card, then marble
    , ERROR                 // error made; marbles cleared, card remains
    , CONTINUING            // all right so far, waiting for next move
    , CONTINUING_GET_NUMBER // waiting for count (7) 
    , READY                 // turn is complete and can be executed.
}
