package it.homeautomation.hagui;

import javax.swing.BorderFactory;
import javax.swing.JTextField;

public class HATextField extends JTextField implements HAThemeListener
{
	private static final long serialVersionUID = 1L;
	
	public HATextField(int columns)
	{
		super(columns);

		reloadColors();
		
//		setBorder(BorderFactory.createCompoundBorder(
//		        this.getBorder(), 
//		        BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		
		setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
	}

	@Override
	public void reloadColors()
	{
		setBackground(HATools.getDarkBackgroundColor());
		setForeground(HATools.getForegroundColor());		
	}
}
