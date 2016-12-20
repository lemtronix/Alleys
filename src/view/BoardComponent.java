package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;

import controller.Controller;
import controller.MarbleEvent;
import controller.MarbleListener;

public class BoardComponent extends JComponent implements MarbleListener
{
    private final int NewDirection = 16;
    
    private Controller _Controller;

    private SpotGraphic _Spots[] = new SpotGraphic[64];
    private MarbleGraphic _Marbles[] = new MarbleGraphic[16];
    
    private MarbleGraphic _SelectedMarble;

    public BoardComponent(Controller GameController)
    {
        _Controller = GameController;
        
        CreateSpots();
        CreateMarbles();
        
        _Controller.setMarbleListener(this);
        
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
    
    public MarbleGraphic GetSelectedMarble()
    {
        if (_SelectedMarble != null)
        {
//            System.out.println("BoardComponent: Returning actual marble");
            return _SelectedMarble;
        }
        
        return null;
    }

    
    @Override
    public void marbleEventOccurred(MarbleEvent me) {
//        System.out.println("BoardComponent: Marble Event Occurred!");
//        System.out.println("Marble# " + me.getMarbleIdNumber());
//        System.out.println("NewSpot# " + me.getSpotIdNumber());
        
        _Marbles[me.getMarbleIdNumber()].MoveTo(_Spots[me.getSpotIdNumber()]);
        repaint();
        
    }

    public void clearSelectedMarble()
    {
        _SelectedMarble = null;
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

        for (int i = 0; i < _Controller.GetMaxNumberOfSpots(); i++)
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

            _Spots[i] = new SpotGraphic(i, (LastSpotX + (DirectionX * SpotSpacing)), (LastSpotY + (DirectionY * SpotSpacing)), Color.gray);
            
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
        
        for (int i = 0; i < _Controller.GetMaxNumberOfMarbles(); i++)
        {
//            System.out.println("MarbleX: " + LastSpotX);

            _Marbles[i] = new MarbleGraphic(i, LastSpotX, LastSpotY, Color.yellow);
            LastSpotX = (LastSpotX + MarbleSpacing);
            
            addMouseListener(_Marbles[i]);
            addMouseMotionListener(_Marbles[i]);
        }

    }

    private void UpdateSpots(Graphics2D g2d)
    {
        for (int i = 0; i < _Controller.GetMaxNumberOfSpots(); i++)
        {
            g2d.setColor(_Spots[i].GetColor());
            g2d.fill(_Spots[i].GetShape());
        }
    }

    private void UpdateMarbles(Graphics2D g2d)
    {
        for (int i = 0; i < _Controller.GetMaxNumberOfMarbles(); i++)
        {
            g2d.setColor(_Marbles[i].GetColor());
            g2d.fill(_Marbles[i].GetShape());
        }
    }
    
    private synchronized void CheckMarbleSelected()
    {
        boolean MarbleSelected = false;
        
        // Check all marbles if they were selected
        for (int i=0; i<_Controller.GetMaxNumberOfMarbles(); i++)
        {
            if (_Marbles[i].IsHit() == true)
            {
                System.out.println("Marble hit.");
                _SelectedMarble = _Marbles[i];
                MarbleSelected = true;
                notify();
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
//        Spot SelectedSpot = null;
//        
//        // Check if a marble was previously selected
//        if (GetSelectedMarble() != null)
//        {
//            for (int i=0; i<MaxNumberOfSpots; i++)
//            {
//                if (_Spots[i].IsHit() == true)
//                {
//                    SelectedSpot = _Spots[i];
//                    break;
//                }
//            }
//            
//            if (SelectedSpot != null)
//            {
//                
//                // TODO Test if this is okay... then...
//                
//                // Drop the marble on top of the spot perfectly
//                GetSelectedMarble().MoveTo(SelectedSpot);
//            }
//        }
    }
    
}
