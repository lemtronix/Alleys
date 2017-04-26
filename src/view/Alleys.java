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
import javax.swing.JLabel;
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
import model.Turn;
import model.TurnManager;
import view.viewinterface.AlleysUI;

public class Alleys extends JFrame implements AlleysUI
{
    private final int MINIMUM_X = 600;
    private final int MINIMUM_Y = 850;

//    private Controller _Controller;
    AlleysGame alleysGame;

    private BoardComponent _BoardPanel;
    private CardPanel _CardPanel;
    private JPanel topPanel;
    private JTextArea messageTextArea;
    private JScrollPane messagePane;

    private int _FirstMarbleSelected;
    private int _SecondMarbleSelected;
    private int _NumberOfSpotsToMoveFirstMarble;
    private Card _Card;
    private CardButton _CardButton;

    public Alleys(AlleysGame alleysGame) 
    {
        this.alleysGame = alleysGame;
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
                    List<Player> players = new ArrayList<>();
                    players.add(new Player("Bob", MarbleColor.RED, MarbleColor.YELLOW));
                    players.add(new Player("Carol", MarbleColor.BLUE, MarbleColor.GREEN));
                    players.add(new Player("Ted", MarbleColor.YELLOW, MarbleColor.RED));
                    players.add(new Player("Alice", MarbleColor.GREEN, MarbleColor.BLUE));
                    
                    alleysGame.startGame(players);
                    startGameButton.setEnabled(false);
                }
            }
        );
        buttonPanel.add(startGameButton);
        topPanel.add(buttonPanel, BorderLayout.WEST);
        
    //            JPanel messagePanel = new JPanel();
    //            messagePanel.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
        messageTextArea = new JTextArea(6, 0);
        messageTextArea.setMargin(new Insets(0,3,0,3));
        messagePane = new JScrollPane(messageTextArea);
    //            messagePanel.add(messagePane);
        topPanel.add(messagePane, BorderLayout.CENTER);
        message("info.welcome");
//        message("info.yourTurn", "Red");
    
        _CardPanel = new CardPanel();
        _CardPanel.setCardListener(new CardListener()
        {
    
            @Override
            public void cardsFolded()
            {
                System.out.println("Alleys: Player folding cards.");
                alleysGame.cardsFolded();
    //                    _Controller.skipPlayerTurn();
    //                    newTurn();
            }
    
            @Override
            public void cardPlayed(Card card)
            {
                _Card = card;
                alleysGame.cardChosen(card);
            }
         });
        
        initUI();
    }

//    public void newTurn()
    public void startTurn(Player player)
    {
//        resetSelectedCardsAndMarbles();
        List<Card> cards = player.getCards();
        _CardPanel.displayCardList(cards);

        message("info.newTurn", player.getName(), player.getColor().toString().toLowerCase());
        _CardPanel.setNameLabel(player.getName(), player.getColor());
    }

    private void resetSelectedCardsAndMarbles()
    {
        _FirstMarbleSelected = -1;
        _SecondMarbleSelected = -1;
        _NumberOfSpotsToMoveFirstMarble = -1;
        _Card = null;
        _CardButton = null;
    }

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

    public void getMoveCount()
    {
        String numberOfSpotsString = (String) JOptionPane.showInputDialog
                (_BoardPanel, "Enter number of spots to move this marble", "Split Seven?", JOptionPane.QUESTION_MESSAGE);
        int numberOfSpots = 0;
        try 
        { 
            numberOfSpots = Integer.parseInt(numberOfSpotsString);
            alleysGame.moveCountForSevenChosen(numberOfSpots);
        }
        catch (NumberFormatException nfe)
        {
            message("error.needNumber");
        }
    }
    
    
    private ResourceBundle messageBundle = ResourceBundle.getBundle("messages", Locale.getDefault());
    private String TEXT_AREA_FORMAT = "%s%n";
    
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
        catch (MissingResourceException mre) 
        { 
            message = "{" + messageKey + "}"; 
        }
        
        displayMessage = String.format(TEXT_AREA_FORMAT, message);
        messageTextArea.append(displayMessage);
    }
    
    public void setSpotMarble(int spotIndex, Marble marble)
    {
        _BoardPanel.setSpotMarble(spotIndex, marble);
        repaint();
    }
    
    public void moveMarble(int fromIndex, int toIndex)
    {
        SpotGraphic fromSpot = _BoardPanel.getSpot(fromIndex);
        Marble marble = fromSpot.getMarble();
        fromSpot.setMarble(null);

        SpotGraphic toSpot = _BoardPanel.getSpot(toIndex);
        toSpot.setMarble(marble);
        repaint();
    }
    
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

    @Override
    public void chooseCard(Card card)
    {
        moveCardToMiddle(card);
    }
    
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
