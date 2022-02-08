package it.homeautomation.hagui;


import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListModel;

public class HAList <E> extends JList<E> implements HAThemeListener
{
	private static final long serialVersionUID = 1L;
	private ListModel<E> listModel = new DefaultListModel<E>();
	
	
	public HAList()
	{
		super();
		setModel(listModel);
		reloadColors();
	}

	public DefaultListModel<E> getDefaultModel()
	{
		return (DefaultListModel<E>) listModel;
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
