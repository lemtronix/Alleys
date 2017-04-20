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
    public static void say(String msg)
    {
        System.out.println(msg);
    }

    Player player = null;
    Card card = null; // card chosen to be played
    List<Spot> marbles = new ArrayList<>(); // spots (with marbles) chosen for
                                            // this turn
    List<Spot> destinations = new ArrayList<>(); // destinations for marbles if
                                                 // move successful.
    MoveState moveState = null;

    public Turn(Player player)
    {
        this.player = player;
        moveState = MoveState.AWAITING_CARD;
    }

    /**
     * sets the chosen card into the current turn; if a card was already chosen,
     * this replaces it.
     * 
     * @param card
     */
    public Card setCard(Card newCard, Messager messager)
    {
        Card result = card;
        card = newCard;
        if (result == null)
        {
            messager.message("info.setCard", card.toString());
        }
        else
        {
            messager.message("info.replacedCard", result.toString(), card.toString());
        }
        if (!marbles.isEmpty())
        {
            marbles.clear();
            messager.message("info.marblesCleared");
        }

        return result;
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

    public Spot getFirstMarble()
    {
        return marbles.get(0);
    }

    public Spot getFirstDestination()
    {
        return destinations.get(0);
    }

    public Spot getSecondMarble()
    {
        return marbles.get(1);
    }

    public Spot getSecondDestination()
    {
        return destinations.get(1);
    }

    public Player getPlayer()
    {
        return player;
    }

    public MoveState getMoveState()
    {
        return moveState;
    }

    public void setLastMoveState(MoveState newState)
    {
        moveState = newState;
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
            // elsewhere
            // on the board. If he chose a marble in the starting spots, we'll
            // try to move
            // it to home, otherwise (home, normal, or finishing) we try to move
            // it one spot.
            if (spot.getSpotType() == SpotType.STARTING)
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
            say(card.toString() + " not yet implemented");
            break;
        default:
            moveState = validateMoveMarble(player, board, spot, card.toInt(false));
            break;
        }

        return this;
    }

    // private MoveState validateSwapMarble(Player player, Board board, Spot
    // currentSpot)

    private MoveState validateMoveJack(Player player, Board board, Spot currentSpot)
    {
        MoveState result = null;
        // we call this once on the first marble and once on the second; we use
        // the
        // marbles list to tell us which one we're doing.
        if (marbles.isEmpty())
        {
            // chose first marble for Jack; we cannot swap out of starting or
            // finishing spots
            // TODO: find out if this is true for home.
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
                
                if (spotType == SpotType.STARTING || spotType == SpotType.FINISHING)
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
            // home, or finishing for the 2nd marble.
            // TODO: do we limit this to swap with marbles of other colors?
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
                if (spotType == SpotType.FINISHING || spotType == SpotType.STARTING)
                {
                    // cannot swap a marble that's on any special spot
                    // TODO: do we have a protected status of a marble on its own home?
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

    private MoveState validateMoveMarble(Player player, Board board, Spot currentSpot, int moveCount)
    {
        MoveState moveState = null;
        Marble currentMarble = currentSpot.getMarble();
        MarbleColor currentMarbleColor = currentMarble.getColor();
        SpotType    currentSpotType = currentSpot.getSpotType();

        // get the move track for this color, and the index into the move track
        // for the marble's current position
        int currentSpotIndex = currentSpot.getSpotIndex();
        int currentMoveTrackIndex = board.getMoveTrackIndex(currentMarbleColor, currentSpotIndex);
        int targetMoveTrackIndex = currentMoveTrackIndex + moveCount;

        // if we're moving backwards, we may move backwards beyond the start of
        // the moveTrack (i.e., past the home spot). In that case, we need to calculate the
        // main loop spot to which it would move.
        targetMoveTrackIndex = ensurePositiveMoveTrackIndex(targetMoveTrackIndex);

        Integer[] moveTrack = board.getMoveTrack(currentMarbleColor);

        // ensure there are enough spots for the length of the move
        if (targetMoveTrackIndex >= moveTrack.length)
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

            do
            {
                moveTrackIndex = ensurePositiveMoveTrackIndex(moveTrackIndex + increment);

                // for each spot, determine whether there are obstacles
                // a marble cannot move past another color marble in its own
                // home spot, nor past its own marble in a finishing spot.
                int allSpotsIndex = moveTrack[moveTrackIndex];
                Spot spot = board.getSpot(allSpotsIndex);
//                SpotType spotType = spot.getSpotType();
                Marble marble = spot.getMarble();
                if (currentSpotType == SpotType.HOME && marble != null)
                {
                    MarbleColor spotColor = spot.getColor();
                    MarbleColor marbleColor = marble.getColor();
                    if (spotColor == marbleColor)
                    {
                        moveState = MoveState.BLOCKED_BY_HOME_MARBLE;
                        break;
                    }
                }
                else if (currentSpotType == SpotType.FINISHING && marble != null)
                {
                    moveState = MoveState.BLOCKED_BY_FINISHING_MARBLE;
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
                if (lastSpot.hasMarble())
                    {
                      if (currentSpotType == SpotType.FINISHING)
                      {
                          moveState = MoveState.BLOCKED_BY_FINISHING_MARBLE;
                      }
                      else
                      {
                          moveState = MoveState.VALID_MARBLE_BUMP;
                          setMarblesForBump(board, lastSpot, currentSpot);
                      }
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
    private int ensurePositiveMoveTrackIndex(int index)
    {
        if (index < 0)
        {
            index += 72;
        }
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
            if (spotType != SpotType.STARTING)
            {
                say("Marble must be in the bank");
                moveState = MoveState.MARBLE_NOT_IN_BANK;
            }
            else
            {
                int homeSpotIndex = board.getHomeIndex(marble.getColor());
                Spot homeSpot = board.getSpot(homeSpotIndex);
                MarbleColor playerColor = player.getColor();
                if (homeSpot.hasMarble())
                    {
                        if (homeSpot.getMarble().getColor() == playerColor)
                        {
                            say("Home spot already occupied");
                            moveState = MoveState.HOME_SPOT_OCCUPIED;
                        }
                        else
                        {
                            // bumping marble from home spot
                            moveState = MoveState.VALID_MARBLE_BUMP;
                            setMarblesForBump(board, homeSpot, marbleSpot);
                        }
                    }
                else
                {
                    // we're validated -- can move this marble from bank to
                    // home.
                    say("move is good");
                    moveState = MoveState.VALID_MARBLE_START;
                    marbles.add(marbleSpot);
                    destinations.add(homeSpot);
                }
            }
        }
        return moveState;
    }

}
