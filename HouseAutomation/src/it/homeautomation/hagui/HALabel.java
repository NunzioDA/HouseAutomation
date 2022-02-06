package it.homeautomation.hagui;

import javax.swing.JLabel;

public class HALabel extends JLabel implements HAThemeListener
{
	private static final long serialVersionUID = 1L;
	private static final float DEFAULT_FONT_SIZE = 15f;
	
	
	public void init()
	{
		reloadColors();
		setFont( HAUtilities.getThinFont().deriveFont(DEFAULT_FONT_SIZE) );
	}
	
	public HALabel(String text, int horizontalAlignment) 
	{
		super(text, horizontalAlignment);
		init();
	}
	
	public void setFontSize(float size)
	{
		setFont( HAUtilities.getThinFont().deriveFont(size) );
	}

	
	@Override
	public void reloadColors()
	{
		setForeground(HAUtilities.getForegroundColor());
	}
}
