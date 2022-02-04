package it.homeautomation.hagui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class HAImageView extends JPanel{

	private static final long serialVersionUID = 1L;
	
	private BufferedImage image = null;

    public HAImageView() {

    }
    
    public void loadImage(String path)
    {
        try {                
            image = ImageIO.read(new File(path));
         } catch (IOException ex) {
              // handle exception...
         }
        
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(image != null)
        	g.drawImage(image, 0, 0, this); // see javadoc for more info on the parameters            
    }

}
