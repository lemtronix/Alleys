package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Holds the overall 'turn state' of the program, including list of players, current
 * player and dealer, current turn, and logic for moving from one turn to the next.
 * @author rcook
 *
 */
public class TurnManager
{
    private List<Player>    players;            // we sort this list to marble color order
    private Integer         currentPlayerIndex;
    private Integer         currentDealerIndex;
    private DealManager     dealManager;
    
    private Messager        messager;
    private Board           board;
    
    private Turn            currentTurn;    public Turn getCurrentTurn() { return currentTurn; }
    
    private void say(String msg) { System.out.println(msg); }
    
    public TurnManager(Messager messager, Board board, List<Player> players)
    {
        this.board      = board;
        this.messager   = messager;
        
        this.players = new ArrayList<>(players);    // not clear making a copy of the list is necessary
                                                    // but I hate sorting the one we're passed.
        Collections.sort(this.players);      // ensure marble color order
        currentPlayerIndex  = null;
        currentDealerIndex  = null;
        dealManager         = new DealManager();
    }
    
    /**
     * Start a new turn in the game; currentDealerIndex == null indicates a brand new game.
     * This routine deals out more cards if people are out, and shuffles the deck when the
     * deck is out of cards. 
     * @return Player whose turn it is.
     */
    public Player startNewTurn()
    {
        if (currentDealerIndex == null) 
        {
            // new game being started; play starts left of the dealer
            dealManager.deal(players);
            currentDealerIndex = 0;
            currentPlayerIndex = 1;
        }
        else
        {
            currentPlayerIndex = getNextPlayerWithCards(currentPlayerIndex);
            if (currentPlayerIndex == null)
            {
                // everyone's out of cards, make a new deal.
                // deal manager handles differing card
                // counts per round and reshuffle when cards run out.
                dealManager.deal(players);
                // ok, everybody has cards; increment the dealer index, then choose
                // the first player in this new round.
                currentDealerIndex = getNextPlayerIndex(currentDealerIndex);
                currentPlayerIndex = getNextPlayerWithCards(currentDealerIndex);
            }
        }
        Player player = players.get(currentPlayerIndex);
        currentTurn = new Turn(player);
        return player;
    }
    
    /**
     * get the index of the next player who has any cards; this skips
     * players who have folded. Returns null if no one has any more 
     * cards (all cards are played). Can return the startIndex, if
     * only one player has any cards left.
     */
    private Integer getNextPlayerWithCards(Integer currentPlayerIndex)
    {
        Integer result = null;
        Integer nextPlayerIndex = currentPlayerIndex;
        
        // this loops through all players from an arbitrary starting point,
        // and ENDS with the player at the position given in the method
        // argument. That player might be the only one with any cards left,
        // but we must check for that after all other players.
        while (true)
        {
            // get the index of the next player in the group, then
            // check whether they have any cards.
            nextPlayerIndex = getNextPlayerIndex(nextPlayerIndex);
            Player nextPlayer = players.get(nextPlayerIndex);
            int cardCount = nextPlayer.getCards().size();
            if (cardCount > 0) 
            {   
                result = nextPlayerIndex;
                break;
            }
            else if (nextPlayerIndex.equals(currentPlayerIndex)) // true if we just checked for cards
            {                                                    // for the player originally passed to this method.
                break;                                           // and therefore we've now checked all players.
                // result will still be null;
            }
        }

        return result;
    }
    
    /**
     * get the next player's index, wrapping to start of list if
     * necessary.
     * @param startIndex
     * @return next player's index
     */
    public Integer getNextPlayerIndex(Integer startIndexObject)
    {
        int startIndex = startIndexObject.intValue();
        int result = startIndex + 1;
        if (result >= players.size()) { result = 0; }
        return new Integer(result);
    }
    
}
