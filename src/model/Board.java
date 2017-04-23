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
    private Map<MarbleColor, Integer> firstStartingSpots = new HashMap<>();
    private Map<MarbleColor, Integer> homeSpots          = new HashMap<>();
    private Map<MarbleColor, Integer> firstFinishingSpots = new HashMap<>();
    
    // for each color, an array of ints indicating the current position of
    // each marble for that color. We update this as we move the marbles.
    private EnumMap<MarbleColor, int[]> marbleSpots = new EnumMap<MarbleColor, int[]>(MarbleColor.class);
    
    // Now we have a map of moveTrack arrays.
    // a moveTrack is an Integer array corresponding to one color; 
    // each item in the array represents one spot that a marble of that color
    // can move to. So each array starts with the home spot for that color, 
    // travels around the board until the spot just before the home spot, then
    // moves up through the finishing spots for that color. This way, when
    // a marble of a given color is going to move a certain number of spots,
    // it corresponds to a certain number of spots in its moveTrack. See
    // the moveIndex() method.
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

        // for each color, create the home spot, then the spots between it
        // and the next color's home spot.
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
        
        // for each color, create 4 finishing spots
        for (MarbleColor color: MarbleColor.values())
        {
            firstFinishingSpots.put(color, spotIndex);  // save first finishing spot
            for (int i=0; i<4; i++)
            {
                allSpots[spotIndex] = new Spot(spotIndex, SpotType.HOMEBASE, color);
                spotIndex++;
            }
        }

        // for each color, create 4 starting spots
        for (MarbleColor color: MarbleColor.values())
        {
            firstStartingSpots.put(color, spotIndex); // save first starting spot for each color
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
        
        // now initialize the arrays that will hold the current positions of each of the 
        // marbles on the board
        for (MarbleColor color : MarbleColor.values())
        {
            int[] positions = new int[4];
            marbleSpots.put(color, positions);
        }
        
    }
    
    public int getFirstStartingIndex (MarbleColor color)  { return firstStartingSpots.get(color); }
    public int getHomeIndex          (MarbleColor color)  { return homeSpots.get(color); }
    public int getFirstFinishingIndex(MarbleColor color)  { return firstFinishingSpots.get(color); }
    
    public void setAlleysUI(AlleysUI aui)
    {
        alleysUI = aui;
    }
    
    public Spot getSpot(int spotIndex)
    {
        return allSpots[spotIndex];
    }
    
    public Integer[] getMoveTrack(MarbleColor color)
    {
        return moveTracks.get(color);
    }
    
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
   
   /**
     * Return the allSpots array index that would be moved to after moving the indicated number of spots.
     * The number of spots may be positive or negative. If the move would take the marble outside
     * the bounds of the track of moves for this color, this returns -1.
     * <P>A 'moveTrack' is an array of integers; each position in the moveTrack represents
     * a position that a marble moves in its trip around the board, and the integer at that
     * index is the position of the corresponding spot in the 'allSpots' array. Each color
     * has its own moveTrack, starting at its home spot, traveling around the board, and
     * going through its finishing spots.
     * <P>A marble moving backwards from its own home spot crosses to the end of the normal
     * spots in its track, i.e., the spot before the finishing spots for that color. This 
     * method allows for that.
     * @param moveFrom: an index into an allSpots array or list
     * @param numberToMove: number of spots to be moved, may be positive or negative
     * @return an index into allSpots at which the move would finish
     */
    public int moveIndex(MarbleColor color, int moveFromIndex, int numberToMove)
    {
        int result = -1;
        // our moveFromIndex is the index into the allSpots array. Get the
        // track sequence for this color, and search it for that index.
        // (the track sequence is an integer array of allSpots indices).
        Integer[] moveTrack = moveTracks.get(color);
        int i = 0;
        while (i<moveTrack.length && moveTrack[i] != moveFromIndex) { i++; }
        // If we didn't find the index, something is badly wrong;
        // it means the moveFromIndex we were given is not in the moveTrack
        // array, i.e., that the marble was outside the track somehow. In that case
        // we just return -1, so all the processing below is only done if we
        // found the moveFromIndex on the track.
        if (i < moveTrack.length)
        {
            i = i + numberToMove;
            // if the index is now past the end of moveTrack, the marble is trying to
            // move too far; there's no reason to support this, it is not an operation
            // that is ever done in the game, so we only continue to process if the
            // resulting index is within the array.
            if (i < moveTrack.length)
            {
                if (i < 0)
                {
                    // this is legal -- the marble is moving backwards through its home spot.
                    // So we need to calculate the main loop spot to which it would move.
                    // -1 would correspond to the last normal spot before finishing spots, which
                    // would be in moveTrack position 71 (positions 72-75 are occupied by
                    // finishing spots); so we add i to 72 to get the moveTrack position
                    // to which the negative amount corresponds.
                    result = moveTrack[i + 72];
                } 
                else 
                {
                    result = moveTrack[i];
                }
            }
            // TODO: is this the best place to check for marbles we cannot move past? 
        }
        return result;
    }
    
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
                
                marbleSpots.get(color)[i] = spotIndex;      // set initial position of the marble.
            }
        }
    }
    
    public void executeTurn(Turn turn)
    {
        MoveState   moveState           = turn.getMoveState();
        
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
                    moveMarble(start.getSpotIndex(), end.getSpotIndex());
                    start.setProtectedMarble(false);
                    // if this is a valid start move, then the marble/spot is protected.
                    boolean endProtected = (moveState == MoveState.VALID_MARBLE_START);
                    end.setProtectedMarble(endProtected);
                    // update marble positions
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
            messager.message(moveState.name() + " not yet implemented");
            break;
        }
        
        // remove card from player's hand
        Player player = turn.getPlayer();
        Card card = turn.getCard();
        player.removeCard(card);
        
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
     * longer BE at the 'from' spot; this method is used internally during
     * moves.
     * @param color
     * @param from
     * @param to
     */
    private void updateCurrentPosition(MarbleColor marbleColor, int previousIndex, int newIndex)
    {
        int[] currentPositions = marbleSpots.get(marbleColor);
        int positionIndex = getCurrentPositionsIndex(currentPositions, marbleColor, previousIndex);
        currentPositions[positionIndex] = newIndex;
        say("Marble moved from %d to %d", previousIndex, newIndex);
    }
    
    private int getCurrentPositionsIndex(int[] currentPositions, MarbleColor color, int index)
    {
        int j = 0;
        while (currentPositions[j] != index) { j++; if (j >= currentPositions.length) { break; }}
        if (j >= currentPositions.length){ throw new RuntimeException("internal error: marble position not in currentPositions array"); }
        return j;
    }
    
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
