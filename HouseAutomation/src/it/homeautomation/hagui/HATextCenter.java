package it.homeautomation.hagui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;

import javax.swing.JPanel;


public class HATextCenter extends JPanel implements HAThemeListener
{
	private static final long serialVersionUID = 1L;
	private static int TEXT_MARGIN = 3;
	private String text;
	
	public HATextCenter(String text)
	{
		this.text = text;
		
		reloadColors();
	}
	
	public void setText(String text)
	{
		this.text = text;
		repaint();
	}

	@Override
	public void paint(Graphics g)
	{
		// Printing background
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(getBackground());
		g2.fillRect(0, 0, getWidth(), getHeight());
		
		
		g2.setColor(getForeground());
		float textWidth;
		float fontSize = 0;
		
		// calculating max font size to fit panel width
		
		int targetWidth = getWidth() - TEXT_MARGIN * 2;
		int textheight;
		do {			
			fontSize += 0.5;
			g2.setFont(HATools.getThinFont().deriveFont(fontSize));		
			textWidth = g2.getFontMetrics().stringWidth(text);	
			
			// getting text height
			textheight = getStringHeight(g2, text);
		}while(textWidth < targetWidth && textheight < getHeight() - TEXT_MARGIN);

		
		int textStartX = (int)(getWidth() - textWidth)/2;
		
		g2.drawString(text, textStartX ,(getHeight() / 2) + (textheight/2));
	}

    private static int getStringHeight(Graphics2D g2, String str)
	{
		FontRenderContext frc = g2.getFontRenderContext();
		GlyphVector gv = g2.getFont().createGlyphVector(frc, str);
		return (int) gv.getPixelBounds(null, 0f,0f).getHeight();
	}
    
	@Override
	public void reloadColors()
	{
		setBackground(HATools.getBackgroundColor());
		setForeground(HATools.getForegroundColor());
	}
}
