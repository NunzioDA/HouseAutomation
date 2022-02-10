package it.homeautomation.view.implementation;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

import it.homeautomation.hagui.HALabel;
import it.homeautomation.hagui.HAPanel;
import it.homeautomation.hagui.HAUtilities;
import it.homeautomation.model.Device;
import it.homeautomation.view.implementation.DeviceCommandExecutonFrame.DeviceCommandExecutedListener;
import it.homeautomation.view.interfaces.DeviceDeletedListener;
import it.homeautomation.view.interfaces.ListCardRenderer;

/**
 * This class is the room visualization template.
 * 
 * @author Nunzio D'Amore
 *
 */

public class RoomCard extends HAPanel implements ListCardRenderer<Map.Entry<String, List<Device>>>
{
	private static final long serialVersionUID = 1L;
	private HALabel roomName = HAUtilities.newTitle("", HAUtilities.MIDDLE_TITLE); 
	
	private DeviceList deviceList;
	private JScrollPane deviceScrollPane;

	private boolean listenToMouseWheel = false;
	
	private static final int FACTOR = 30;

	public RoomCard(DeviceDeletedListener listener, DeviceCommandExecutedListener commandListener)
	{
		this.deviceList = new DeviceList(listener, commandListener);
		deviceScrollPane = new JScrollPane(deviceList, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		init();		
		reloadColors();
	}
	

	@Override
	public Component getListCardRendererComponent(Map.Entry<String, List<Device>> value)
	{
		roomName.setText(value.getKey());
		if(deviceList.getComponentCount() != value.getValue().size()) 
		{
			deviceList.refreshList(value.getValue());
		}
		return this;
	}	
	
	/**
	 * This method initialize the mouse listeners 
	 * that allow the user to scroll the list by using the scroll wheel
	 */
	public void initMouseWheelListeners()
	{
        deviceScrollPane.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseExited(MouseEvent e)
			{
				listenToMouseWheel = false;
			}
			
			@Override
			public void mouseEntered(MouseEvent e)
			{
				listenToMouseWheel = true;
			}
			
		});
        
        deviceScrollPane.addMouseWheelListener(new MouseWheelListener() {
			
			@Override
			public void mouseWheelMoved(MouseWheelEvent e)
			{
				if(listenToMouseWheel)
				{
					int amount = e.getWheelRotation() * FACTOR;
					int value = deviceScrollPane.getHorizontalScrollBar().getValue();
					
					deviceScrollPane.getHorizontalScrollBar().setValue(value + amount);
				}
			}
		});
	}
	
	public void init()
	{
		deviceList.setMyScrollPane(deviceScrollPane);
		deviceScrollPane.setBorder(BorderFactory.createEmptyBorder());
		
		setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 0.1f;
        constraints.fill=GridBagConstraints.HORIZONTAL;
        
        add(roomName, constraints);
        
        constraints.insets = new Insets(10, 0, 0, 0);
        constraints.gridy = 1;
        constraints.weighty = 1f;
        constraints.ipady = 40;
        constraints.fill=GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.WEST;
        add(deviceScrollPane, constraints);        

        initMouseWheelListeners();
	}
	
	@Override
	public void reloadColors()
	{
		setBackground(HAUtilities.getBackgroundColor());
		deviceList.reloadColors();
		deviceScrollPane.getViewport().setBackground(HAUtilities.getBackgroundColor());
	}



}
