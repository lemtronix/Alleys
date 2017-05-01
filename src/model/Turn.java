package model;

import java.util.ArrayList;
import java.util.List;

/**
 * represents a 'turn' in the game; this includes the card a player has chosen
 * and one or more marbles (if the player chooses a 7, this is where the two
 * marbles chosen to move are stored). It takes proposed 'moves', such as the
 * choice of a card or a marble, and holds state to indicate whether those moves
 * are valid or why they aren't.
 * 
 * @author rcook
 *
 */
public class Turn
{
    public static void say(String msg)    {       System.out.println(msg);    }

    Messager    messager            = null;
    Player      player              = null;
    Card        card                = null;                 // card chosen to be played
    List<Spot>  marbles             = new ArrayList<>();    // spots (with marbles) chosen for this turn
    List<Spot>  destinations        = new ArrayList<>();    // destinations for marbles if move successful.
    MoveState   moveState           = null;
    int         moveCountForSeven   = 0;

    public Turn(Messager messager, Player player)
    {
        this.messager = messager;
        this.player = player;
        moveState = MoveState.AWAITING_CARD;
    }

    /**
     * sets the chosen card into the current turn; if a card was already chosen,
     * this replaces it and returns it, otherwise this returns null.
     * 
     * @param card already set in the turn, if there is one, or null.
     */
    public Card setCard(Card newCard)
    {
        Card previousCard = card;
        card = newCard;
        if (previousCard == null)
        {
            messager.message("info.setCard", card.toString());
        }
        else
        {
            messager.message("info.replacedCard", previousCard.toString(), card.toString());
        }
        if (!marbles.isEmpty() || !destinations.isEmpty())
        {
            marbles.clear();
            destinations.clear();
            messager.message("info.marblesCleared");
        }

        return previousCard;
    }

    public boolean hasCard()
    {
        return (card != null);
    }

    public MarbleColor getColor()
    {
        return player.getColor();
    }

    public Card getCard()
    {
        return card;
    }

    public List<Spot> getMarbles()
    {
        return marbles;
    }

    public List<Spot> getDestinations()
    {
        return destinations;
    }

    public Player getPlayer()
    {
        return player;
    }

    public MoveState getMoveState()
    {
        return moveState;
    }

    public void addMarble(Spot spot)
    {
        marbles.add(spot);
    }

    public void addDestination(Spot spot)
    {
        destinations.add(spot);
    }

    public void clearMarbles()
    {
        marbles.clear();
    }

    public Turn validateMove(Board board, Spot spot)
    {

        switch (card.getRank())
        {
        case King:
            moveState = validateBankToHome(player, board, spot);
            break;
        case Ace:
            // An ace may either move a marble from bank to home, or one square
            // elsewhere on the board. If he chose a marble in the bank, we'll
            // try to move it to home, otherwise we try to move it one spot.
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
            moveState = validateMoveJack(player, board, spot);
            break;
        case Seven:
            moveState = validateMoveSeven(player, board, spot);
            break;
        default:
            moveState = validateMoveMarble(player, board, spot, card.toInt());
            break;
        }

        return this;
    }

