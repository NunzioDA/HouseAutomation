package it.homeautomation.view;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.net.URL;
import java.util.List;

import javax.swing.JPanel;

import it.homeautomation.controller.HouseAutomationController;
import it.homeautomation.hagui.HAButton;
import it.homeautomation.hagui.HAFrame;
import it.homeautomation.hagui.HAImageView;
import it.homeautomation.hagui.HAUtilities;
import it.homeautomation.model.Device;
import it.homeautomation.model.DeviceGroup;
import it.homeautomation.model.features.DeviceFeature;
import it.homeautomation.model.features.SingleValueFeature;
import it.homeautomation.model.features.implementation.StateFeature;
import it.homeautomation.view.interfaces.DeviceDeletedListener;

public class DeviceManagementFrame extends HAFrame
{
	private static final long serialVersionUID = 1L;
	
	private HAButton deleteDevice = new HAButton("Delete");	
	private JPanel featuresVisualizer = new JPanel();	
	private Device device;	
	private HouseAutomationController controller;
	private DeviceDeletedListener listener;
	
	public DeviceManagementFrame(Device device, HouseAutomationController controller, DeviceDeletedListener listener)
	{
		super("Device Manager", 500, 500);
		this.controller = controller;
		this.device = device;
		this.listener = listener;
		
		if(device instanceof DeviceGroup)
		{
			for(Device d : ((DeviceGroup)device).getChilden())
			{
				for(DeviceFeature f : d.getFeatures())
					if(f instanceof SingleValueFeature<?>)
						System.out.println(((SingleValueFeature<?>)f).getValue());
			}
		}
		
		init();		
	}
	
	private void initFeturesPanel()
	{

		List<DeviceFeature> features = device.getFeatures();
		featuresVisualizer.setLayout(new GridLayout(1, features.size()));
		
		features.stream().forEach(f ->{
			if(!(f instanceof StateFeature))
			{
				URL url = HAUtilities.getIconPath(f.getIconID());
				featuresVisualizer.add(new HAImageView(url, 0));
			}
		});
	}
	
	private void init()
	{		
		initDeleteButton();
		initFeturesPanel();
		setContentLayout(new GridBagLayout());
		deleteDevice.setFocusable(false);
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.insets = new Insets(20, 20, 20, 20);
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.BOTH;
		
		addComponent(HAUtilities.newTitle(device.getName(), HAUtilities.MAIN_TITLE), constraints);
		
		constraints.gridy ++;
		addComponent(HAUtilities.newDescription("Device Features"), constraints);
		
		constraints.gridy ++;
		constraints.insets.top = 0;
		addComponent(featuresVisualizer, constraints);
		
		constraints.gridy ++;
		constraints.insets.top = 20;
		addComponent(deleteDevice, constraints);
		
		initFocusListener();
		reloadColors();
		setVisible(true);
	}
	
	private void initDeleteButton()
	{
		deleteDevice.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				controller.deleteDevice(device);
				listener.deviceDeleted(device);
				setVisible(false);
				dispose();
			}
		});
	}
	
	private void initFocusListener()
	{
		for(Component com : getComponents())
		{
			com.setFocusable(false);
		}
		
		addFocusListener(new FocusAdapter() 
		{
			@Override
			public void focusLost(FocusEvent e)
			{

				setVisible(false);
				dispose();
			}
			 
		});
		
		requestFocus();
	}

	@Override
	public void reloadColors()
	{
		if(featuresVisualizer != null)
			featuresVisualizer.setBackground(HAUtilities.getBackgroundColor());
		if(deleteDevice != null)
			deleteDevice.reloadColors();
	}

	@Override
	public void resizeContent()
	{
		// TODO Auto-generated method stub
		
	}
}
