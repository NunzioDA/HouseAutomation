package it.homeautomation.view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Collection;

import javax.swing.JScrollPane;

import it.homeautomation.hagui.HAPanel;
import it.homeautomation.hagui.HATools;
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
	
	public DeviceList()
	{		
		setLayout(new GridBagLayout());
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(0, 20, 0, 20);
//        constraints.ipadx = 20;
//        constraints.ipady = 20;
        constraints.weightx = 1;
        constraints.weighty = 1f;
        constraints.fill=GridBagConstraints.VERTICAL;
        
        reloadColors();        
	}
	
	public void setMyScrollPane(JScrollPane scrollPane)
	{
		this.myScrollPane = scrollPane;
	}
	
	private void addComponent(Device m)
	{		
		constraints.gridx ++;
		add(new DeviceCard(myScrollPane).getListCardRendererComponent(m), constraints);
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
		setBackground(HATools.getBackgroundColor());		
	}

	@Override
	public void setSize(Dimension d)
	{
		int newWidth = getComponentCount() * (getComponent(0).getPreferredSize().width + 40);
		super.setSize(new Dimension(newWidth, DeviceCard.dimensions.height));
	}

}
