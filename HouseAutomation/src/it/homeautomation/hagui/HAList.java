package it.homeautomation.hagui;


import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.ListModel;

public class HAList <E> extends JList<E> implements HAThemeListener
{
	private static final long serialVersionUID = 1L;
	
	public HAList(ListModel<E> model)
	{
		super(model);
		reloadColors();
	}

	@Override
	public void reloadColors()
	{
		setBackground(HAUtilities.getDarkBackgroundColor());
		setForeground(HAUtilities.getForegroundColor());
		
//		setBorder(BorderFactory.createCompoundBorder(
//		        this.getBorder(), 
//		        BorderFactory.createLineBorder(HATools.getForegroundColor(), 1)));
		setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
	}
	
}
