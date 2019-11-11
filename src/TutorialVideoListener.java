import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import de.yadrone.base.ARDrone;

public class TutorialVideoListener extends JFrame
{
    private BufferedImage image = null;
    
    public TutorialVideoListener(final ARDrone drone)
    {
        super("YADrone Tutorial");
        
        setSize(640,360);
        setVisible(true);
    }
    
    public void paint(Graphics g)
    {
        if (image != null)
            g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
    }
}
