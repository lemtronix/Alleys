package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

public class BoardComponent extends JComponent
{
    private final int MaxNumberOfSpots = 64;
    private final int MaxNumberOfMarbles = 4;

    private final int NewDirection = 16;

    private Spot _Spots[] = new Spot[MaxNumberOfSpots];
    private Spot _LastSpotSelected;

    private Marble _Marbles[] = new Marble[MaxNumberOfMarbles];

    public BoardComponent()
    {
        CreateSpots();
        CreateMarbles();

        addMouseMotionListener(new MouseMotionListener()
        {

            @Override
            public void mouseMoved(MouseEvent e)
            {
            }

            @Override
            public void mouseDragged(MouseEvent e)
            {
            }
        });

        addMouseListener(new MouseAdapter()
        {

            @Override
            public void mouseClicked(MouseEvent e)
            {
                HighlightSpot(e);
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

    private void HighlightSpot(MouseEvent e)
    {
        boolean SpotFound = false;

        for (int i = 0; i < MaxNumberOfSpots; i++)
        {
            Rectangle2D SpotCollisionRect2D = _Spots[i].GetShape().getBounds2D();

            if (SpotCollisionRect2D.contains(e.getX(), e.getY()) == true)
            {
                // Highlight the spot
                // System.out.println("Spot " + i + " clicked!");

                SpotFound = true;

                if (_LastSpotSelected != null)
                {
                    // Selected a new spot, remove highlighting from the last one
                    _LastSpotSelected.SetDefaultColor();
                }

                _Spots[i].SetColor(Color.yellow);
                _LastSpotSelected = _Spots[i];
                break;
            }
        }

        if (SpotFound == false && _LastSpotSelected != null)
        {
            // Went through all spots and none found, remove highlighting from the last one that was found
            _LastSpotSelected.SetDefaultColor();
            _LastSpotSelected = null;
        }
    }
}
