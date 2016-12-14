package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;

import javax.swing.JComponent;

public class BoardComponent extends JComponent
{
    final int MaxNumberOfPips = 64;
    final int NewDirection = 16;
    private Ellipse2D.Double[] _SpotArray = new Ellipse2D.Double[MaxNumberOfPips];
    
    @Override
    protected void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D)g;
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Background
        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, getWidth(), getHeight());
                
        CreateSpots(g2d);
        
        // Add Bar
//        g2d.setColor(Color.blue);
//        g2d.fill(new RoundRectangle2D.Double(200, 200, 100, 20, 20, 20));
    }
    
    private void CreateSpots(Graphics2D g2d)
    {
        final int SpotSize = 15;
        final int SpotSpacing = 20;
        
        final int InitialSpotX = 100;
        final int InitialSpotY = 50;
        
        int LastSpotX = InitialSpotX;
        int LastSpotY = InitialSpotY;
        
        for (int i=0; i < MaxNumberOfPips; i++)
        {
            
            int DirectionX = 0;
            int DirectionY = 0;
            
            if (i <= NewDirection)
            {
                // Go Right
                DirectionX = 1;
                DirectionY = 0;
            }
            else if (i > NewDirection && i <= NewDirection*2)
            {
                // Go Down
                DirectionX = 0;
                DirectionY = 1;
            }
            else if (i > NewDirection*2 && i <= NewDirection*3)
            {
                // Go Left
                DirectionX = -1;
                DirectionY = 0;
            }
            else if (i > NewDirection*3)
            {
                // Go Up
                DirectionX = 0;
                DirectionY = -1;
            }
            
            _SpotArray[i] = new Ellipse2D.Double((LastSpotX+(DirectionX*SpotSpacing)), LastSpotY+(DirectionY*SpotSpacing), SpotSize, SpotSize);
            
            LastSpotX = (LastSpotX+(DirectionX*SpotSpacing));
            LastSpotY = (LastSpotY+(DirectionY*SpotSpacing));
            
//            System.out.println(i + " LastSpotX: " + LastSpotX + " LastSpotY: " + LastSpotY);
            
            // Set the color for the new direction
            if (i % NewDirection == 0)
            {
                if (i == 0)
                {
                    g2d.setColor(Color.red);
                }
                else if (i == NewDirection)
                {
                    g2d.setColor(Color.green);
                }
                else if (i == NewDirection*2)
                {
                    g2d.setColor(Color.blue);
                }
                else if (i == NewDirection*3)
                {
                    g2d.setColor(Color.yellow);
                }
            }
            else
            {
                g2d.setColor(Color.gray);
            }
            
            g2d.fill(_SpotArray[i]);
        }
    }
}
