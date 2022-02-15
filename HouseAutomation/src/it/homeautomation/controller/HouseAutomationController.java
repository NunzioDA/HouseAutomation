package it.homeautomation.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.homeautomation.hagui.HAUtilities;
import it.homeautomation.model.CommandsGroupUtility;
import it.homeautomation.model.Device;
import it.homeautomation.model.DeviceGroup;
import it.homeautomation.model.Filter;
import it.homeautomation.model.Model;
import it.homeautomation.model.Routine;
import it.homeautomation.model.RoutineFactory;
import it.homeautomation.model.command.Command;
import it.homeautomation.model.features.DeviceFeature;
import it.homeautomation.view.View;
import it.homeautomation.view.implementation.HAViewImplementation;

public class HouseAutomationController
{	

	private Model model;	
	private View view;
	
	public HouseAutomationController(Model model)
	{
		this.model = model;
		view = HAViewImplementation.getSingleton();
		view.setController(this);
	}
	
	// MAIN
		
	public String getCommandsGroupDescription(String groupDescription, Command<?> command, List<Object> values)
	{
		return CommandsGroupUtility.getCommandsGroupDescription(groupDescription, command, values);
	}
	
	public String refreshCommands(Filter filter, List<Object> inputValues, List<Command<?>> commandsList, Command<?> selectedCommand)
	{
		return model
				.getCommandsGroupUtility()
				.refreshCommands(filter, inputValues, commandsList, selectedCommand);
	}
	
	public String getFilteredCommands(Object roomSelected, Object deviceS, Object categorySelected, List<Command<?>> commands)
	{
		return model.getCommandsFilterTool().filterCommands(roomSelected, deviceS, categorySelected, commands);
	}
	
	public void setHouseName(String houseName)
	{		
		houseName = HAUtilities.capitalize(houseName);
		model.setName(houseName);
	}
	
	public void startMainScreen(String houseName)
	{		
		setHouseName(houseName);
		view.mainScreen(model.getName());
	}
	
	public void startWelcomeScreen()
	{
		view.welcomeScreen();
	}
	
	public String getHouseName()
	{
		return model.getName();
	}	
	
	//DEVICE MANAGEMENT
	
	public List<Device> getAllDevices()
	{
		return model.getAllDevices();
	}
	
	public List<DeviceGroup> getAllDeviceGroups()
	{
		return model.getAllDeviceGroups();
	}
	
	public List<Device> getDevicesByRoom(String room)
	{
		return model.getDevicesByRoom(room);
	}
	
	public List<Device> getRoomDevicesByCategory(String room, String category)
	{
		return model.getRoomDevicesByCategory(room, category);
	}
	
	public List<Device> getDevicesByCategory(String category)
	{
		return model.getDevicesByCategory(category);
	}
	
	public boolean addDevice(String name, String room, List<DeviceFeature> features, boolean isAGroup)
	{
		boolean success = true;	
		
		success = model.addDevice(name, room, features, isAGroup);	

		if(success)
			view.showMessage("Device Added");
		else view.showMessage("Device name already used");
		
		return success;
	}
	
	public boolean addNewDeviceToGroup(DeviceGroup deviceGroup, String name, List<DeviceFeature> features)
	{
		boolean success = true;
		
		success = model.addNewDeviceToGroup(deviceGroup, name, features);
		
		if(success)
			view.showMessage("Device added to the group");
		else view.showMessage("A device in the group has the same name");
		
		return success;
	}
		

	public void deleteDevice(Device device)
	{
		model.deleteDevice(device);
		view.deviceStateUpdate();
		view.showMessage("Device Removed");		
	}
	
	//ROOMS
	
	public List<String> getAllRooms()
	{
		return Collections
				.unmodifiableList(new ArrayList<>(model.getRoomsMap().keySet()));
	}	
	
	public Set<Map.Entry<String, List<Device>>> getGroupedRoomsMapEntrySet()
	{
		return model.getRoomsMap().entrySet();
	}
	
	// CATEGORIES
	
	public List<String> getAllCategories()
	{
		return new ArrayList<>(model.getCategoryMap().keySet());
	}
	
	public List<String> getCategoriesByRoom(String room)
	{
		return new ArrayList<>(model.getRoomInCategoriesMap(room).keySet());
	}

	// ROUTINE
	
	public Routine getRoutineInstance()
	{
		return RoutineFactory.createEmptyRoutine();
	}
	
	public List<Routine> getRoutines()
	{
		return model.getRoutines();
	}

	public boolean addRoutine(Routine currentRoutine)
	{	
		boolean success = false;
		
		success = model.addRoutine(currentRoutine);	
		
		return success;		
	}

	public void deleteRoutine(Routine selectedRoutine)
	{
		model.deleteRoutine(selectedRoutine);		
	}

	public void executeCommand(Command<?> command)
	{
		command.execute();
		view.deviceStateUpdate();
	}
	
	public void executeCommands(List<Command<?>> commands)
	{
		commands.stream().forEach(this::executeCommand);
	}

}
