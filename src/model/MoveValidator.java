package model;


public class MoveValidator
{
    public static void say(String msg) { System.out.println(msg); }
    
    public static MoveState validateMove(Board board, Player player, Spot spot, Card card)
    {
        MoveState moveState = null;
        
        switch (card.getRank())
        {
        case King:
            moveState = validateBankToHome(player, board, spot);
            break;
        case Ace:
            // An ace may either move a marble from bank to home, or one square elsewhere
            // on the board.
            if (spot.getSpotType() == SpotType.BANK)
            {
                moveState = validateBankToHome(player, board, spot);
            }
            else
            {
                moveState = validateMoveMarble(player, board, spot, 1);
            }
            break;
        case Jack:
            say( card.toString() + " not yet implemented");
            break;
        case Seven:
            say( card.toString() + " not yet implemented");
            break;
        default:
            moveState = validateMoveMarble(player, board, spot, card.toInt(false));
            break;
        }
        
        return moveState;
    }
    
    private static MoveState validateMoveMarble(Player player, Board board, Spot currentSpot, int moveCount)
    {
        MoveState moveState = null;
        Marble      currentMarble       = currentSpot.getMarble();
        MarbleColor currentMarbleColor  = currentMarble.getColor();

        // get the move track for this color, and the index into the move track
        // for the marble's current position
        int currentSpotIndex = currentSpot.getSpotIndex();
        int currentMoveTrackIndex = board.getMoveTrackIndex(currentMarbleColor, currentSpotIndex);
        int targetMoveTrackIndex = currentMoveTrackIndex + moveCount;
        Integer[] moveTrack = board.getMoveTrack(currentMarbleColor);
        
        // ensure there are enough spots for the length of the move
        if (targetMoveTrackIndex >= moveTrack.length)
        {
            moveState = MoveState.MOVE_TOO_LONG;
        }
        else
        {
            // for each spot, determine whether there are obstacles
            // a marble cannot move past another color marble in its own home spot,
            // nor past its own marble in a finishing spot.
            for (int moveTrackIndex = currentMoveTrackIndex; 
                    currentMoveTrackIndex <= targetMoveTrackIndex;
                    moveTrackIndex++)
            {
                int allSpotsIndex = moveTrack[moveTrackIndex];
                Spot spot = board.getSpot(allSpotsIndex);
                SpotType spotType = spot.getSpotType();
                Marble marble = spot.getMarble();
                if (spotType == SpotType.STARTINGSPOT && marble != null)
                {
                    MarbleColor spotColor = spot.getColor();
                    MarbleColor marbleColor = marble.getColor();
                    if (spotColor == marbleColor)
                    {
                        moveState = MoveState.BLOCKED_BY_PROTECTED_MARBLE;
                        break;
                    }
                }
                else if (spotType == SpotType.HOMEBASE && marble != null)
                {
                    moveState = MoveState.BLOCKED_BY_HOMEBASE_MARBLE;
                }
            }
            
            if (moveState == null) 
            {
                // no errors; determine whether the last spot moved to has another marble in it;
                // is one allowed to bump one's own marble?
                Spot lastSpot = board.getSpot(moveTrack[targetMoveTrackIndex]);
                if (lastSpot.hasMarble())
                {
                    moveState = MoveState.VALID_MARBLE_BUMP;
                }
                else
                {
                    moveState = MoveState.VALID_MARBLE_MOVE;
                }
            }
        }
        
        return moveState;
    }
        
    private static MoveState validateBankToHome(Player player, Board board, Spot marbleSpot)
    {
        MoveState moveState = null;
        
        SpotType spotType = marbleSpot.getSpotType();
        Marble marble = marbleSpot.getMarble();
        
        if (marble.getColor() != player.getColor())
        {
            say("Marble must belong to current player");
            moveState = MoveState.MARBLE_NOT_CURRENT_PLAYER;
        }
        else
        {
            if (spotType != SpotType.BANK)
            {
                say("Marble must be in the bank");
                moveState = MoveState.MARBLE_NOT_IN_BANK;
            }
            else
            {
                int startingSpotIndex = board.getHomeIndex(marble.getColor());
                Spot startingSpot = board.getSpot(startingSpotIndex);
                if (startingSpot.hasMarble())
                {
                    say("Home spot already occupied");
                    moveState = MoveState.HOME_SPOT_OCCUPIED;
                }
                else
                {
                    // we're validated -- can move this marble from bank to home.
                    say("move is good");
                    moveState = MoveState.VALID_MARBLE_START;
                }
            }
        }
        return moveState;
    }
    
}
