package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JFrame;
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

    private boolean _JackIsPlayed = false;
    private int _MarbleToMove;
    private int _MarbleToMoveTo;
    private Card _Card;
    private CardButton _CardButton;

    public Alleys(Controller GameController)
    {
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

                    if (_Card.getRank() == CardValue.Jack)
                    {
                        _JackIsPlayed = true;
                    }
                    else
                    {
                        _JackIsPlayed = false;
                    }

                    checkAndPlayCard();
                }
            });

            _BoardPanel.setMarbleListener(new MarbleListener()
            {
                public void MarbleSelected(MarbleGraphic marbleGraphic)
                {
                    if (_MarbleToMove == -1)
                    {
                        _MarbleToMove = marbleGraphic.getMarbleIdNumber();
                        System.out.println("First Marble " + _MarbleToMove + " selected.");
                        checkAndPlayCard();
                    }
                    else if (_JackIsPlayed == true && _MarbleToMoveTo == -1)
                    {
                        _MarbleToMoveTo = marbleGraphic.getMarbleIdNumber();
                        System.out.println("Second Marble " + _MarbleToMoveTo + " selected.");
                        checkAndPlayCard();
                    }
                    else
                    {
                        System.out.println("Alleys: Too many marbles selected...");
                        _MarbleToMove = -1;
                        _MarbleToMoveTo = -1;
                    }
                }
            });

            InitUI();

            _Controller.begin();
            newTurn();
        }
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

    public void newTurn()
    {
        _MarbleToMove = -1;
        _MarbleToMoveTo = -1;
        _Card = null;
        _CardButton = null;

        // 1. Show player cards
        List<Card> PlayersCards = _Controller.getCurrentPlayersCards();

        // Sends cards to panel
        _CardPanel.displayCards(PlayersCards);

        System.out.println(_Controller.getCurrentPlayersName() + "'s turn.");
    }

    private void checkAndPlayCard()
    {
        // Check that cards and marbles have been selected. All cards require at least 1 card and 1 marble to be selected. Return early if this is not the case
        if (_Card == null)
        {
            return;
        }
        else if (_MarbleToMove == -1)
        {
            return;
        }

        if (_JackIsPlayed == true)
        {
            if (_MarbleToMoveTo == -1)
            {
                System.out.println("Alleys: (Jack) Select a second marble to move to");
                return;
            }

            // TODO need to add a method for playing a jack card on two marbles
            System.out.println(
                    "Alleys: Player is swapping marble " + _MarbleToMove + " with " + _MarbleToMoveTo + " using the " + _Card.toString());

            if (_Controller.currentPlayerPlaysJack(_Card, _MarbleToMove, _MarbleToMoveTo) == true)
            {
                _CardPanel.hideCard(_CardButton);
                newTurn();
            }
        }
        else
        {
            // Not playing a jack
            // User has played a card and a marble
            System.out.println("Alleys: Player playing the " + _Card.toString());

            if (_Controller.currentPlayerPlays(_Card, _MarbleToMove) == true)
            {
                _CardPanel.hideCard(_CardButton);
                newTurn();
            }
        }
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
