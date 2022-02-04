package it.homeautomation.hagui;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

public class HAButton extends JButton implements HAThemeListener
{
	private static final long serialVersionUID = 1L;
	private static final float BUTTON_FONT_SIZE = 15f;
	private static final int BUTTON_COLOR_BRIGHTENING = 15;
	private Color normalColor, foreground;
	
	public HAButton(String text)
	{
		super(text);
		reloadColors();
		setBorder(null);
		setFocusPainted(false);
		setFont(HATools.getThinFont().deriveFont(BUTTON_FONT_SIZE));
		initMouseReactionListener();
	}
	
	private void resetButtonColor()
	{
		this.setBackground(normalColor);
	}
	
	private void initMouseReactionListener()
	{
		addMouseListener(new MouseListener() {			
			@Override
			public void mouseReleased(MouseEvent e) {}			
			@Override
			public void mousePressed(MouseEvent e) {}			
			@Override
			public void mouseClicked(MouseEvent e) {}
			
			@Override
			public void mouseExited(MouseEvent e) {
				if(isEnabled())
					HAButton.this.resetButtonColor();
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				if(isEnabled()) {
					
					Color mouseEnteredColor = HATools.changeColorBrightness(normalColor, BUTTON_COLOR_BRIGHTENING);
					HAButton.this.setBackground(mouseEnteredColor);
				}
			}
		});				
	
	}
	
	@Override
	public void setEnabled(boolean b)
	{
		// setting disabled color
		if(!b)
		{
			Color disabledColor = HATools.changeColorBrightness(normalColor, -BUTTON_COLOR_BRIGHTENING);
			setBackground(disabledColor);
		}
		else resetButtonColor();
		
		super.setEnabled(b);		
	}
	
	public void setCustomColors(Color background, Color foreground)
	{
		this.normalColor = background;
		this.foreground = foreground;
		setBackground(normalColor);
		setForeground(foreground);
	}
	
	@Override
	public int hashCode()
	{
		return getText().hashCode();
	}

	@Override
	public void reloadColors()
	{
		normalColor = HATools.getPrimaryColor();
		foreground = HATools.getPrimaryForegroundColor();
		
		
		setBackground(normalColor);
		setForeground(foreground);
	}
}
