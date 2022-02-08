package it.homeautomation.view.navigationpanels;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

import it.homeautomation.controller.HouseAutomationController;
import it.homeautomation.hagui.HANavigationDrawerPanel;
import it.homeautomation.hagui.HAUtilities;
import it.homeautomation.model.Device;
import it.homeautomation.model.command.Command;
import it.homeautomation.view.DeviceCommandExecutonFrame.DeviceCommandExecutedListener;
import it.homeautomation.view.RoomList;
import it.homeautomation.view.interfaces.DeviceDeletedListener;

public class HomePanel extends HANavigationDrawerPanel implements DeviceDeletedListener, DeviceCommandExecutedListener
{
	private static final long serialVersionUID = 1L;
	private HouseAutomationController controller;
	
	private RoomList listR;
	private JScrollPane scrollPane;
	
	public HomePanel(HouseAutomationController controller)
	{
		super("Home");
		this.controller = controller;			
		listR = new RoomList(controller, this, this);
		scrollPane = new JScrollPane(listR,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
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
		listR.refreshList(controller.getRoomsEntrySet());
		listR.updateUI();
	}

	@Override
	public void reloadColors()
	{
		getContent().setBackground(HAUtilities.getBackgroundColor());
		setBackground(HAUtilities.getBackgroundColor());
		scrollPane.getViewport().setBackground(HAUtilities.getBackgroundColor());
	}

	@Override
	public void deviceDeleted(Device device)
	{
		updateContent();
	}

	@Override
	public void commandExecuted(Command<?> command)
	{
		updateContent();		
	}
}
 