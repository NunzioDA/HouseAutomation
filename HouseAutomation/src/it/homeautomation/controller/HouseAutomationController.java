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
		startWelcomeFrame();
		commandsFilterTool = new CommandsFilterTool(this);
		commandsGroupUtility = new CommandsGroupUtility(this);
	}
	
	public void startMainFrame(String houseName)
	{
		housemap = new HouseMap(houseName);
		new MainFrame("Home Automation", this, 1220, 700);
	}
	
	private void startWelcomeFrame()
	{
		new WelcomeFrame("Welcome", 500, 500, this);
	}
	public String getHouseName()
	{
		return housemap.getName();
	}
	
	public CommandsGroupUtility getCommandsGroupUtility()
	{
		return commandsGroupUtility;
	}
	
	public CommandsFilterTool getCommandsFilterTool()
	{
		return commandsFilterTool;
	}
	
	public void addDevice(String name, String room, List<DeviceFeature> features, boolean isAGroup)
	{
		int uniqueID = housemap.getUniqueId();
		
		Device device = DeviceFactory.createDevice(HAUtilities.capitalize(name), uniqueID, features, isAGroup);		
		housemap.addDevice(HAUtilities.capitalize(room), device);
		
		housemap.getRoutines().stream().forEach(r -> r.update(this));
	}
	
	public void addNewDeviceToGroup(DeviceGroup deviceGroup, String name, List<DeviceFeature> features)
	{
		int uniqueID = housemap.getUniqueId();
		String deviceName = HAUtilities.capitalize(name) + " in " + deviceGroup.getName();
		Device child = DeviceFactory.createDevice(deviceName, uniqueID, features, false);
		
		housemap.addNewDeviceGroupChild(deviceGroup, child);
	}
	
	public void deleteDevice(Device device)
	{
		housemap.deleteDevice(device);
		
		List<DeviceGroup> group = getAllDeviceGroup();
		group.stream().forEach(d -> d.removeChild(device));
		
		housemap.getRoutines().stream().forEach(r -> r.update(this));
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
	
	public List<DeviceGroup> getAllDeviceGroup()
	{
		return getAllDevices()
				.stream()
				.filter(d -> (d instanceof DeviceGroup))
				.map(d -> (DeviceGroup)d)
				.toList();
	}
	
	
	public Set<Map.Entry<String, List<Device>>> getRoomsEntrySet()
	{
		return housemap.getRoomsMap().entrySet();
	}
	public List<String> getRoomsList()
	{
		return housemap.getRoomsList();
	}
	public List<Device> getDevicesByRoom(String room)
	{
		return housemap.getDeviceByRoom(room);
	}
	
	public List<Device> getDevicesByCategory(String category)
	{
		return housemap.getCategoryMap().get(category);
	}
	
	public List<Device> getAllDevices()
	{
		return housemap.getDevicesList();
	}
	
	public List<String> getAllCategories()
	{
		return new ArrayList<>(housemap.getCategoryMap().keySet());
	}
	public List<String> getCategoriesByRoom(String room)
	{
		return new ArrayList<>(housemap.getRoomInCategoriesMap(room).keySet());
	}

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
		return housemap.addRoutine(currentRoutine);		
	}

	public boolean isFeatureStillAvailable(DeviceFeature feature)
	{
		return housemap.getDevicesList().stream().anyMatch(d -> d.getFeatures().contains(feature));
	}

	public void deleteRoutine(Routine selectedRoutine)
	{
		housemap.deleteRoutine(selectedRoutine);		
	}

}
