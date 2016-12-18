package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;

public class BoardComponent extends JComponent
{
    private final int MaxNumberOfSpots = 64;
    private final int MaxNumberOfMarbles = 4;

    private final int NewDirection = 16;

    private Spot _Spots[] = new Spot[MaxNumberOfSpots];
    private Marble _Marbles[] = new Marble[MaxNumberOfMarbles];
    
    private Marble _SelectedMarble;

    public BoardComponent()
    {
        CreateSpots();
        CreateMarbles();

        addMouseListener(new MouseAdapter()
        {
            public void mousePressed(MouseEvent e)
            {
                CheckMarbleSelected();
                repaint();
            }
            public void mouseReleased(MouseEvent e)
            {
                CheckSelectedMarbleDroppedOnSpot();
                repaint();
            }
        });
        
        addMouseMotionListener(new MouseMotionListener()
        {
            public void mouseMoved(MouseEvent e)
            {
            }

            public void mouseDragged(MouseEvent e)
            {
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background
        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        UpdateSpots(g2d);
        UpdateMarbles(g2d);
    }

    private void CreateSpots()
    {
        final int SpotSpacing = 20;

        final int InitialSpotX = 100;
        final int InitialSpotY = 50;

        int LastSpotX = InitialSpotX;
        int LastSpotY = InitialSpotY;

        for (int i = 0; i < MaxNumberOfSpots; i++)
        {

            int DirectionX = 0;
            int DirectionY = 0;

            if (i <= NewDirection)
            {
                // Go Right
                DirectionX = 1;
                DirectionY = 0;
            }
            else if (i > NewDirection && i <= NewDirection * 2)
            {
                // Go Down
                DirectionX = 0;
                DirectionY = 1;
            }
            else if (i > NewDirection * 2 && i <= NewDirection * 3)
            {
                // Go Left
                DirectionX = -1;
                DirectionY = 0;
            }
            else if (i > NewDirection * 3)
            {
                // Go Up
                DirectionX = 0;
                DirectionY = -1;
            }

            _Spots[i] = new Spot((LastSpotX + (DirectionX * SpotSpacing)), (LastSpotY + (DirectionY * SpotSpacing)), Color.gray);
            
            addMouseListener(_Spots[i]);
            
            LastSpotX = (LastSpotX + (DirectionX * SpotSpacing));
            LastSpotY = (LastSpotY + (DirectionY * SpotSpacing));

            // Set the color for the new direction
            // if (i % NewDirection == 0)
            // {
            // if (i == 0)
            // {
            // _Spots[i].SetColor(Color.red);
            // }
            // else if (i == NewDirection)
            // {
            // _Spots[i].SetColor(Color.green);
            // }
            // else if (i == NewDirection * 2)
            // {
            // _Spots[i].SetColor(Color.blue);
            // }
            // else if (i == NewDirection * 3)
            // {
            // _Spots[i].SetColor(Color.yellow);
            // }
            // }
        }
    }

    private void CreateMarbles()
    {
        final int MarbleSpacing = 20;
        final int MarbleSpotX = 20;
        final int MarbleSpotY = 20;
        
        int LastSpotX = MarbleSpotX;
        int LastSpotY = MarbleSpotY;
        
        for (int i = 0; i < MaxNumberOfMarbles; i++)
        {
            System.out.println("MarbleX: " + LastSpotX);

            _Marbles[i] = new Marble(LastSpotX, LastSpotY, Color.yellow);
            LastSpotX = (LastSpotX + MarbleSpacing);
            
            addMouseListener(_Marbles[i]);
            addMouseMotionListener(_Marbles[i]);
        }

    }

    private void UpdateSpots(Graphics2D g2d)
    {
        for (int i = 0; i < MaxNumberOfSpots; i++)
        {
            g2d.setColor(_Spots[i].GetColor());
            g2d.fill(_Spots[i].GetShape());
        }
    }

    private void UpdateMarbles(Graphics2D g2d)
    {
        for (int i = 0; i < MaxNumberOfMarbles; i++)
        {
            g2d.setColor(_Marbles[i].GetColor());
            g2d.fill(_Marbles[i].GetShape());
        }
    }
    
    private void CheckMarbleSelected()
    {
        boolean MarbleSelected = false;
        
        // Check all marbles if they were selected
        for (int i=0; i<MaxNumberOfMarbles; i++)
        {
            if (_Marbles[i].IsHit() == true)
            {
                _SelectedMarble = _Marbles[i];
                MarbleSelected = true;
                break;
            }
        }
        
        if (MarbleSelected == false)
        {
            _SelectedMarble = null;
        }
    }
    
    private void CheckSelectedMarbleDroppedOnSpot()
    {
        Spot SelectedSpot = null;
        
        // Check if a marble was previously selected
        if (_SelectedMarble != null)
        {
            for (int i=0; i<MaxNumberOfSpots; i++)
            {
                if (_Spots[i].IsHit() == true)
                {
                    SelectedSpot = _Spots[i];
                    break;
                }
            }
            
            if (SelectedSpot != null)
            {
                // System.out.println("Marble dropped on spot #" + _SelectedSpot.GetSpotNumber());
                int NumberOfSpotsMoved = GetNumberOfSpotsMarbleWillMove(SelectedSpot);
                // TODO Test if this is okay... then...
                
                // Drop the marble on top of the spot perfectly
                _SelectedMarble.MoveTo(SelectedSpot);
            }
        }
    }
    
    private int GetNumberOfSpotsMarbleWillMove(Spot DesiredSpot)
    {
        int NewSpotNumber = DesiredSpot.GetSpotNumber();
        int CurrentMarbleSpotNumber = _SelectedMarble.CurrentlyOnSpotNumber();
        int NumberOfSpotsMoved = NewSpotNumber - CurrentMarbleSpotNumber;
        
        System.out.println("Marble will move " + NumberOfSpotsMoved + " number of spots.");
        
        return NumberOfSpotsMoved;
    }
}
