package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Alleys extends JFrame implements Runnable
{
    private final int MINIMUM_X = 600;
    private final int MINIMUM_Y = 500;
    
    private Thread _AlleysThread;
    private BoardComponent _Board;

    public Alleys(BoardComponent Board)
    {
        if (Board != null)
        {
            _Board = Board;
            InitUI();
            
            // Start a new thread for handling game logic, separately from the GUI thread
            _AlleysThread = new Thread(this);
            _AlleysThread.setName("AlleysGameLogicThread");
            _AlleysThread.start();
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
        Marble _SelectedMarble = null;
        
//        System.out.println("AlleysGame: Alleys thread started...");
        
        // 1. Select a marble
        System.out.println("AlleysGame: Select a marble to move...");
        
        synchronized(_Board)
        {
            if (_Board.GetSelectedMarble() == null)
            {
                try
                {
                    _Board.wait();
                    _SelectedMarble = _Board.GetSelectedMarble();
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
        
//        System.out.println("AlleysGame: Marble selected " + _SelectedMarble);
        
        // 2. Play a card
        System.out.println("AlleysGame: Select a card to play...");
        
        /// For now, just get a number from the console
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        try
        {
            String UserInput = br.readLine();
            int UserInt = Integer.parseInt(UserInput);
            System.out.println("User entered: " + UserInt);
            
            // 3. Test for valid move
            /// TODO For now, just always return true
            
            // 4. If valid, then move the marble
            /// TODO For now, move the marble to the appropriate spot.
            _Board.MoveMarbleToSpot(_SelectedMarble, UserInt);
        }
        catch (NumberFormatException nfe)
        {
            System.err.println("Alleys: Enter a number only!");
        }
        catch (IOException e)
        {
            System.err.println("Alleys: Generic IOException!");
            e.printStackTrace();
        }
        
//        System.out.println(_AlleysThread.getName() + " completed.");
    }
    
    public static void main(String[] args)
    {

        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                BoardComponent AlleysBoard = new BoardComponent();
                
                Alleys _AlleysGame = new Alleys(AlleysBoard);
            }
        });
    }
}


