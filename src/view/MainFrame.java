package view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

public class MainFrame extends JFrame
{
    private final int MINIMUM_X = 600;
    private final int MINIMUM_Y = 500;

    public MainFrame()
    {
        super("Alleys Game");

        setLayout(new BorderLayout());

        add(new BoardComponent(), BorderLayout.CENTER);

        setMinimumSize(new Dimension(MINIMUM_X, MINIMUM_Y));
        setSize(MINIMUM_X, MINIMUM_Y);

        setVisible(true);
    }
}
