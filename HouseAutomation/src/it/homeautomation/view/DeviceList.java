package it.homeautomation.view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Collection;

import javax.swing.JScrollPane;

import it.homeautomation.controller.HouseAutomationController;
import it.homeautomation.hagui.HAPanel;
import it.homeautomation.hagui.HAUtilities;
import it.homeautomation.model.Device;


/**
 * This class is an horizontal list 
 * useful to visualize all the devices in the room.
 * 
 * @author Nunzio D'Amore
 */

public class DeviceList extends HAPanel
{
	private static final long serialVersionUID = 1L;
	private GridBagConstraints constraints = new GridBagConstraints();
	private JScrollPane myScrollPane;
	
	private static int MARGIN_BETWEEN_CARDS = 30;
	
	private HouseAutomationController controller;
	
	public DeviceList(HouseAutomationController controller)
	{		
		this.controller = controller;
		setLayout(new GridBagLayout());
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.weightx = 1;
        constraints.weighty = 1f;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.VERTICAL;
        
        reloadColors();        
	}
	
	public void setMyScrollPane(JScrollPane scrollPane)
	{
		this.myScrollPane = scrollPane;
	}
	
	private void addComponent(Device m)
	{		
		if(constraints.gridx != 0)
			constraints.insets.left = MARGIN_BETWEEN_CARDS;
		else constraints.insets.left = 0;
		
		add(new DeviceCard(myScrollPane, controller).getListCardRendererComponent(m), constraints);
		
		constraints.gridx ++;
	}
	
	public void refreshList(Collection<Device> coll)
	{
		constraints.gridx = 0;		
		removeAll();
		coll.stream().forEach(this::addComponent);
	}
	
	@Override
	public void reloadColors()
	{
		setBackground(HAUtilities.getBackgroundColor());		
	}

	@Override
	public void setSize(Dimension d)
	{
		int newWidth = 0;
		
		if(getComponentCount() > 0)
		{
			newWidth = getComponentCount() * (getComponent(0).getPreferredSize().width + MARGIN_BETWEEN_CARDS);
			
			if(d.width < newWidth)
				newWidth = d.width;
		}
		
		super.setSize(new Dimension(newWidth, DeviceCard.dimensions.height));
	}

}
