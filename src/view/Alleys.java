package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import model.AlleysGame;
import model.Card;
import model.Marble;
import model.MarbleColor;
import model.Player;
import view.viewinterface.AlleysUI;

public class Alleys extends JFrame implements AlleysUI
{
    private final int MINIMUM_X = 600;
    private final int MINIMUM_Y = 850;


    private BoardComponent  _BoardPanel;
    private CardPanel       _CardPanel;
    private JPanel          topPanel;
    private JTextArea       messageTextArea;
    private JScrollPane     messagePane;

    /**
     * create this Alleys view.
     * @param alleysGame
     */
    public Alleys(AlleysGame alleysGame) 
    {
        _BoardPanel = new BoardComponent(alleysGame);
        
        topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel();
        JButton startGameButton = new JButton("Start Game");
        startGameButton.addActionListener
        (
            new ActionListener() 
            {
                public void actionPerformed(ActionEvent e)
                {
                    // TODO: create dialog for entering player info
                    Player bob      = new Player("Bob", MarbleColor.RED);     // , MarbleColor.YELLOW));
                    Player carol    = new Player("Carol", MarbleColor.BLUE);  // , MarbleColor.GREEN));
                    Player ted      = new Player("Ted", MarbleColor.YELLOW);  // , MarbleColor.RED));
                    Player alice    = new Player("Alice", MarbleColor.GREEN); // , MarbleColor.BLUE));
                    
                    setPartners(bob, ted);
                    setPartners(carol, alice);
                    
                    List<Player> players = new ArrayList<>();
                    players.add(bob);
                    players.add(carol);
                    players.add(ted);
                    players.add(alice);
                    alleysGame.startGame(players);
                    startGameButton.setEnabled(false);
                }
            }
        );
        buttonPanel.add(startGameButton);
        topPanel.add(buttonPanel, BorderLayout.WEST);
        
        messageTextArea = new JTextArea(6, 0);
        messageTextArea.setMargin(new Insets(0,3,0,3));
        messagePane = new JScrollPane(messageTextArea);
        topPanel.add(messagePane, BorderLayout.CENTER);
        message("info.welcome");
    
        _CardPanel = new CardPanel();
        _CardPanel.setCardListener(new CardListener()
        {
    
            @Override
            public void cardsFolded()
            {
                System.out.println("Alleys: Player folding cards.");
                alleysGame.cardsFolded();
            }
    
            @Override
            public void cardPlayed(Card card)
            {
                alleysGame.cardChosen(card);
            }
         });
        
        initUI();
    }
    
    private void setPartners(Player one, Player two)
    {
        one.setPartner(two);
        two.setPartner(one);
    }

    /**
     * start up a turn on the UI; display the given player's cards,
     * and set the name label on the board. What the heck, display
     * a message.
     */
    @Override
    public void startTurn(Player player)
    {
        List<Card> cards = player.getCards();
        _CardPanel.displayCardList(cards);

        message("info.newTurn", player.getName(), player.getColor().toString().toLowerCase());
        _CardPanel.setNameLabel(player.getName(), player.getColor());
    }