    private MoveState validateMoveMarble(Player player, Board board, Spot currentSpot, int moveCount)
    {
        MoveState   moveState           = null;
        Marble      currentMarble       = currentSpot.getMarble();
        MarbleColor currentMarbleColor  = currentMarble.getColor();
        SpotType    currentSpotType     = currentSpot.getSpotType();

        // get the move track for this color, and the index into the move track
        // for the marble's current position
        int currentSpotIndex        = currentSpot.getSpotIndex();
        int currentMoveTrackIndex   = board.getMoveTrackIndex(currentMarbleColor, currentSpotIndex);
        int targetMoveTrackIndex    = currentMoveTrackIndex + moveCount;

        // if we're moving backwards, we may move backwards beyond the start of
        // the moveTrack (i.e., past the home spot). In that case, we need to calculate the
        // main loop spot to which it would move.
        targetMoveTrackIndex = translateTrackIndex(targetMoveTrackIndex);

        Integer[] moveTrack = board.getMoveTrack(currentMarbleColor);

        // ensure that we aren't moving from the bank, 
        // and that there are enough spots for the length of the move
        if (currentSpotType == SpotType.BANK)
        {
            moveState = MoveState.CANNOT_MOVE_FROM_BANK;
        }
        else if (targetMoveTrackIndex >= moveTrack.length)
        {
            moveState = MoveState.MOVE_TOO_LONG;
        }
        else
        {
            // we do this loop either forward or backward, so we have
            // to ensure that each track index is non-negative.
            int moveTrackIndex = currentMoveTrackIndex;
            int increment = 1;
            if (moveCount < 0) { increment = -1; }

            // for each spot, determine whether there are obstacles
            // a marble cannot move past a protected marble,
            // nor past its own marble in a finishing spot.
            do
            {
                moveTrackIndex = translateTrackIndex(moveTrackIndex + increment);

                int allSpotsIndex = moveTrack[moveTrackIndex];
                Spot spot = board.getSpot(allSpotsIndex);
                // check on protected marble (has not moved since moving from bank to starting spot)
                if (spot.isProtected())
                {
                        moveState = MoveState.BLOCKED_BY_PROTECTED_MARBLE;
                        break;
                }
                else
                {   // check on homebase marble (one on of four finishing spots, can't be passed or bumped)
                    Marble marble = spot.getMarble();
                    if (currentSpotType == SpotType.HOMEBASE && marble != null)
                    {
                        moveState = MoveState.BLOCKED_BY_HOMEBASE_MARBLE;
                    }
                }
            } while (moveTrackIndex != targetMoveTrackIndex);

            if (moveState == null)
            {
                // no errors; determine whether the last spot moved to has another marble in it;
                // is one allowed to bump one's own marble? We'll program it that way for now.
                // It would allow a player to stay in the game if a Jack was his
                // only legal move, and he could hope something would come free before it was his
                // turn again.
                Spot lastSpot = board.getSpot(moveTrack[targetMoveTrackIndex]);
                // check if there's a marble being bumped.
                // TODO: determine if we allow bumping of one's own marble.
                if (lastSpot.hasMarble())
                    {
                      moveState = MoveState.VALID_MARBLE_BUMP;
                      setMarblesForBump(board, lastSpot, currentSpot);
                    }
                else
                {
                    marbles.add(currentSpot);
                    destinations.add(lastSpot);
                    moveState = MoveState.VALID_MARBLE_MOVE;
                }
            }
        }

        return moveState;
    }
    
    private MoveState validateMoveSeven(Player player, Board board, Spot currentSpot)
    {
        MoveState result = null;
        
        Marble marble = currentSpot.getMarble();
        MarbleColor marbleColor = marble.getColor();
        MarbleColor playerColor = player.getColor();
        
        // this method gets called either once or twice for a seven; remember the player
        // may move either one or two marbles, where the total number of moves is 7.
        // A different method on this object handles putting the number of moves somewhere.
        
        // both marbles must belong to the player
        if (marbleColor != playerColor)
        {
            result = MoveState.MARBLE_NOT_CURRENT_PLAYER;
        }
        else
        {
            // all we know on the first marble is to store it; we don't know
            // at the time it is chosen how many it will move. 
            if (marbles.isEmpty())
            {
                marbles.add(currentSpot);
                result = MoveState.ENTER_FIRST_MOVE_COUNT;
            }
            else
            {  
                // ok, we have a second marble; let's make sure they didn't click the first one again
                Spot firstMarbleSpot = marbles.get(0);
                if (firstMarbleSpot == currentSpot)
                {
                    result = MoveState.NEED_DIFFERENT_MARBLE;
                }
                else
                {
                    // ok, we have two marbles and a count we're going to use on the first one.
                    // (see validateMoveCountForSeven). Validate the move of the second marble
                    result = validateMoveMarble(player, board, currentSpot, 7 - moveCountForSeven);
                }
            }
        }
        return result;
    }
    
