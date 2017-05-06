package model;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import view.viewinterface.AlleysUI;
import controller.Constants;

/**
 * The Alleys board.
 * <P>Represents the board and all the spots; each spot has an index which is 
 * all that is needed to determine its position relative to other spots and its type
 * (see SpotType). View components may depend on these indices.
 * 
 * <ol>
 * <li>72 (0-71) spots around the 'loop' of the board, starting with Red's home
 * spot; as colors are involved on each side of the board, the colors 
 * follow MarbleColor order. <P>The index of 0 is, therefore, the first color's home spot; 
 * index 17 is the next color's home spot, etc.
 * <li>16 spots (4 spots per marble color) as finishing spots, in MarbleColor order
 * <li>16 spots (4 spots per marble color) as starting spots, in MarbleColor order
 * 
 * <P>The view is expected to organize its spots according to these same numbers;
 * for example, the 5th spot in both model and any view is the first starting spot
 * for the second color in MarbleColor order.
 * 
 * @author rcook
 *
 */
public class Board
{
    private static void say(String msg) { System.out.println(msg); }
    private static void say(String format, Object ... args) { say(String.format(format, args)); }
    
    private AlleysUI alleysUI = null;   
    private Messager messager = null;
    
    private Spot[] allSpots = new Spot[Constants.TOTAL_SPOTS];
    private Map<MarbleColor, Integer> firstBankSpots = new HashMap<>();
    private Map<MarbleColor, Integer> homeSpots          = new HashMap<>();
    private Map<MarbleColor, Integer> firstHomebaseSpots = new HashMap<>();
    
    // for each color, an array of ints indicating the current position of
    // each marble for that color. We update this as we move the marbles.
    private EnumMap<MarbleColor, int[]> allMarbleSpots = new EnumMap<MarbleColor, int[]>(MarbleColor.class);
    
    // A moveTrack is an Integer array, for one color, holding the indices
    // from the allSpots array which define the movement track for that color.
    // It starts from the starting spot, moves around the board to the homebase
    // spots, and includes those. This facilitates computing where a move of 
    // a given number of spots will take a marble, since each color of marble
    // has a different track, and they're mostly non-contiguous on the allSpots
    // array.
    private Map<MarbleColor, Integer[]> moveTracks          = new HashMap<>();
    
    // this 3d array is used to fill in the moveTracks; each pair of numbers
    // represents a sequence of indices into the allSpots array.
    private Integer[][][] moveTrackSequences= 
        {
             { { 0,   75} }                             // red      00-75
            ,{ { 18, 71 }, {0, 17}, { 76, 79 } }        // blue     18-71, 00-17, 76-79
            ,{ { 36, 71 }, {0, 35}, {80, 83 } }         // yellow   36-71, 00-35, 80-83 
            ,{ { 54, 71 }, { 0, 53}, {84, 87 }}         // green    54-71, 00-53, 84-87
        };
    
    public Board(Messager messager) { this.messager = messager; }
    