    /**
     * initialize this user interface.
     */
    private void initUI()
    {
        setTitle("Alleys Game");
        setLayout(new BorderLayout());

        add(topPanel,    BorderLayout.NORTH);
        add(_BoardPanel, BorderLayout.CENTER);
        add(_CardPanel,  BorderLayout.SOUTH);

        setMinimumSize(new Dimension(MINIMUM_X, MINIMUM_Y));
        setSize(MINIMUM_X, MINIMUM_Y);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    /**
     * display the dialog for entering the number of spots to move the first
     * marble chosen after a 7 is played.
     */
    @Override
    public int getMoveCount()
    {
        int moveCount = 0;
        boolean finished = false;
        while (!finished)
        {
            try 
            { 
                String moveCountString = (String) JOptionPane.showInputDialog
                        (_BoardPanel, 
                         "Enter number of spots to move this marble", 
                         "Split Seven?", 
                         JOptionPane.QUESTION_MESSAGE
                        );
                if (moveCountString == null)
                {
                    // user pressed cancel, what do we do now?
                    message("info.cancelled7");
                    moveCount = 0;
                    finished = true;
                }
                else
                {
                    moveCount = Integer.parseInt(moveCountString);
                    // no NFE, so we're done
                    finished = true;
                }
            }
            catch (NumberFormatException nfe)
            {
                message("error.count1to7");
            }
        }
        return moveCount;
    }
    
    // we keep our messages in this resource bundle, and centralize how we put the text into
    // our message area.
    private ResourceBundle messageBundle = ResourceBundle.getBundle("messages", Locale.getDefault());
    private String TEXT_AREA_FORMAT = "%s%n";
    
    @Override
    public void message(String messageKey)
    {
        String displayMessage;
        String message;
        try 
        { 
            message = messageBundle.getString(messageKey); 
        }
        catch (MissingResourceException mre) 
        { 
            message = "{" + messageKey + "}"; 
        } 
        displayMessage = String.format(TEXT_AREA_FORMAT, message);
        messageTextArea.append(displayMessage);
    }
    
    @Override
    public void message(String messageKey, String ... messageArguments)
    {
        String messageFormat;
        String message;
        String displayMessage;
        try 
        { 
            messageFormat = messageBundle.getString(messageKey);
            message = String.format(messageFormat, (Object[]) messageArguments);
        }
        catch (Exception exception) 
        { 
            // we get here if a programmer has misspelled a message key, or forgotten
            // to add that message to the message file; also, if the programmer has a
            // mismatch between a format specifier and the parameter passed.
            message = "{" + messageKey + "} : " + exception.getMessage(); 
        }
        
        displayMessage = String.format(TEXT_AREA_FORMAT, message);
        messageTextArea.append(displayMessage);
    }
    
    /**
     * put the given marble on the spot with the given index.
     */
    @Override
    public void setSpotMarble(int spotIndex, Marble marble)
    {
        _BoardPanel.setSpotMarble(spotIndex, marble);
        repaint();
    }
    
    /**
     * move the marble at the given fromIndex to the given toIndex.
     */
    @Override
    public void moveMarble(int fromIndex, int toIndex)
    {
        SpotGraphic fromSpot = _BoardPanel.getSpot(fromIndex);
        Marble marble = fromSpot.getMarble();
        fromSpot.setMarble(null);

        SpotGraphic toSpot = _BoardPanel.getSpot(toIndex);
        toSpot.setMarble(marble);
        repaint();
    }
    
    /**
     * swap the marbles at the two given indices.
     */
    @Override
    public void swapMarbles(int firstIndex, int secondIndex)
    {
        SpotGraphic firstSpot = _BoardPanel.getSpot(firstIndex);
        SpotGraphic secondSpot = _BoardPanel.getSpot(secondIndex);
        Marble firstMarble = firstSpot.getMarble();
        Marble secondMarble = secondSpot.getMarble();
        firstSpot.setMarble(secondMarble);
        secondSpot.setMarble(firstMarble);
        repaint();
    }

    /**
     * Show the given card as chosen on the UI
     */
    @Override
    public void chooseCard(Card card)
    {
        moveCardToMiddle(card);
    }
    
    /**
     * show a previously chosen card as going back to its hand
     * on the UI.
     */
    @Override
    public void unChooseCard()
    {
        CardGraphic graphic = retrieveCardFromMiddle();
        if (graphic != null)
        {
            _CardPanel.addCard(graphic);
        }
    }

    public static void main(String[] args)
    {

        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                AlleysGame alleysGame = AlleysGame.getNewInstance();
                Alleys alleys = new Alleys(alleysGame);
                alleysGame.setAlleysUI(alleys);
                alleys.setVisible(true);
            }
        });
    }

    // ===== handling played/discarded pile in the middle of the board

    ArrayList<CardGraphic>  playedPile         = new ArrayList<>();
    
    private void moveCardToMiddle(Card card)
    {
        // remove the card from the currently displayed player's cards
        _CardPanel.removeCard(card);
        
        CardGraphic cardGraphic = new CardGraphic(card);
        playedPile.add(cardGraphic);
        displayTopPlayed();
    }
    
    /**
     * removes the card from the middle of the board and returns it to
     * its own hand (oh, lord, where is that?)
     * @return
     */
    private CardGraphic retrieveCardFromMiddle()
    {
        // remove the card from the board panel and from the pile if there is one.
        CardGraphic cardGraphic = getTopPlayed();
        if (cardGraphic != null)
        {
            playedPile.remove(cardGraphic);
        }
        
        // if there's another card on the pile under that, make a label out of it and display it.
        displayTopPlayed();
        
        return cardGraphic;
    }
    
    /**
     * return the CardGraphic from the top of the played/discard pile, or null
     * @return
     */
    private CardGraphic getTopPlayed()
    {
        int size = playedPile.size();
        return (size > 0) 
                ? playedPile.get(size - 1)
                : null;
    }
    
    /**
     * display the top card on the played/discard pile, if there is one.
     * If one is currently displayed, remove it.
     */
    private void displayTopPlayed()
    {
        CardGraphic cardGraphic = getTopPlayed();
        if (cardGraphic != null)
        {
            ImageIcon icon = cardGraphic.getIcon();
            _BoardPanel.displayPlayedCard(icon);
        }
    }

