package view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Alleys extends JFrame
{
    private final int MINIMUM_X = 600;
    private final int MINIMUM_Y = 500;

    public Alleys()
    {
        InitUI();
    }
    
    private void InitUI()
    {
        setTitle("Alleys Game");
        setLayout(new BorderLayout());

        add(new BoardComponent(), BorderLayout.CENTER);

        setMinimumSize(new Dimension(MINIMUM_X, MINIMUM_Y));
        setSize(MINIMUM_X, MINIMUM_Y);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        setVisible(true);
    }
    
    public static void main(String[] args)
    {

        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                new Alleys();
            }
        });
    }
}