    public void initializeBoard()
    {
        int spotIndex = 0;
        
        // ==========-------------
        // the next 3 for loops each create different types of spot each and add them to
        // one array that holds all the spots on the board.

        // for each color, create the starting spot, then the spots between it
        // and the next color's starting spot.
        for (MarbleColor color: MarbleColor.values())
        {
            homeSpots.put(color, spotIndex);        // save the home spot for each color
            allSpots[spotIndex] = new Spot(spotIndex, SpotType.STARTINGSPOT, color);
            spotIndex++;
            // put spots between one home marble and the next on the board's loop
            for (int i=0; i<17; i++)
            {
                allSpots[spotIndex] = new Spot(spotIndex, SpotType.NORMAL);
                spotIndex++;
            }
        }
        
        // for each color, create 4 homebase spots
        for (MarbleColor color: MarbleColor.values())
        {
            firstHomebaseSpots.put(color, spotIndex);  // save first homebase spot
            for (int i=0; i<4; i++)
            {
                allSpots[spotIndex] = new Spot(spotIndex, SpotType.HOMEBASE, color);
                spotIndex++;
            }
        }

        // for each color, create 4 bank spots
        for (MarbleColor color: MarbleColor.values())
        {
            firstBankSpots.put(color, spotIndex); // save first bank spot for each color
            for (int i=0; i<4; i++)
            {
                allSpots[spotIndex] = new Spot(spotIndex, SpotType.BANK, color);
                spotIndex++;
            }
        }

        // end of creating universal array of spots on the board
        // ==========-------------

        // a 'move track' is a subset of the spots on the board defining the 
        // path for marbles of one color. This simplifies move calculations,
        // since each color starts at a different spot and can move into
        // its own finishing spots but not another color's finishing spots.
        // initializing move tracks; each covers 76 spots
        for (MarbleColor color: MarbleColor.values())
        {
            Integer[] trackSequence = new Integer[76];
            int currentIndex = 0;
            Integer[][] colorSequences = moveTrackSequences[color.ordinal()];
            for (int i=0; i<colorSequences.length; i++)
            {
                Integer[] sequence = colorSequences[i];
                for (int j=0; j<sequence.length; j=j+2)
                {
                    int sequenceStart = sequence[0];
                    int sequenceEnd   = sequence[1];
                    for (int k=sequenceStart; k<=sequenceEnd; k++)
                    {
//                        System.out.println(color + " " + k);
                        trackSequence[currentIndex++] = k;
                    }
                }
                moveTracks.put(color, trackSequence);
            }
        }
        
        // now create empty arrays that will hold the current positions of each of the 
        // marbles on the board. We'll use these when we start to determine if a player has
        // any legal moves.
        for (MarbleColor color : MarbleColor.values())
        {
            int[] positions = new int[4];
            allMarbleSpots.put(color, positions);
        }
        
    }
    
    public int getFirstStartingIndex (MarbleColor color)  { return firstBankSpots.get(color); }
    public int getStartingIndex          (MarbleColor color)  { return homeSpots.get(color); }
    public int getFirstFinishingIndex(MarbleColor color)  { return firstHomebaseSpots.get(color); }
    
    public void setAlleysUI(AlleysUI aui)               { alleysUI = aui;               }
    public Spot getSpot(int spotIndex)                  { return allSpots[spotIndex];   }
    public Integer[] getMoveTrack(MarbleColor color)    { return moveTracks.get(color); }
    
    public Integer getMoveTrackIndex(MarbleColor color, int allSpotsIndex)
    {
        int result = -1;
        Integer[] moveTrack = moveTracks.get(color);
        for (int i=0; i<moveTrack.length; i++)
        {
            if (allSpotsIndex == moveTrack[i])
            {
                result = i;
                break;
            }
        }
        return result;
    }
   
//   /**
//     * Return the allSpots array index that would be moved to after moving the indicated number of spots.
//     * The number of spots may be positive or negative. If the move would take the marble outside
//     * the bounds of the track of moves for this color, this returns -1.
//     * <P>A 'moveTrack' is an array of integers; each position in the moveTrack represents
//     * a position that a marble moves in its trip around the board, and the integer at that
//     * index is the position of the corresponding spot in the 'allSpots' array. Each color
//     * has its own moveTrack, starting at its home spot, traveling around the board, and
//     * going through its homebase spots.
//     * <P>A marble moving backwards from its own starting spot crosses to the end of the normal
//     * spots in its track, i.e., the spot before the finishing spots for that color. This 
//     * method allows for that.
//     * @param moveFrom: an index into an allSpots array or list
//     * @param numberToMove: number of spots to be moved, may be positive or negative
//     * @return an index into allSpots at which the move would finish
//     */
//    public int moveIndex(MarbleColor color, int moveFromIndex, int numberToMove)
//    {
//        int result = -1;
//        // our moveFromIndex is the index into the allSpots array. Get the
//        // track sequence for this color, and search it for that index.
//        // (the track sequence is an integer array of allSpots indices).
//        Integer[] moveTrack = moveTracks.get(color);
//        int i = 0;
//        while (i<moveTrack.length && moveTrack[i] != moveFromIndex) { i++; }
//        // If we didn't find the index, something is badly wrong;
//        // it means the moveFromIndex we were given is not in the moveTrack
//        // array, i.e., that the marble was outside the track somehow. In that case
//        // we just return -1, so all the processing below is only done if we
//        // found the moveFromIndex on the track.
//        if (i < moveTrack.length)
//        {
//            i = i + numberToMove;
//            // if the index is now past the end of moveTrack, the marble is trying to
//            // move too far; there's no reason to support this, it is not an operation
//            // that is ever done in the game, so we only continue to process if the
//            // resulting index is within the array.
//            if (i < moveTrack.length)
//            {
//                if (i < 0)
//                {
//                    // this is legal -- the marble is moving backwards through its starting spot.
//                    // So we need to calculate the main loop spot to which it would move.
//                    // -1 would correspond to the last normal spot before finishing spots, which
//                    // would be in moveTrack position 71 (positions 72-75 are occupied by
//                    // finishing spots); so we add i to 72 to get the moveTrack position
//                    // to which the negative amount corresponds.
//                    result = moveTrack[i + 72];
//                } 
//                else 
//                {
//                    result = moveTrack[i];
//                }
//            }
//        }
//        return result;
//    }
    
