package it.homeautomation.hagui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class HAImageView extends JPanel implements HAThemeListener{

	private static final long serialVersionUID = 1L;
	
	private BufferedImage image = null;
	private Color background = null;
	private int margin = 0;
    
    public void loadImage(String path, Color backGround)
    {
    	this.background = backGround;
    	
    	reloadColors();
    	
        try {
        	URL url = HAImageView.class.getResource(path);
        	
        	if(url != null)
            	image = ImageIO.read(url);
        	
         } catch (IOException ex) {
        	 System.out.print(ex);
         }
        
        repaint();
    }
    
    public void setMargin(int margin)
    {
    	this.margin = margin;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if(image != null) 
        {

        	float imageRatio = (float)image.getWidth() / (float)image.getHeight();        	
        	int imageWidth = (int)(imageRatio * (float)(getHeight() - margin));
        	int imageHeight = getHeight();
        	
        	if(imageWidth > (getWidth() - margin))
        	{
        		float imageRatioW = (float)imageHeight / (float)imageWidth;        	
        		imageHeight = (int)(imageRatioW *  (float)(getWidth() - margin));
        		imageWidth = (int)(imageRatio * imageHeight);
        	}
        	
        	int xStartPosition = (getWidth() / 2) - (imageWidth / 2);
        	int yStartPosition = (getHeight() / 2) - (imageHeight / 2);
        	
        	g.drawImage(image, xStartPosition, yStartPosition, imageWidth, imageHeight, this);
        }
    }

	@Override
	public void reloadColors()
	{
		if(background != null)
			setBackground(background);
	}

}
