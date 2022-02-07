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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.List;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import it.homeautomation.controller.HouseAutomationController;
import it.homeautomation.hagui.HAButton;
import it.homeautomation.hagui.HAFrame;
import it.homeautomation.hagui.HAImageView;
import it.homeautomation.hagui.HATextCenter;
import it.homeautomation.hagui.HAUtilities;
import it.homeautomation.model.Device;
import it.homeautomation.model.DeviceGroup;
import it.homeautomation.model.features.DeviceCategory;
import it.homeautomation.model.features.DeviceFeature;
import it.homeautomation.view.interfaces.DeviceDeletedListener;

public class DeviceManagementFrame extends HAFrame
{
	private static final long serialVersionUID = 1L;
	
	private HAButton deleteDevice = new HAButton("Delete");	
	private JPanel featuresVisualizer = new JPanel();
	private JPanel childDeviceVisualizer = new JPanel();
	private Device device;	
	private HouseAutomationController controller;
	private DeviceDeletedListener listener;
	
	public DeviceManagementFrame(Device device, HouseAutomationController controller, DeviceDeletedListener listener)
	{
		super("Device Manager", 500, 500);
		this.controller = controller;
		this.device = device;
		this.listener = listener;
		
		init();
		
		setVisible(true);
	}
	
	private void initChildMouseListener(JPanel childPanel, Device d)
	{
		childPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e)
			{
				new DeviceManagementFrame(d, controller, listener);
			}
		});
	}
	
	private JPanel newChildDevicePanel(Device ch)
	{
		HAImageView image = new HAImageView();
		
		Optional<DeviceFeature> firstCategoryOpt = ch
				.getFeatures()
				.stream()
				.filter(f -> (f instanceof DeviceCategory))
				.findFirst();
		
		if(!firstCategoryOpt.isEmpty())
		{
			DeviceCategory firstCategory = (DeviceCategory)firstCategoryOpt.get();
			String iconID = firstCategory.getIconID();
			URL imagePath;
			
			if(iconID != null)
			{
				imagePath = HAUtilities.getIconPath(iconID);
				image.loadImage(imagePath);
			}
			
		}
		
		image.setLayout(new GridLayout());
		image.add(new HATextCenter(ch.getName()));
		initChildMouseListener(image, ch);
		
		return image;
	}
	
	
	private void initChildPanel()
	{
		List<Device> children = ((DeviceGroup)device).getChildren();
		childDeviceVisualizer.setLayout(new GridLayout(1, children.size()));
		
		children.stream().forEach(c ->{
			JPanel ch = newChildDevicePanel(c);
			childDeviceVisualizer.add(ch);
		});
	}
	
	
	private void init()
	{		
		initDeleteButton();
		featuresVisualizer = new DeviceStateVisualizer(device);
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
		
		if(device instanceof DeviceGroup)
		{
			initChildPanel();
			addComponent(childDeviceVisualizer, constraints);
			constraints.gridy ++;
		}
		
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
		{
			featuresVisualizer.setBackground(HAUtilities.getBackgroundColor());
			featuresVisualizer.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, HAUtilities.getPrimaryColor()));
		}
		
		if(childDeviceVisualizer != null)
			childDeviceVisualizer.setBackground(HAUtilities.getBackgroundColor());
		
		if(deleteDevice != null)
			deleteDevice.reloadColors();
	}

	@Override
	public void resizeContent()
	{
		// TODO Auto-generated method stub
		
	}
}
