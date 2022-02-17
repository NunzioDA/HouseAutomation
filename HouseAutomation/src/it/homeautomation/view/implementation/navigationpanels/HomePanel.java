package it.homeautomation.view.implementation.navigationpanels;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

import it.homeautomation.controller.HouseAutomationController;
import it.homeautomation.hagui.HANavigationDrawerPanel;
import it.homeautomation.hagui.HAUtilities;
import it.homeautomation.view.implementation.HAViewImplementation;
import it.homeautomation.view.implementation.RoomList;

public class HomePanel extends HANavigationDrawerPanel 
{
	private static final long serialVersionUID = 1L;
	private HouseAutomationController controller;
	
	private RoomList listR;
	private JScrollPane scrollPane;
	
	public HomePanel()
	{
		super("Home");
		this.controller = HAViewImplementation.getSingleton().getController();		
		listR = new RoomList();
		scrollPane = new JScrollPane(listR,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setWheelScrollingEnabled(false);
		init();

        updateContent();
        reloadColors();
	}
	
	private void init()
	{
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
		getContent().setLayout(new GridLayout());
        getContent().add(scrollPane); 
    }
	
	@Override
	public void repaint()
	{
		if(listR!=null)
			listR.setMaxWidth(getWidth() - 20);
		
		super.repaint();
	}
		
	@Override
	public void updateContent()
	{
		listR.refreshList(controller.getGroupedRoomsMapEntrySet());
		listR.updateUI();
	}

	@Override
	public void reloadColors()
	{
		getContent().setBackground(HAUtilities.getBackgroundColor());
		setBackground(HAUtilities.getBackgroundColor());
		scrollPane.getViewport().setBackground(HAUtilities.getBackgroundColor());
	}

}
 