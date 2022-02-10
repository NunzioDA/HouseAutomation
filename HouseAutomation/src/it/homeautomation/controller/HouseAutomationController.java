package it.homeautomation.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import it.homeautomation.hagui.HAUtilities;
import it.homeautomation.model.Device;
import it.homeautomation.model.DeviceFactory;
import it.homeautomation.model.DeviceGroup;
import it.homeautomation.model.HouseMaps;
import it.homeautomation.model.Model;
import it.homeautomation.model.Routine;
import it.homeautomation.model.features.DeviceFeature;
import it.homeautomation.view.View;
import it.homeautomation.view.implementation.HAViewImplementation;

public class HouseAutomationController
{	
	private CommandsFilterTool commandsFilterTool;
	private CommandsGroupUtility commandsGroupUtility;
	private Model model;	
	private View view;
	
	public HouseAutomationController(Model model)
	{
		commandsFilterTool = new CommandsFilterTool(this);
		commandsGroupUtility = new CommandsGroupUtility(this);
		
		this.model = model;
		view = HAViewImplementation.getSingleton();
		view.setController(this);
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
	
	private boolean isIdPresent(int id)
	{
		return model
			.getRoomsMap()		
			.values()
			.stream()
			.anyMatch(l -> l
					.stream()
					.anyMatch(d -> d
							.getId() == id) );
	}
	
	private boolean isDevicePresent(Device device)
	{
		return isIdPresent(device.getId());
	}
	
	/**
	 * Generate a random id not used in the room map.
	 * 
	 * @return unique id
	 */
	public int getUniqueId()
	{
		Random random = new Random();
		int id;
		
		do {
			id = random.nextInt();
		}while(isIdPresent(id));
		
		return id;
	}
	

	
	public String getHouseName()
	{
		return model.getName();
	}	
	
	//DEVICE MANAGEMENT
	
	public List<Device> getAllDevices()
	{
		List<Device> allDevices = new ArrayList<>();
		
		for(Device device : model.getDevicesList())
		{
			allDevices.add(device);
			
			if(device instanceof DeviceGroup)
			{
				allDevices.addAll(((DeviceGroup)device).getChildren());
			}
		}		
		
		return allDevices;
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
			int uniqueID = getUniqueId();
			
			Device device = DeviceFactory.createDevice(name , uniqueID, features, isAGroup);		
			model.addDevice(HAUtilities.capitalize(room), device);
			
			model.getRoutines().stream().forEach(r -> r.update(this));
		}
		else success = false;
		
		return success;
	}
	
	public boolean addNewDeviceToGroup(DeviceGroup deviceGroup, String name, List<DeviceFeature> features)
	{
		boolean success = true;
		name = HAUtilities.capitalize(name);
		
		if(!isDeviceNamePresent(name) && isDevicePresent(deviceGroup))
		{		
			int uniqueID = getUniqueId();
			String deviceName = deviceGroup.getName() +" -> " + name;
			Device child = DeviceFactory.createDevice(deviceName, uniqueID, features, false);
			
			
			deviceGroup.add(child);
			model.addDeviceIntoCategoryMap(child);
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
	
	/**
	 * Maps the devices contained in a specified room into a category map.
	 * @param room name
	 * @return category map
	 */
	private Map<String,List<Device>> getRoomInCategoriesMap(String room)
	{
		List<Device> devicesInRoom = model.getRoomsMap().get(room);
		Map<String,List<Device>> returnMap = new HashMap<>();
		
		for(Device device : devicesInRoom)
		{
			HouseMaps.splitDeviceIntoCategoryMap(returnMap, device);
			
			if(device instanceof DeviceGroup)
				for(Device child : ((DeviceGroup)device).getChildren() )
					HouseMaps.splitDeviceIntoCategoryMap(returnMap, child);
		}
		
		return returnMap;
	}
	
	public List<Device> getRoomDevicesByCategory(String room, String category)
	{
		List<Device> devices = getRoomInCategoriesMap(room).get(category);
		
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
		
		for(Device device : model.getRoomsMap().get(room))
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
		return model.getCategoryMap().get(category);
	}

	public boolean isFeatureStillAvailable(DeviceFeature feature)
	{
		return getAllDevices()
				.stream()
				.anyMatch(d -> d.getFeatures().contains(feature));
	}	
	
	public void deleteDevice(Device device)
	{
		model.deleteDevice(device);
		
		List<DeviceGroup> group = getAllDeviceGroups();
		group.stream().forEach(d -> d.removeChild(device));
		
		if(device instanceof DeviceGroup)
		{
			((DeviceGroup)device)
			.getChildren()
			.stream()
			.forEach(model::deleteDevice);
		}
		
		model.getRoutines().stream().forEach(r -> r.update(this));
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
		return new ArrayList<>(getRoomInCategoriesMap(room).keySet());
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
		
		if(currentRoutine != null 
				&& !currentRoutine.getName().isEmpty()
				&& !currentRoutine.getCommands().isEmpty())
			
			success = model.addRoutine(currentRoutine);	
		
		return success;		
	}

	public void deleteRoutine(Routine selectedRoutine)
	{
		model.deleteRoutine(selectedRoutine);		
	}

}
