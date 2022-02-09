package it.homeautomation.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.homeautomation.hagui.HAUtilities;
import it.homeautomation.model.Device;
import it.homeautomation.model.DeviceFactory;
import it.homeautomation.model.DeviceGroup;
import it.homeautomation.model.HouseMap;
import it.homeautomation.model.Routine;
import it.homeautomation.model.features.DeviceFeature;
import it.homeautomation.view.MainFrame;
import it.homeautomation.view.WelcomeFrame;

public class HouseAutomationController
{
	private HouseMap housemap;	
	private CommandsFilterTool commandsFilterTool;
	private CommandsGroupUtility commandsGroupUtility;
	
	public HouseAutomationController()
	{
		commandsFilterTool = new CommandsFilterTool(this);
		commandsGroupUtility = new CommandsGroupUtility(this);
	}
	
	// MAIN
	
	public CommandsGroupUtility getCommandsGroupUtility()
	{
		return commandsGroupUtility;
	}
	
	public CommandsFilterTool getCommandsFilterTool()
	{
		return commandsFilterTool;
	}
	
	public void startNewHouse(String houseName)
	{
		houseName = HAUtilities.capitalize(houseName);
		housemap = new HouseMap(houseName);
	}
	
	public void startMainFrame(String houseName)
	{		
		startNewHouse(houseName);
		new MainFrame("Home Automation", this, 1220, 700);
	}
	
	public void startWelcomeFrame()
	{
		new WelcomeFrame("Welcome", 500, 500, this);
	}
	
	public String getHouseName()
	{
		return housemap.getName();
	}
	
	
	//DEVICE MANAGEMENT
	
	public List<Device> getAllDevices()
	{
		return housemap.getDevicesList();
	}
	
	public boolean isDeviceNamePresent(String name)
	{
		return getAllDevices()
				.stream()
				.anyMatch(d->d.getName().equals(name));
	}
	
	public boolean addDevice(String name, String room, List<DeviceFeature> features, boolean isAGroup)
	{
		boolean success = true;
		
		name = HAUtilities.capitalize(name);
		if(!isDeviceNamePresent(name) && features != null && !features.isEmpty())
		{
			int uniqueID = housemap.getUniqueId();
			
			Device device = DeviceFactory.createDevice(name , uniqueID, features, isAGroup);		
			housemap.addDevice(HAUtilities.capitalize(room), device);
			
			housemap.getRoutines().stream().forEach(r -> r.update(this));
		}
		else success = false;
		
		return success;
	}
	
	public boolean addNewDeviceToGroup(DeviceGroup deviceGroup, String name, List<DeviceFeature> features)
	{
		boolean success = true;
		name = HAUtilities.capitalize(name);
		
		if(!isDeviceNamePresent(name))
		{		
			int uniqueID = housemap.getUniqueId();
			String deviceName = deviceGroup.getName() +" -> " + name;
			Device child = DeviceFactory.createDevice(deviceName, uniqueID, features, false);
			
			housemap.addNewDeviceGroupChild(deviceGroup, child);
		}
		else success = false;
		
		return success;
	}
	
	public List<DeviceGroup> getAllDeviceGroups()
	{
		return getAllDevices()
				.stream()
				.filter(d -> (d instanceof DeviceGroup))
				.map(d -> (DeviceGroup)d)
				.toList();
	}
	
	public List<Device> getRoomDevicesByCategory(String room, String category)
	{
		List<Device> devices = housemap.getRoomInCategoriesMap(room).get(category);
		
		List<Device> categoryDevices = getDevicesByCategory(category);
		
		getDevicesByRoom(room)
		.stream()
		.filter(d -> (d instanceof DeviceGroup))
		.forEach(dg -> devices.addAll(
				((DeviceGroup)dg)
				.getChildren()
				.stream()
				.filter(categoryDevices::contains)
				.toList()
				)
		);
		
		return devices;
	}
	
	
	public List<Device> getDevicesByRoom(String room)
	{
		List<Device> roomDevices = new ArrayList<>();
		
		for(Device device : housemap.getDeviceByRoom(room))
		{
			roomDevices.add(device);
			
			if(device instanceof DeviceGroup)
			{
				roomDevices.addAll(((DeviceGroup) device).getChildren());
			}
		}
				
		return roomDevices;
	}
		
	public List<Device> getDevicesByCategory(String category)
	{
		return housemap.getCategoryMap().get(category);
	}

	public boolean isFeatureStillAvailable(DeviceFeature feature)
	{
		return housemap.getDevicesList().stream().anyMatch(d -> d.getFeatures().contains(feature));
	}	
	
	public void deleteDevice(Device device)
	{
		housemap.deleteDevice(device);
		
		List<DeviceGroup> group = getAllDeviceGroups();
		group.stream().forEach(d -> d.removeChild(device));
		
		housemap.getRoutines().stream().forEach(r -> r.update(this));
	}
	
	//ROOMS
	
	public List<String> getAllRooms()
	{
		return housemap.getRoomsList();
	}	
	
	public Set<Map.Entry<String, List<Device>>> getRoomsMapEntrySet()
	{
		return housemap.getRoomsMap().entrySet();
	}
	
	// CATEGORIES
	
	public List<String> getAllCategories()
	{
		return new ArrayList<>(housemap.getCategoryMap().keySet());
	}
	public List<String> getCategoriesByRoom(String room)
	{
		return new ArrayList<>(housemap.getRoomInCategoriesMap(room).keySet());
	}

	// ROUTINE
	
	public Routine getRoutineInstance()
	{
		return new Routine("");
	}
	
	public List<Routine> getRoutines()
	{
		return housemap.getRoutines();
	}

	public boolean addRoutine(Routine currentRoutine)
	{	
		boolean success = false;
		
		if(currentRoutine != null 
				&& !currentRoutine.getName().isEmpty()
				&& !currentRoutine.getCommands().isEmpty())
			
			success = housemap.addRoutine(currentRoutine);	
		
		return success;		
	}

	public void deleteRoutine(Routine selectedRoutine)
	{
		housemap.deleteRoutine(selectedRoutine);		
	}

}
