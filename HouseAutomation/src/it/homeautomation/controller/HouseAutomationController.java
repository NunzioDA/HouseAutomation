package it.homeautomation.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.homeautomation.hagui.HATools;
import it.homeautomation.model.Device;
import it.homeautomation.model.DeviceFactory;
import it.homeautomation.model.HouseMap;
import it.homeautomation.model.Routine;
import it.homeautomation.model.features.DeviceFeature;
import it.homeautomation.view.MainFrame;
import it.homeautomation.view.WelcomeFrame;

public class HouseAutomationController
{
	private HouseMap housemap;
	
	public void startMainFrame(String houseName)
	{
		housemap = new HouseMap(houseName);
		new MainFrame("Home Automation", this, 1220, 700);
	}
	
	private void startWelcomeFrame()
	{
		new WelcomeFrame("Welcome", 500, 500, this);
	}
	
	public HouseAutomationController()
	{
		startWelcomeFrame();
	}
	
	public String getHouseName()
	{
		return housemap.getName();
	}
	
	public void addDevice(String name, String room, List<DeviceFeature> features)
	{
		int uniqueID = housemap.getUniqueId();
		
		Device device = DeviceFactory.createDevice(HATools.capitalize(name), uniqueID, features);		
		housemap.addDevice(HATools.capitalize(room), device);
		
		housemap.getRoutines().stream().forEach(r -> r.update(this));
	}
	
	public void deleteDevice(Device device)
	{
		housemap.deleteDevice(device);
		housemap.getRoutines().stream().forEach(r -> r.update(this));
	}
	
	public List<Device> getRoomDevicesByCategory(String room, String category)
	{
		return housemap.getRoomInCategoriesMap(room).get(category);
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
