package it.homeautomation.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.homeautomation.hagui.HAUtilities;
import it.homeautomation.model.AvailableCommandsFilterTool;
import it.homeautomation.model.CommandsGroupUtility;
import it.homeautomation.model.Device;
import it.homeautomation.model.DeviceGroup;
import it.homeautomation.model.Model;
import it.homeautomation.model.Routine;
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
		
	public CommandsGroupUtility getCommandsGroupUtility()
	{
		return model.getCommandsGroupUtility();
	}
	
	public AvailableCommandsFilterTool getCommandsFilterTool()
	{
		return model.getCommandsFilterTool();
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

		return success;
	}
	
	public boolean addNewDeviceToGroup(DeviceGroup deviceGroup, String name, List<DeviceFeature> features)
	{
		boolean success = true;
		
		success = model.addNewDeviceToGroup(deviceGroup, name, features);
		
		if(success)
			view.showMessage("Device Added");
		else view.showMessage("The group is missing or the name is alreafy");
		
		return success;
	}
		

	public void deleteDevice(Device device)
	{
		model.deleteDevice(device);
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
		return new Routine("");
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

}
