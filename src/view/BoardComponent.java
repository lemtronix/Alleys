package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JComponent;

public class BoardComponent extends JComponent
{

    @Override
    protected void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D)g;
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        g2d.setColor(Color.gray);
        g2d.fill(new Ellipse2D.Double(100, 100, 15, 15));
        
        g2d.setColor(Color.blue);
        g2d.fill(new RoundRectangle2D.Double(200, 200, 100, 20, 20, 20));
    }
    
}