//    private void checkAndPlayCard(Card card)
//    {
//        // Check that cards and marbles have been selected. All cards require at least 1 card and 1 marble to be selected. Return early if this is not
//        // the case
//        if (card == null)
//        {
//            return;
//        }
//        else if (_FirstMarbleSelected == -1)
//        {
//            return;
//        }
//        else if (isCardJack() && _SecondMarbleSelected == -1)
//        {
//            // Jacks always require a second marble
//            System.out.println("Alleys: Select a second marble");
//            return;
//        }
//        else if (isCardSeven() && _NumberOfSpotsToMoveFirstMarble == -1)
//        {
//            // Player has not input how many spots they'd like to move...
//            String numberOfSpots = (String) JOptionPane.showInputDialog(_BoardPanel,
//                    "How many spots would you like to move?", "Split Seven?", JOptionPane.QUESTION_MESSAGE);
//
//            try
//            {
//                _NumberOfSpotsToMoveFirstMarble = Integer.parseInt(numberOfSpots);
//
//                if (_NumberOfSpotsToMoveFirstMarble < 1 || _NumberOfSpotsToMoveFirstMarble > 7)
//                {
//                    resetSelectedCardsAndMarbles();
//                    throw new NumberFormatException();
//                }
//                else if (_NumberOfSpotsToMoveFirstMarble == 7)
//                {
//                    // If the player wants to move the first marble
//                    _SecondMarbleSelected = _FirstMarbleSelected;
//                }
//                else
//                {
//                    System.out.println("Alleys: Select a second marble");
//                }
//
//                System.out.println(
//                        "Alleys: User wants to move " + _NumberOfSpotsToMoveFirstMarble + " on their first marble.");
//            }
//            catch (NumberFormatException nfe)
//            {
//                System.err.println("Alleys: Please enter a number from 1 to 7");
//            }
//
//        }
//
//        if (isCardJack())
//        {
//            System.out.println("Alleys: Player is swapping marble " + _FirstMarbleSelected + " with "
//                    + _SecondMarbleSelected + " using the " + _Card.toString());
//
////            if (_Controller.currentPlayerPlaysJack(_Card, _FirstMarbleSelected, _SecondMarbleSelected) == true)
////            {
////                _CardPanel.hideCard(_CardButton);
////                newTurn();
////            }
//        }
//        else if (isCardSeven() && _SecondMarbleSelected != -1)
//        {
//            // if yes, then present the slider option
//
//            System.out.println("Alleys: Player is playing a seven with the first marble " + _FirstMarbleSelected
//                    + " moving " + _NumberOfSpotsToMoveFirstMarble + " number of spots and the second marble "
//                    + _SecondMarbleSelected + " using the remaining spots.");
//
////            if (_Controller.currentPlayerPlaysSeven(_Card, _FirstMarbleSelected, _NumberOfSpotsToMoveFirstMarble,
////                    _SecondMarbleSelected) == true)
////            {
////                _CardPanel.hideCard(_CardButton);
////                newTurn();
////            }
//        }
//        else if (!isCardSeven())
//        {
//            // Not playing a jack or seven
//            System.out.println("Alleys: Player playing the " + _Card.toString());
//
////            if (_Controller.currentPlayerPlays(_Card, _FirstMarbleSelected) == true)
////            {
////                _CardPanel.hideCard(_CardButton);
////                newTurn();
////            }
//        }
//    }
//
//    private boolean isCardJack()
//    {
//        if (_Card == null)
//        {
//            return false;
//        }
//
//        if (_Card.getRank() == CardValue.Jack)
//        {
//            return true;
//        }
//
//        return false;
//    }
//
//    private boolean isCardSeven()
//    {
//        if (_Card == null)
//        {
//            return false;
//        }
//
//        if (_Card.getRank() == CardValue.Seven)
//        {
//            return true;
//        }
//
//        return false;
//    }

}
