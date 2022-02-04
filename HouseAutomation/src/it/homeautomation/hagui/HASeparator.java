package it.homeautomation.hagui;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JSeparator;

public class HASeparator extends JSeparator
{
	private static final long serialVersionUID = 1L;

	public HASeparator(int orientation, Color separatorColor, int thickness)
	{
		super(orientation);
		setBorder(BorderFactory.createLineBorder(separatorColor, thickness));
	}
	
	public HASeparator(int orientation, int thickness)
	{
		super(orientation);
		Color color = HATools.changeColorBrightness(HATools.getDarkBackgroundColor(), -20);
		setBorder(BorderFactory.createLineBorder(color, thickness));
	}
}
