package it.homeautomation.view.implementation;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.swing.SwingConstants;

import it.homeautomation.hagui.HALabel;
import it.homeautomation.hagui.HAPanel;
import it.homeautomation.hagui.HAUtilities;
import it.homeautomation.model.Device;
import it.homeautomation.view.implementation.DeviceCommandExecutonFrame.DeviceCommandExecutedListener;
import it.homeautomation.view.interfaces.DeviceDeletedListener;

public class RoomList extends HAPanel
{
	private static final long serialVersionUID = 1L;
	
	private GridBagConstraints constraints = new GridBagConstraints();
	private int maxWidth = Integer.MAX_VALUE;
	private DeviceDeletedListener deletedListener;
	private DeviceCommandExecutedListener commandListener;
	public RoomList(DeviceDeletedListener deletedListener, DeviceCommandExecutedListener commandListener)
	{		
		this.deletedListener = deletedListener;
		this.commandListener = commandListener;
		
		setLayout(new GridBagLayout());
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 1f;
        constraints.insets = new Insets(0, 0, 20, 0);
        constraints.fill = GridBagConstraints.BOTH;
        
        reloadColors();
	}	
	
	public void notifyEmptyList()
	{
		add(new HALabel("No devices have been added...", SwingConstants.CENTER), constraints);
	}
	
	
	private void addComponent(Map.Entry<String, List<Device>> m)
	{			
		constraints.gridy ++;
		RoomCard r = (RoomCard) new RoomCard(deletedListener, commandListener)
				.getListCardRendererComponent(m);
		add(r, constraints);
	}
	
	
	public void refreshList(Collection<Map.Entry<String, List<Device>>> coll)
	{
		constraints.gridy = 0;
		removeAll();
		
		if(coll.size() > 0)
		{		
			coll.stream().forEach(this::addComponent);
		}
		else notifyEmptyList();
	}

	@Override
	public void reloadColors()
	{
		setBackground(HAUtilities.getBackgroundColor());		
	}
	
	public void setMaxWidth(int maxWidth)
	{
		this.maxWidth = maxWidth - 50;
	}
	
	@Override
	public void setSize(Dimension d)
	{
		// TODO Auto-generated method stub
		int width = d.width;
		
		if(d.width > maxWidth)
			width = maxWidth;
			
		super.setSize(new Dimension(width, d.height));
	}


}
