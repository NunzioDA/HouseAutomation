package it.homeautomation.view.commandmanagement;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import it.homeautomation.controller.CommandsUtility;
import it.homeautomation.controller.HouseAutomationController;
import it.homeautomation.hagui.HAComboBox;
import it.homeautomation.hagui.HALabel;
import it.homeautomation.hagui.HAPanel;
import it.homeautomation.hagui.HAUtilities;
import it.homeautomation.model.AvailableFeature;
import it.homeautomation.model.Device;
import it.homeautomation.model.command.Command;
import it.homeautomation.model.features.DeviceCategory;
import it.homeautomation.model.features.implementation.StateFeature;

public class CreateCommandPanel extends HAPanel
{
	private static final long serialVersionUID = 1L;
	private static final String PROTOTYPE_TEXT = "wwwwwwwwww";
	
	private HALabel subtitleDevice = HAUtilities.newTitle("Select Device, a room or a category", HAUtilities.MIDDLE_TITLE);
	private HALabel tutorial = HAUtilities.newTitle("Selecting a room will filter devices and categories.", HAUtilities.LAST_TITLE);
	
	
	private DefaultComboBoxModel<String> roomsModel = new DefaultComboBoxModel<>();
	private HAComboBox<String> roomsBox = new HAComboBox<>(roomsModel);
	
	
	private DefaultComboBoxModel<Object> deviceModel = new DefaultComboBoxModel<>();
	private HAComboBox<Object> devicesBox = new HAComboBox<>(deviceModel);
	
	
	private DefaultComboBoxModel<Object> categoryModel = new DefaultComboBoxModel<>();
	private HAComboBox<Object> categoryBox = new HAComboBox<>(categoryModel);
	
	private HouseAutomationController controller;
	private SelectCommandPanel selectCommand;

	private Device singleDevice = null;
	
	public CreateCommandPanel(HouseAutomationController controller, SelectCommandPanel selectCommand)
	{
		this.controller = controller;
		this.selectCommand = selectCommand;

		init();		
	}

	private <E>  Object getSelected(JComboBox<E> comboBox, DefaultComboBoxModel<E> model)
	{
		Object result = null;
		
		int index = comboBox.getSelectedIndex();
		
		if(index>=0)
		{
			result = model.getElementAt(index);
		}			
		
		return result;
	}
	
	public String getSelectedRoom()
	{
		return (String) getSelected(roomsBox, roomsModel);			
	}
	
	public Object getSelectedDevice()
	{
		return getSelected(devicesBox, deviceModel);				
	}
	
	public Object getSelectedCategory()
	{		
		return getSelected(categoryBox, categoryModel);				
	}

	private void filterCommands()
	{
		Object roomSelected = roomsBox.getSelectedItem();
		Object deviceS = devicesBox.getSelectedItem();
		Object categorySelected = categoryBox.getSelectedItem();
		
		if(roomSelected != null && deviceS != null && categorySelected != null)
		{
			String roomS = roomsBox.getSelectedItem().toString();
			String categoryS = categoryBox.getSelectedItem().toString();
			boolean error = false;
			
			List<Command<?>> commands = new ArrayList<>();	
			
			String description = "";
			
			
			if(roomS.equals(CommandsUtility.ALL_ROOMS) && CommandsUtility.ALL_DEVICES.equals(deviceS)  && categoryS.equals(CommandsUtility.ALL_CATEGORIES))
			{
				// ALL DEVICES SELECTED				
				description = allDeviceSelected();
			}
			else if(roomS.equals(CommandsUtility.ALL_ROOMS) && !categoryS.equals(CommandsUtility.ALL_CATEGORIES))
			{
				// SELECTED A CATEGORY
				description = selectedACategory(categoryS, commands);				
			}
			else if(devicesBox.isEnabled() && !CommandsUtility.ALL_DEVICES.equals(deviceS))
			{
				// SELECTED A DEVICE
				description = selectedADevice(commands);
			}
			else if(!roomS.equals(CommandsUtility.ALL_ROOMS) && 
					!categoryS.equals(CommandsUtility.ALL_CATEGORIES))
			{
				// SELECTED A CATEGORY IN A ROOM				
				description = selectedACategoryInRoom(categoryS, roomS, commands);	
			}
			else if(!roomS.equals(CommandsUtility.ALL_ROOMS) && 
					categoryS.equals(CommandsUtility.ALL_CATEGORIES))
			{
				// SELECTED ALL DEVICE IN A ROOM
				description = selectedAllDevicesInRoom(roomS);	
			}
			else error = true;
			
			if(!error) 
			{
				commands.addAll(AvailableFeature
						.getDefaultFeature()
						.getCommands());
				
				if(singleDevice == null)
					selectCommand.refreshCommands(description, commands, categoryS, roomS);
				else selectCommand.refreshCommands(description, singleDevice, commands);
			}
		}
	}
	
	private String selectedAllDevicesInRoom(String room)
	{
		return "All devices in " + room;
	}
	
	private String selectedACategoryInRoom(String categoryS, String room, List<Command<?>> commands)
	{
		String description = categoryS + " devices in " + room;

		selectedACategory(categoryS, commands);
		
		return description;
	}
	
