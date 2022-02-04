package it.homeautomation.hagui;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
;

public class HAScrollPane extends JScrollPane implements HAThemeListener
{
	private static final long serialVersionUID = 1L;
	
	public HAScrollPane(Component comp)
	{
		super(comp);
		init();
	}

	public HAScrollPane(Component comp, int verticalScrollbarAsNeeded, int horizontalScrollbarNever)
	{
		super(comp, verticalScrollbarAsNeeded, horizontalScrollbarNever);
		init();
	}

	private void init()
	{
		setBorder(BorderFactory.createEmptyBorder());
		reloadColors();
	}
	
	@Override
	public void reloadColors()
	{
		getViewport().setBackground(getBackground());
	}
}