    /**
     * put marbles in their starting positions on the board.
     */
    public void setUpMarbles(List<Player> players)
    {
        for (Player player : players)
        {
            MarbleColor color = player.getColor();
            int startStartingSpotsIndex = getFirstStartingIndex(color);
            for (int i=0; i<4; i++)
            {
                int spotIndex = startStartingSpotsIndex + i;
                Marble marble = new Marble(color);
                Spot spot = allSpots[spotIndex];
                spot.setMarble(new Marble(color));
                alleysUI.setSpotMarble(spotIndex, marble);
                
                allMarbleSpots.get(color)[i] = spotIndex;      // set initial position of the marble.
            }
        }
    }
    
    /**
     * Once a turn is validated and ready to move, execution comes here to actually do
     * the moving.
     * @param turn
     */
    public void executeTurn(Turn turn)
    {
        MoveState   moveState = turn.getMoveState();
        
        switch(moveState)
        {
        case VALID_MARBLE_START:
        case VALID_MARBLE_MOVE:
        case VALID_MARBLE_BUMP:     // we implement bump by moving the bumped marble first.
            {
                List<Spot> marbles      = turn.getMarbles();
                List<Spot> destinations = turn.getDestinations();
                int i=0;
                while (i < marbles.size())
                {
                    Spot start = marbles.get(i);
                    Spot end   = destinations.get(i);
                    SpotType endType = end.getSpotType();
                    moveMarble(start.getSpotIndex(), end.getSpotIndex());
                    start.setProtectedMarble(false);
                    // if this is a valid start move, or a start move that bumps a marble,
                    // then the ending marble/spot is protected.
                    boolean endProtected = (endType == SpotType.STARTINGSPOT
                                            && (   moveState == MoveState.VALID_MARBLE_START
                                                || moveState == MoveState.VALID_MARBLE_BUMP)
                                           );
                    end.setProtectedMarble(endProtected);
                    
                    i++;
                }
            }
            break;
        case VALID_MARBLE_SWAP:
            {
                List<Spot> marbles = turn.getMarbles();
                swapMarbles(marbles.get(0), marbles.get(1));
            }
            break;
        default:
            messager.message("Internal logic error, " + moveState.name() + " not yet implemented");
            break;
        }
        
        // if all the marbles this player was playing are now in homebase,
        // and the flag for that color marble is not set, then we 
        // just completed moving all this color into homebase.  
        // If he has a partner, and both are not done, then he switches
        // to moving his partner's marbles. If they're both done, the game
        // is over.
        // If he doesn't have a partner, completion means the game is over.
        
        // is player playing his own marbles?
        Player player = turn.getPlayer();
        MarbleColor playingColor = player.getPlayingColor();
        MarbleColor ownColor     = player.getColor();
        if (ownColor != playingColor)
        {
            // player has been playing his partner's marbles;
            // if they're all in, game is over.
            boolean allMarblesIn = areAllMarblesIn(playingColor);
            if (allMarblesIn) { messager.message("info.gameOver"); }
        }
        else
        {
            // player has been playing his own marbles.
            // since he just moved one, he wasn't done at the start of his turn.
            // is he done now?
            boolean allMarblesIn = areAllMarblesIn(ownColor);   // look at all the marble positions for this color
            if (allMarblesIn)
            {
                // if he has no partner, the game is over
                Player partner = player.getPartner();
                if (partner == null)
                { 
                    // game is over
                    messager.message("info.gameOver", player.getName());
                }
                else // we have to see if the partner's marbles are also all in.
                {
                    if (partner.getAllMarblesIn())
                    {
                        // game is over
                        messager.message("info.gameOver", player.getName());
                    }
                    else
                    {
                        player.setAllMarblesIn(true); 
                        messager.message("info.nowPlayingPartnersMarbles", player.getName(), player.getPartner().getName());
                    }
                }
            }
        }
    }
    
