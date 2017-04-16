package model;

import java.util.ArrayList;
import java.util.List;

/**
 * represents a 'turn' in the game; this includes the card a player has chosen and one or
 * more marbles (if the player chooses a 7, this is where the two marbles chosen to move are
 * stored). It takes proposed 'moves', such as the choice of a card or a marble, and holds 
 * state to indicate whether those moves are valid or why they aren't.
 * 
 * @author rcook
 *
 */
public class Turn
{
    public static void say(String msg) { System.out.println(msg); }

    Player          player              = null;
    Card            card                = null;                 // card chosen to be played
    List<Spot>      marbles             = new ArrayList<>();    // spots (with marbles) chosen for this turn
    List<Spot>      destinations        = new ArrayList<>();    // destinations for marbles if move successful.
    MoveState       lastMoveState       = null;
    
    public Turn(Player player)    { this.player = player; }
    
    public Card setCard(Card newCard)
    {
        Card result = card;
        card = newCard;
        return result;
    }
    
    public boolean      hasCard()   { return (card != null);        }
    public MarbleColor  getColor()  { return player.getColor();     }
    public Card         getCard()   { return card;                  }
    public List<Spot>   getMarbles() { return marbles;              }
    public List<Spot>   getDestinations() { return destinations; }
    public Spot         getFirstMarble() { return marbles.get(0); }
    public Spot         getFirstDestination() { return destinations.get(0); }
    public Spot         getSecondMarble() { return marbles.get(1); }
    public Spot         getSecondDestination() { return destinations.get(1); }
    
    public Player       getPlayer() { return player;                }
    public MoveState    getLastMoveState() { return lastMoveState;  }
    
    public void         setLastMoveState(MoveState newState)    { lastMoveState = newState; }
    public void         addMarble(Spot spot)                    { marbles.add(spot);        }
    public void         addDestination(Spot spot)               { destinations.add(spot); }
    public void         clearMarbles()                          { marbles.clear();          }
    
    public MoveState validateMove(Board board, Spot spot)
    {
        
        switch (card.getRank())
        {
        case King:
            lastMoveState = validateBankToHome(player, board, spot);
            break;
        case Ace:
            // An ace may either move a marble from bank to home, or one square elsewhere
            // on the board.
            if (spot.getSpotType() == SpotType.STARTING)
            {
                lastMoveState = validateBankToHome(player, board, spot);
            }
            else
            {
                lastMoveState = validateMoveMarble(player, board, spot, 1);
            }
            break;
        case Jack:
            say( card.toString() + " not yet implemented");
            break;
        case Seven:
            say( card.toString() + " not yet implemented");
            break;
        default:
            lastMoveState = validateMoveMarble(player, board, spot, card.toInt(false));
            break;
        }
        
        return lastMoveState;
    }
    
    private MoveState validateMoveMarble(Player player, Board board, Spot currentSpot, int moveCount)
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
            for (int moveTrackIndex = currentMoveTrackIndex + 1;    // start one beyond current spot 
                    moveTrackIndex <= targetMoveTrackIndex;  // else we can't move off start...
                    moveTrackIndex++)
            {
                int allSpotsIndex = moveTrack[moveTrackIndex];
                Spot spot = board.getSpot(allSpotsIndex);
                SpotType spotType = spot.getSpotType();
                Marble marble = spot.getMarble();
                if (spotType == SpotType.HOME && marble != null)
                {
                    MarbleColor spotColor = spot.getColor();
                    MarbleColor marbleColor = marble.getColor();
                    if (spotColor == marbleColor)
                    {
                        moveState = MoveState.BLOCKED_BY_HOME_MARBLE;
                        break;
                    }
                }
                else if (spotType == SpotType.FINISHING && marble != null)
                {
                    moveState = MoveState.BLOCKED_BY_FINISHING_MARBLE;
                }
            }
            
            if (moveState == null) 
            {
                // no errors; determine whether the last spot moved to has another marble in it;
                // is one allowed to bump one's own marble? We'll program it that way for now.
                // It would allow a player to stay in the game if a Jack was his only legal move, 
                // and he could hope something would come free before it was his turn again.
                Spot lastSpot = board.getSpot(moveTrack[targetMoveTrackIndex]);
                if (lastSpot.hasMarble())
                {
                    moveState = MoveState.VALID_MARBLE_BUMP;
                    // find first available bank spot for marble getting bumped
                    Spot bankSpot = board.findFirstEmptyBankSpot(lastSpot.getMarble().getColor());
                    addMarble(lastSpot);
                    addDestination(bankSpot);
                    // now move marble to bump spot
                    addMarble(currentSpot);
                    addDestination(lastSpot);
                }
                else
                {
                    moveState = MoveState.VALID_MARBLE_MOVE;
                    marbles.add(currentSpot);
                    destinations.add(lastSpot);
                }
            }
        }
        
        return moveState;
    }
        
    private MoveState validateBankToHome(Player player, Board board, Spot marbleSpot)
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
            if (spotType != SpotType.STARTING)
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
                    marbles.add(marbleSpot);
                    destinations.add(startingSpot);
                }
            }
        }
        return moveState;
    }
    
}
