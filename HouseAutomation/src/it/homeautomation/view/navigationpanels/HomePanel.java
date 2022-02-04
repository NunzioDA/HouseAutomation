package it.homeautomation.view.navigationpanels;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import it.homeautomation.controller.HouseAutomationController;
import it.homeautomation.hagui.HALabel;
import it.homeautomation.hagui.HANavigationDrawerPanel;
import it.homeautomation.hagui.HATools;
import it.homeautomation.view.RoomList;

public class HomePanel extends HANavigationDrawerPanel
{
	private static final long serialVersionUID = 1L;
	private HouseAutomationController controller;
	
	private HALabel title = new HALabel("Home", SwingConstants.LEFT);
	private RoomList listR = new RoomList();
	private JScrollPane scrollPane = new JScrollPane(listR,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	
	public HomePanel(HouseAutomationController controller)
	{
		super("Home");
		this.controller = controller;			
		
		init();
        
        updateContent();
        reloadColors();
	}
	
	private void init()
	{
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		title.setFont(HATools.getRegularFont().deriveFont(50f));
        
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
		listR.refreshList(controller.getRoomsEntrySet());
	}

	@Override
	public void reloadColors()
	{
		getContent().setBackground(HATools.getBackgroundColor());
		setBackground(HATools.getBackgroundColor());
		scrollPane.getViewport().setBackground(HATools.getBackgroundColor());
	}
}
 