    private boolean areAllMarblesIn(MarbleColor color)
    {
        int[] marbleSpots = allMarbleSpots.get(color);
        int marblesIn = 0;
        for (int spotIndex : marbleSpots)
        {
            Spot spot = allSpots[spotIndex];
            if (spot.getSpotType() == SpotType.HOMEBASE) { marblesIn++; }
        }
        return (marblesIn == 4);
    }
    
    /**
     * move a marble from one spot index to another, updating the marble's 
     * position.
     * @param fromIndex
     * @param toIndex
     */
    private void moveMarble(int fromIndex, int toIndex) throws IllegalArgumentException
    {
        Spot fromSpot = allSpots[fromIndex];
        Spot toSpot = allSpots[toIndex];

        Marble marble = fromSpot.getMarble();
        
        fromSpot.setMarble(null);
        toSpot.setMarble(marble);
        
        // update the array we keep of marble positions.
        updateCurrentPosition(marble.getColor(), fromIndex, toIndex);

        // update the view
        alleysUI.moveMarble(fromIndex, toIndex);
        
    }
    
    /**
     * this does the swap for the move of a jack, after everything is validated.
     * @param firstSpot
     * @param secondSpot
     */
    private void swapMarbles(Spot firstSpot, Spot secondSpot)
    {
        int firstIndex = firstSpot.getSpotIndex();
        int secondIndex = secondSpot.getSpotIndex();
        Marble firstMarble = firstSpot.getMarble();
        Marble secondMarble = secondSpot.getMarble();
        MarbleColor firstColor = firstMarble.getColor();
        MarbleColor secondColor = secondMarble.getColor();
        secondSpot.setMarble(firstMarble);
        firstSpot.setMarble(secondMarble);
        firstSpot.setProtectedMarble(false);
        secondSpot.setProtectedMarble(false);
        updateCurrentPosition(firstColor, firstIndex, secondIndex);
        updateCurrentPosition(secondColor, secondIndex, firstIndex);
        alleysUI.swapMarbles(firstIndex, secondIndex);
    }
    
    /**
     * update the position of a marble of the given color that is moving
     * from one spot to another, given as indices. NOTE: The marble may no
     * longer BE at the 'from' spot; since this method is used internally during
     * moves, the marble may have already moved, though the currentPosition
     * array still thinks it is at that spot.
     * @param color
     * @param from
     * @param to
     */
    private void updateCurrentPosition(MarbleColor marbleColor, int previousIndex, int newIndex)
    {
        int[] currentPositions = allMarbleSpots.get(marbleColor);
        int positionIndex = getCurrentPositionIndex(currentPositions, previousIndex);
        currentPositions[positionIndex] = newIndex;
        say("Marble moved from %d to %d", previousIndex, newIndex);
    }
    
    /**
     * get the index into the currentPositions array for the marble with the given
     * color at the given allSpots index.
     * @param currentPositions
     * @param color
     * @param index
     * @return
     */
    private int getCurrentPositionIndex(int[] currentPositions, int index)
    {
        int j = 0;
        while (currentPositions[j] != index) { j++; if (j >= currentPositions.length) { break; }}
        if (j >= currentPositions.length){ throw new RuntimeException("internal error: marble position not in currentPositions array"); }
        return j;
    }
    
    /**
     * find the first empty bank spot so we can return a poor bumped marble there.
     * @param color
     * @return
     */
    public Spot findFirstEmptyBankSpot(MarbleColor color)
    {
        Spot resultSpot = null;
        int firstIndex = getFirstStartingIndex(color);
        for (int i=0; i<4; i++)
        {
            resultSpot = allSpots[firstIndex + i];
            if (!resultSpot.hasMarble()) { break; }
        }
        return resultSpot;
    }

}