    public Turn validateMoveCountForSeven(Board board, int moveCount)
    {
        if (moveCount > 7 || moveCount < 1)
        {
            moveState = MoveState.COUNT_1_TO_7;
        }
        else
        {
            moveCountForSeven = moveCount;
            Spot currentSpot = marbles.get(0);
            // now that we've gotten the marble we saved, clear it out
            // so that we can just move that marble as though it were not
            // already saved.
            marbles.clear();
            
            if (moveCount == 7)
            {
                // just moving 7
                // clear out the marble we saved earlier, and treat this like
                // any other numbered-card move.
                moveState = validateMoveMarble(player, board, currentSpot, 7);
            }
            else
            {
                // validate that we can move the number of spaces entered.
                moveState = validateMoveMarble(player, board, currentSpot, moveCount);
                
                // if we're good to go, then change the state to be waiting for the second
                // marble pick
                if (moveState.getTurnState() == TurnState.READY)
                {
                    moveState = MoveState.WAITING_FOR_SECOND_MARBLE;
                }
            }
        }
        return this;
    }
    
    private MoveState validateMoveJack(Player player, Board board, Spot currentSpot)
    {
        MoveState result = null;
        // we call this once on the first marble and once on the second; we use
        // the marbles list to tell us which one we're doing.
        if (marbles.isEmpty())
        {
            // chose first marble for Jack; it must be a marble for the turn's
            // player. We cannot swap out of bank or homebase spots.
            Marble marble = currentSpot.getMarble();
            MarbleColor marbleColor = marble.getColor();
            MarbleColor playerColor = player.getColor();
            if (marbleColor != playerColor)
            {
                result = MoveState.MARBLE_NOT_CURRENT_PLAYER;
            }
            else
            {
                SpotType spotType = currentSpot.getSpotType();
                
                if (   spotType == SpotType.BANK 
                    || spotType == SpotType.HOMEBASE
                   )
                {
                    result = MoveState.CANNOT_SWAP_FROM_SPOT;
                }
                else
                {
                    result = MoveState.AWAITING_SWAP_MARBLE;
                    marbles.add(currentSpot);
                    destinations.add(currentSpot);
                }
            }
        }
        else
        {
            // have chosen the second marble for Jack; cannot swap into bank,
            // or finishing for the 2nd marble, and cannot swap a protected
            // marble.
            // TODO: can a player swap with his own marble?
            int spotIndex = currentSpot.getSpotIndex();
            int firstSpotIndex = marbles.get(0).getSpotIndex();
            if (spotIndex == firstSpotIndex)
            {
                result = MoveState.CANNOT_SWAP_SAME_MARBLE;
            }
            else
            {
                SpotType    spotType    = currentSpot.getSpotType();
                // cannot swap into a finishing or starting spot
                if (   spotType == SpotType.HOMEBASE 
                    || spotType == SpotType.BANK
                    || currentSpot.isProtected())
                {
                    // cannot swap a marble that's on any special spot
                    result = MoveState.CANNOT_SWAP_ONTO_SPOT;
                }
                else
                {
                    // this marble can be swapped
                    marbles.add(currentSpot);
                    destinations.add(currentSpot);
                    result = MoveState.VALID_MARBLE_SWAP;
                }
            }
        }
        return result;
    }

    private void setMarblesForBump(Board board, Spot destination, Spot source)
    {
        Spot bankSpot = board.findFirstEmptyBankSpot(destination.getMarble().getColor());
        addMarble(destination);
        addDestination(bankSpot);
        addMarble(source);
        addDestination(destination);
    }

    /**
     * translate the given index in case we are moving backwards through spots
     * past the home spot; in this case increasing negative numbers will go from
     * the home spot to the last spot on the main loop of the board, then the
     * one before that, etc.
     * 
     * @param index
     * @return
     */
    private int translateTrackIndex(int index)
    {
        if (index < 0) { index += 72; } // -1 -> 71, -2 -> 70, etc.
        return index;
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
            if (spotType != SpotType.BANK)
            {
                say("Marble must be in the bank");
                moveState = MoveState.MARBLE_NOT_IN_BANK;
            }
            else
            {
                int startingSpotIndex = board.getStartingIndex(marble.getColor());
                Spot startingSpot = board.getSpot(startingSpotIndex);
                MarbleColor playerColor = player.getColor();
                if (startingSpot.hasMarble())
                    {
                        if (startingSpot.getMarble().getColor() == playerColor)
                        {
                            say("Starting spot already occupied");
                            moveState = MoveState.STARTING_SPOT_OCCUPIED;
                        }
                        else
                        {
                            // bumping marble from home spot
                            moveState = MoveState.VALID_MARBLE_BUMP;
                            setMarblesForBump(board, startingSpot, marbleSpot);
                        }
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