	private String selectedADevice(List<Command<?>> commands)
	{
		int index = devicesBox.getSelectedIndex();
		singleDevice = (Device)deviceModel.getElementAt(index);
						
		singleDevice.getFeatures().forEach(f->{ 
			if(f.getCommands() != null && !(f instanceof StateFeature))
				commands.addAll(f.getCommands());
		});
		
		return singleDevice.getName();
	}
	
	private String allDeviceSelected()
	{
		return "All devices";
	}
	
	private String selectedACategory(String categoryS, List<Command<?>> commands)
	{
		List<Device> devices = controller.getDevicesByCategory(categoryS);
		String description = "";
		
		if(!devices.isEmpty())
		{
			Optional<DeviceCategory> op = AvailableFeature.getCategoryByName(categoryS);
			
			if(!op.isEmpty())
			{
				List<Command<?>> filter = devices
						.get(0)
						.castToFeature(op
								.get()
								.getClass())
						.getCommands();
				
				if(filter != null)
					commands.addAll(filter);
				
				description = op.get().getCategoryName() + " Category";
			}					
		}
		
		return description;
	}

	private void initDeviceBoxListener()
	{
		devicesBox.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				filterCommands();
			}
		});
	}
	
	private void initCategoryBoxListener()
	{
		categoryBox.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				if(!e.getItem().equals(CommandsUtility.ALL_CATEGORIES))
					devicesBox.setEnabled(false);	
				else devicesBox.setEnabled(true);
				filterCommands();
			}
		});
	}
	
	private void initRoomBoxListener()
	{
		roomsBox.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				filterDevices();
				filterCategory();
				
			}
		});
	}
	private void filterCategory()
	{
		if(roomsBox.getSelectedItem()!=null && roomsBox.getItemCount() != 1)
		{
			categoryBox.setEnabled(true);
			categoryModel.removeAllElements();
			categoryModel.addElement(CommandsUtility.ALL_CATEGORIES);
			if(roomsBox.getSelectedItem().equals(CommandsUtility.ALL_ROOMS))
			{
				categoryModel.addAll(controller.getAllCategories());
			}
			else
				categoryModel.addAll(controller
									.getCategoriesByRoom(roomsBox
														.getSelectedItem().toString()));
		}
		else categoryBox.setEnabled(false);
		
	}
	
	private void filterDevices()
	{
		if(roomsBox.getSelectedItem()!=null && roomsBox.getItemCount() != 1)
		{
			devicesBox.setEnabled(true);
			deviceModel.removeAllElements();
			deviceModel.addElement(CommandsUtility.ALL_DEVICES);
			if(roomsBox.getSelectedItem().equals(CommandsUtility.ALL_ROOMS))
			{
				deviceModel.addAll(controller.getAllDevices());
			}
			else
				deviceModel.addAll(controller
									.getDevicesByRoom(roomsBox
														.getSelectedItem().toString()));
		}
		else devicesBox.setEnabled(false);
			
	}
	
	public void refreshRooms()
	{
		List<String> rooms = controller.getRoomsList();
		
		if(!rooms.isEmpty())
		{
			roomsBox.setEnabled(true);
			
			roomsModel.removeAllElements();
			roomsModel.addElement(CommandsUtility.ALL_ROOMS);
			roomsModel.addAll(controller.getRoomsList());
			roomsBox.setSelectedItem(CommandsUtility.ALL_ROOMS);
			singleDevice = null;
			filterDevices();
			filterCategory();
		}
		else {
			devicesBox.setEnabled(false);
			categoryBox.setEnabled(false);
			roomsBox.setEnabled(false);
		}
	}
	
	
	private void init()
	{
		initRoomBoxListener();
		initCategoryBoxListener();
		initDeviceBoxListener();
		
		GridBagConstraints constraints = new GridBagConstraints();
		setLayout(new GridBagLayout());
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weighty = 1f;
		constraints.weightx = 1f;
		constraints.gridwidth = 3;
		add(subtitleDevice, constraints);
		
		constraints.gridy ++;
		add(tutorial, constraints);
		
		constraints.insets = new Insets(20,0,0,20);
		constraints.gridy ++;
		constraints.gridwidth = 1;
		add(HAUtilities.newDescription("Select room"), constraints);
		
		constraints.gridx ++;
		add(HAUtilities.newDescription("Select device"), constraints);
		
		constraints.gridx ++;
		add(HAUtilities.newDescription("Select category"), constraints);
		
		
		constraints.gridx = 0;		
		roomsBox.setPrototypeDisplayValue(PROTOTYPE_TEXT);
		devicesBox.setPrototypeDisplayValue(PROTOTYPE_TEXT);
		categoryBox.setPrototypeDisplayValue(PROTOTYPE_TEXT);
		
		constraints.gridy ++;
		add(roomsBox, constraints);		
		
		constraints.gridx ++;
		add(devicesBox, constraints);
		
		constraints.gridx ++;
		constraints.insets.right = 0;
		add(categoryBox, constraints);
	}	

	
	@Override
	public void reloadColors()
	{
		setBackground(HAUtilities.getBackgroundColor());
	}


}
