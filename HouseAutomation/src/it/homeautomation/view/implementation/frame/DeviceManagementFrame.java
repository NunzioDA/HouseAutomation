package it.homeautomation.view.implementation.frame;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JPanel;

import it.homeautomation.controller.HouseAutomationController;
import it.homeautomation.hagui.HAButton;
import it.homeautomation.hagui.HAImageView;
import it.homeautomation.hagui.HATextCenter;
import it.homeautomation.hagui.HAUtilities;
import it.homeautomation.model.Device;
import it.homeautomation.model.DeviceGroup;
import it.homeautomation.view.implementation.DeviceCard;
import it.homeautomation.view.implementation.DeviceStateVisualizer;

public class DeviceManagementFrame extends DisableMainFrame
{
	private static final long serialVersionUID = 1L;
	
	private HAButton deleteDevice = new HAButton("Delete");	
	private DeviceStateVisualizer featuresVisualizer;
	private JPanel childDeviceVisualizer = new JPanel();
	private Device device;	
	private HouseAutomationController controller;
	
	public DeviceManagementFrame(Device device, HouseAutomationController controller)
	{
		super("Device Manager", 500, 500);
		this.controller = controller;
		this.device = device;
		
		init();
		setResizable(false);
		setVisible(true);
		
	}
	
	private void close()
	{
		setVisible(false);
		dispose();
	}
	
	private void initChildMouseListener(JPanel childPanel, Device d)
	{
		childPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e)
			{
				close();
				new DeviceManagementFrame(d, controller);
			}
			
			@Override
			public void mouseExited(MouseEvent e)
			{
				childPanel.setBackground(HAUtilities.changeColorBrightness(childPanel.getBackground(), -10));
			}
			
			@Override
			public void mouseEntered(MouseEvent e)
			{
				childPanel.setBackground(HAUtilities.changeColorBrightness(childPanel.getBackground(), 10));
			}
		});
	}
	
	private JPanel newChildDevicePanel(Device ch)
	{
		HAImageView image = new HAImageView();
		
		image.loadImage(DeviceCard.getFirstFeatureImagePath(ch));
		
		image.setLayout(new GridLayout());
		HATextCenter text = new HATextCenter(ch.getName(), 20);
		text.setBackground(HAUtilities.getDarkBackgroundColor());
		image.add(text);
		image.setBackground(HAUtilities.getDarkBackgroundColor());
		
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
		featuresVisualizer = new DeviceStateVisualizer(device, true, true);
		
		featuresVisualizer.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				close();
			}
		});
		
		setContentLayout(new GridBagLayout());
		
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
				close();
			}
		});
	}

	@Override
	public void reloadColors()
	{
		if(featuresVisualizer != null)
		{
			featuresVisualizer.setBackground(HAUtilities.getBackgroundColor());
			//featuresVisualizer.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, HAUtilities.getPrimaryColor()));
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
