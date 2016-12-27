package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import controller.Controller;
import model.Card;

public class Alleys extends JFrame implements Runnable
{
    private final int MINIMUM_X = 600;
    private final int MINIMUM_Y = 650;

    private Thread _AlleysThread;
    private Controller _Controller;

    private BoardComponent _Board;

    public Alleys(Controller GameController, BoardComponent Board)
    {
        if (Board != null && GameController != null)
        {
            _Controller = GameController;
            _Board = Board;

            InitUI();

            // Start a new thread for handling game logic, separately from the GUI thread
            _AlleysThread = new Thread(this);
            _AlleysThread.setName("AlleysGameLogicThread");
            _AlleysThread.start();

            System.out.println("Thread completed.");
        }
    }

    private void InitUI()
    {
        setTitle("Alleys Game");
        setLayout(new BorderLayout());

        add(_Board, BorderLayout.CENTER);

        setMinimumSize(new Dimension(MINIMUM_X, MINIMUM_Y));
        setSize(MINIMUM_X, MINIMUM_Y);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void run()
    {
        _Controller.begin();

        for (;;)
        {
            int MarbleIdNumber = -1;
            _Board.clearSelectedMarble();

            // 1. Show player cards
            List<Card> PlayersCards = _Controller.getCurrentPlayersCards();

            System.out.println(_Controller.getCurrentPlayersName() + " turn.");

            for (int i = 0; i < PlayersCards.size(); i++)
            {
                System.out.println(i + ".) " + PlayersCards.get(i));
            }

            System.out.println("S.) Skip Turn.");

            // 2. Play a card
            System.out.println("Alleys: Select a card to play...");

            /// For now, just get a number from the console
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            String UserInput = "";
            Card PlayedCard = null;

            try
            {
                int UserInt = 0;

                UserInput = br.readLine();

                if (UserInput.toUpperCase().contains("S") == true)
                {
                    System.out.println("Alleys: Skipping Turn...");
                    _Controller.skipPlayerTurn();
                    continue;
                }
                else
                {
                    UserInt = Integer.parseInt(UserInput);
                }

                if (UserInt > PlayersCards.size())
                {
                    throw new IndexOutOfBoundsException();
                }

                PlayedCard = PlayersCards.get(UserInt);
            }
            catch (NumberFormatException nfe)
            {
                System.err.println("Alleys: Enter a number only!");
            }
            catch (IndexOutOfBoundsException ioobe)
            {
                System.err.println("Invalid number specified.");
            }
            catch (IOException e)
            {
                System.err.println("Alleys: Generic IOException!");
                e.printStackTrace();
            }

            // 2. Select a marble
            System.out.println("Alleys: Select a marble to move...");

            // Wait for the user to select a marble
            synchronized (_Board)
            {

                if (_Board.GetSelectedMarble() == null)
                {
                    try
                    {
                        // System.out.println("waiting for user to select marble.");
                        _Board.wait();
                        MarbleIdNumber = _Board.GetSelectedMarble().getMarbleIdNumber();
                        // System.out.println("Alleys: Marble selected " + MarbleIdNumber);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }

            // 4. Then move the marble
            if ((MarbleIdNumber != -1) && (PlayedCard != null))
            {
                _Controller.currentPlayerPlays(MarbleIdNumber, PlayedCard);
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
                BoardComponent board = new BoardComponent(controller);

                new Alleys(controller, board);
            }
        });
    }
}
