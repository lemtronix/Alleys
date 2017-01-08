package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import controller.Controller;
import model.Card;
import model.CardValue;

public class Alleys extends JFrame
{
    private final int MINIMUM_X = 600;
    private final int MINIMUM_Y = 750;

    private Controller _Controller;

    private BoardComponent _BoardPanel;
    private CardPanel _CardPanel;

    private int _FirstMarbleSelected;
    private int _SecondMarbleSelected;
    private int _NumberOfSpotsToMoveFirstMarble;
    private Card _Card;
    private CardButton _CardButton;

    public Alleys(Controller GameController) {
        if (GameController != null)
        {
            _Controller = GameController;
            _BoardPanel = new BoardComponent(_Controller);

            _CardPanel = new CardPanel();
            _CardPanel.setCardListener(new CardListener()
            {

                @Override
                public void cardsFolded()
                {
                    System.out.println("Alleys: Player folding cards.");
                    _Controller.skipPlayerTurn();
                    newTurn();
                }

                @Override
                public void cardPlayed(CardButton source, Card card)
                {
                    _CardButton = source;
                    _Card = card;

                    checkAndPlayCard();
                }
            });

            _BoardPanel.setMarbleListener(new MarbleListener()
            {
                public void MarbleSelected(MarbleGraphic marbleGraphic)
                {
                    if (_FirstMarbleSelected == -1)
                    {
                        _FirstMarbleSelected = marbleGraphic.getMarbleIdNumber();
                        System.out.println("First Marble " + _FirstMarbleSelected + " selected.");
                        checkAndPlayCard();
                    }
                    else if (_Card == null)
                    {
                        System.out.println("Alleys: Please select a card before selecting a second marble.");
                        return;
                    }
                    else if ((isCardJack() || isCardSeven()) && _SecondMarbleSelected == -1)
                    {
                        // Jacks and Sevens require two marbles to be selected
                        _SecondMarbleSelected = marbleGraphic.getMarbleIdNumber();
                        System.out.println("Second Marble " + _SecondMarbleSelected + " selected.");
                        checkAndPlayCard();
                    }
                    else
                    {
                        System.out.println("Alleys: Too many marbles selected...");
                        resetSelectedCardsAndMarbles();
                    }
                }
            });

            InitUI();

            _Controller.begin();
            newTurn();
        }
    }

    public void newTurn()
    {
        resetSelectedCardsAndMarbles();

        // 1. Show player cards
        List<Card> PlayersCards = _Controller.getCurrentPlayersCards();

        // Sends cards to panel
        _CardPanel.displayCards(PlayersCards);

        System.out.println(_Controller.getCurrentPlayersName() + "'s turn.");
    }

    private void resetSelectedCardsAndMarbles()
    {
        _FirstMarbleSelected = -1;
        _SecondMarbleSelected = -1;
        _NumberOfSpotsToMoveFirstMarble = -1;
        _Card = null;
        _CardButton = null;
    }

    private void InitUI()
    {
        setTitle("Alleys Game");
        setLayout(new BorderLayout());

        add(_BoardPanel, BorderLayout.CENTER);
        add(_CardPanel, BorderLayout.SOUTH);

        setMinimumSize(new Dimension(MINIMUM_X, MINIMUM_Y));
        setSize(MINIMUM_X, MINIMUM_Y);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void checkAndPlayCard()
    {
        // Check that cards and marbles have been selected. All cards require at least 1 card and 1 marble to be selected. Return early if this is not
        // the case
        if (_Card == null)
        {
            return;
        }
        else if (_FirstMarbleSelected == -1)
        {
            return;
        }
        else if (isCardJack() && _SecondMarbleSelected == -1)
        {
            // Jacks always require a second marble
            System.out.println("Alleys: Select a second marble");
            return;
        }
        else if (isCardSeven() && _NumberOfSpotsToMoveFirstMarble == -1)
        {
            // Player has not input how many spots they'd like to move...
            String numberOfSpots = (String) JOptionPane.showInputDialog(_BoardPanel,
                    "How many spots would you like to move?", "Split Seven?", JOptionPane.QUESTION_MESSAGE);

            try
            {
                _NumberOfSpotsToMoveFirstMarble = Integer.parseInt(numberOfSpots);

                if (_NumberOfSpotsToMoveFirstMarble < 1 || _NumberOfSpotsToMoveFirstMarble > 7)
                {
                    resetSelectedCardsAndMarbles();
                    throw new NumberFormatException();
                }
                else if (_NumberOfSpotsToMoveFirstMarble == 7)
                {
                    // If the player wants to move the first marble
                    _SecondMarbleSelected = _FirstMarbleSelected;
                }
                else
                {
                    System.out.println("Alleys: Select a second marble");
                }

                System.out.println(
                        "Alleys: User wants to move " + _NumberOfSpotsToMoveFirstMarble + " on their first marble.");
            }
            catch (NumberFormatException nfe)
            {
                System.err.println("Alleys: Please enter a number from 1 to 7");
            }

        }

        if (isCardJack())
        {
            System.out.println("Alleys: Player is swapping marble " + _FirstMarbleSelected + " with "
                    + _SecondMarbleSelected + " using the " + _Card.toString());

            if (_Controller.currentPlayerPlaysJack(_Card, _FirstMarbleSelected, _SecondMarbleSelected) == true)
            {
                _CardPanel.hideCard(_CardButton);
                newTurn();
            }
        }
        else if (isCardSeven() && _SecondMarbleSelected != -1)
        {
            // if yes, then present the slider option

            System.out.println("Alleys: Player is playing a seven with the first marble " + _FirstMarbleSelected
                    + " moving " + _NumberOfSpotsToMoveFirstMarble + " number of spots and the second marble "
                    + _SecondMarbleSelected + " using the remaining spots.");

            if (_Controller.currentPlayerPlaysSeven(_Card, _FirstMarbleSelected, _NumberOfSpotsToMoveFirstMarble,
                    _SecondMarbleSelected) == true)
            {
                _CardPanel.hideCard(_CardButton);
                newTurn();
            }
        }
        else if (!isCardSeven())
        {
            // Not playing a jack or seven
            System.out.println("Alleys: Player playing the " + _Card.toString());

            if (_Controller.currentPlayerPlays(_Card, _FirstMarbleSelected) == true)
            {
                _CardPanel.hideCard(_CardButton);
                newTurn();
            }
        }
    }

    private boolean isCardJack()
    {
        if (_Card == null)
        {
            return false;
        }

        if (_Card.getRank() == CardValue.Jack)
        {
            return true;
        }

        return false;
    }

    private boolean isCardSeven()
    {
        if (_Card == null)
        {
            return false;
        }

        if (_Card.getRank() == CardValue.Seven)
        {
            return true;
        }

        return false;
    }

    public static void main(String[] args)
    {

        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                Controller controller = new Controller();

                new Alleys(controller);
            }
        });
    }
}
