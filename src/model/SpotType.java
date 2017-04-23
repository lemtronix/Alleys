package model;

/**
 * Designates the type of the spot on the board: starting, home, normal, or finishing.
 * @author rcook
 *
 */
public enum SpotType
{
    BANK        // spots on which marbles sit at start of game
    ,STARTINGSPOT           // first spot to which a marble moves in the game
    ,NORMAL         // spots around the 'loop' of the board, no colors (doesn't include home spots)
    ,HOMEBASE      // spots to which marbles move at the end of their travel around the board
}
