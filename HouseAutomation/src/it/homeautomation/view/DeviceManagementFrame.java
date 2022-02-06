package it.homeautomation.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JFrame;

import it.homeautomation.controller.HouseAutomationController;
import it.homeautomation.hagui.HAButton;
import it.homeautomation.hagui.HATools;
import it.homeautomation.model.Device;

public class DeviceManagementFrame extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	private HAButton deleteDevice = new HAButton("Delete");
	
	private Device device;
	
	private HouseAutomationController controller;
	
	public DeviceManagementFrame(Device device, HouseAutomationController controller)
	{
		super("Device Manager");
		this.controller = controller;
		this.device = device;
		init();
	}
	
	private void init()
	{
		initView();
		initFocusListener();
		initDeleteButton();
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.BOTH;
		
		add(HATools.newTitle(device.getName(), HATools.MAIN_TITLE), constraints);
		
		constraints.gridy ++;
		add(deleteDevice, constraints);		
		
		setVisible(true);
	}
	
	private void initView()
	{
		setSize(500, 500);
		setLocation(200, 200);
		setUndecorated(true);
		
		setLayout(new GridBagLayout());
		getContentPane().setBackground(HATools.getBackgroundColor());		
	}
	
	private void initDeleteButton()
	{
		deleteDevice.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				controller.deleteDevice(device);
			}
		});
	}
	
	private void initFocusListener()
	{
		addFocusListener(new FocusAdapter() 
		{
			 public void focusLost(FocusEvent e) 
			 {
				 setVisible(false);
				 dispose();
			 }
		});
	}
}
