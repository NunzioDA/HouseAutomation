package it.homeautomation.view.commandmanagement;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import it.homeautomation.controller.CommandsFilterTool;
import it.homeautomation.controller.HouseAutomationController;
import it.homeautomation.hagui.HAComboBox;
import it.homeautomation.hagui.HALabel;
import it.homeautomation.hagui.HAPanel;
import it.homeautomation.hagui.HAUtilities;
import it.homeautomation.model.command.Command;

public class FilterCommandPanel extends HAPanel
{
	private static final long serialVersionUID = 1L;
	private static final String PROTOTYPE_TEXT = "wwwwwwwwww";
	
	private HALabel tutorial = HAUtilities.newDescription("Selecting a room will filter devices and categories.");
	
	
	private DefaultComboBoxModel<String> roomsModel = new DefaultComboBoxModel<>();
	private HAComboBox<String> roomsBox = new HAComboBox<>(roomsModel);
	
	
	private DefaultComboBoxModel<Object> deviceModel = new DefaultComboBoxModel<>();
	private HAComboBox<Object> devicesBox = new HAComboBox<>(deviceModel);
	
	
	private DefaultComboBoxModel<Object> categoryModel = new DefaultComboBoxModel<>();
	private HAComboBox<Object> categoryBox = new HAComboBox<>(categoryModel);
	
	private HouseAutomationController controller;
	private SelectCommandPanel selectCommand;

	public FilterCommandPanel(HouseAutomationController controller, SelectCommandPanel selectCommand)
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
			List<Command<?>> commands = new ArrayList<>();
			
			String description = controller.getCommandsFilterTool().filterCommands(roomSelected, deviceS, categorySelected, commands);
			
			if(description != null)
			{
				selectCommand.refreshCommands(description, commands, 
						categorySelected.toString(), roomSelected.toString(), deviceS);
			}
		}
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
				if(!e.getItem().equals(CommandsFilterTool.ALL_CATEGORIES))
				{
					devicesBox.setEnabled(false);
					devicesBox.setSelectedIndex(0);
				}
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
			categoryModel.addElement(CommandsFilterTool.ALL_CATEGORIES);
			if(roomsBox.getSelectedItem().equals(CommandsFilterTool.ALL_ROOMS))
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
			deviceModel.addElement(CommandsFilterTool.ALL_DEVICES);
			
			if(roomsBox.getSelectedItem().equals(CommandsFilterTool.ALL_ROOMS))
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
		List<String> rooms = controller.getAllRooms();
		
		if(!rooms.isEmpty())
		{
			roomsBox.setEnabled(true);
			
			roomsModel.removeAllElements();
			roomsModel.addElement(CommandsFilterTool.ALL_ROOMS);
			roomsModel.addAll(controller.getAllRooms());
			roomsBox.setSelectedItem(CommandsFilterTool.ALL_ROOMS);
			filterDevices();
			filterCategory();
		}
		else {
			devicesBox.setEnabled(false);
			categoryBox.setEnabled(false);
			roomsBox.setEnabled(false);
			selectCommand.clearCommandsList();
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
		constraints.weighty = 0.1f;
		constraints.weightx = 1f;
		constraints.gridwidth = 3;
		constraints.anchor = GridBagConstraints.SOUTH;
		tutorial.setFontSize(20f);
		add(tutorial, constraints);
		
		constraints.insets = new Insets(20,0,0,20);
		constraints.gridy ++;
		constraints.gridwidth = 1;
		constraints.weighty = 0.1f;
		
		add(HAUtilities.newDescription("Select room"), constraints);
		
		constraints.gridx ++;
		add(HAUtilities.newDescription("Select device"), constraints);
		
		constraints.gridx ++;
		add(HAUtilities.newDescription("Select category"), constraints);
		
		
		constraints.gridx = 0;
		constraints.weighty = 0.1f;
		constraints.insets.top = 0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.HORIZONTAL;
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
