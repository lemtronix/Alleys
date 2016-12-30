package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import controller.Controller;
import model.Card;

public class Alleys extends JFrame
{
    private final int MINIMUM_X = 600;
    private final int MINIMUM_Y = 700;

    private Thread _AlleysThread;
    private Controller _Controller;

    private BoardComponent _BoardPanel;
    private CardPanel _CardPanel;

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
                    int marbleIdPlayed = _BoardPanel.GetSelectedMarble();

                    if (marbleIdPlayed != -1 && card != null)
                    {
                        // User has played a card and a marble
                        System.out.println("Alleys: Player playing the " + card.toString());

                        if (_Controller.currentPlayerPlays(marbleIdPlayed, card) == true)
                        {
                            _CardPanel.hideCard(source);
                            newTurn();
                        }

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
        // 1. Show player cards
        List<Card> PlayersCards = _Controller.getCurrentPlayersCards();

        // Sends cards to panel
        _CardPanel.displayCards(PlayersCards);

        System.out.println(_Controller.getCurrentPlayersName() + " turn.");
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
