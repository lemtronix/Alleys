import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

public class Game extends JComponent
{

    @Override
    protected void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D)g;
        
        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }
    
}
