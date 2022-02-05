package it.homeautomation.hagui;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.border.MatteBorder;

public class HATextField extends JTextField implements HAThemeListener
{
	private static final long serialVersionUID = 1L;
	
	public HATextField(int columns)
	{
		super(columns);

		reloadColors();
		
		setEmptyBorder();
		
		addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e)
			{
				setEmptyBorder();
			}
			
			@Override
			public void focusGained(FocusEvent e)
			{
				setBorder(new MatteBorder(0, 0, 2, 0, HATools.getForegroundColor()));
			}
		});
	}

	private void setEmptyBorder()
	{
		setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
	}
	
	@Override
	public void reloadColors()
	{
		
		setBackground(HATools.getDarkBackgroundColor());
		setForeground(HATools.getForegroundColor());		
	}
}
