package it.homeautomation.hagui;

import java.awt.Color;
import java.awt.Graphics;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class HAImageView extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private ImageIcon image = null;
	private int margin = 0;
    
	public HAImageView()
	{
		setOpaque(false);
	}
	
	public HAImageView(URL url, int margin)
	{
		setOpaque(false);
		setMargin(margin);
		loadImage(url);
	}
	
	@Override
	public void setBackground(Color bg)
	{
		setOpaque(true);
		super.setBackground(bg);
	}
	
	 public void loadImage(URL url, int margin)
	 {
		 setMargin(margin);
		 loadImage(url);
	 }
	
    public void loadImage(URL url)
    {    	       	
    	if(url != null)
        	image = new ImageIcon(url);

        repaint();
    }
    
    public void setMargin(int margin)
    {
    	this.margin = margin;
    }

    @Override
    protected void paintComponent(Graphics g) {
    	
    	if(isOpaque())
        	super.paintComponent(g);
        
        if(image != null) 
        {

        	float imageRatio = (float)image.getIconWidth() / (float)image.getIconHeight();        	
        	int imageWidth = (int)(imageRatio * (float)(getHeight() - margin));
        	int imageHeight = getHeight() - margin;
        	
        	if(imageWidth > (getWidth() - margin))
        	{
        		float imageRatioW = (float)imageHeight / (float)imageWidth;        	
        		imageHeight = (int)(imageRatioW *  (float)(getWidth() - margin));
        		imageWidth = (int)(imageRatio * imageHeight);
        	}
        	
        	int xStartPosition = (getWidth() / 2) - (imageWidth / 2);
        	int yStartPosition = (getHeight() / 2) - (imageHeight / 2);
        	
        	g.drawImage(image.getImage(), xStartPosition, yStartPosition, imageWidth, imageHeight, this);
        }
    }


}
