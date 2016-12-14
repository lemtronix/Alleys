package view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

public class MainFrame extends JFrame
{
    public MainFrame()
    {
        super("Alleys Game");
        
        setLayout(new BorderLayout());
        
        add(new BoardComponent(), BorderLayout.CENTER);
        
        setMinimumSize(new Dimension(500, 400));
        setSize(500, 400);

        setVisible(true);
    }
}
