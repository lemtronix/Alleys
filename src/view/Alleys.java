package view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Alleys extends JFrame
{
    private final int MINIMUM_X = 600;
    private final int MINIMUM_Y = 500;
    
    private BoardComponent _Board;

    public Alleys(BoardComponent Board)
    {
        if (Board != null)
        {
            _Board = Board;
            InitUI();
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
    
    public void PlayGame()
    {
        // Select a marble
        System.out.println("Select a marble to move.");
        
        // TODO Is this correct?  See Java Tetris
//        while (_Board.GetSelectedMarble() == null)
//        {
//            
//        }
        // Play a card
        
        // Test for valid move
        // If valid, then move the marble
    }
    public static void main(String[] args)
    {

        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                BoardComponent _AlleysBoard = new BoardComponent();
                
                Alleys _AlleysGame = new Alleys(_AlleysBoard);
                _AlleysGame.PlayGame();
                
            }
        });
    }
}


