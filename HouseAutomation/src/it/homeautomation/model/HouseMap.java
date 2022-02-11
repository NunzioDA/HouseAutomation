package it.homeautomation.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import it.homeautomation.hagui.HAUtilities;
import it.homeautomation.model.features.DeviceFeature;


/**
 * 
 * <p> This class describe the House structure. 
 * The house is composed of rooms in which the devices are contained. 
 * It perform a categorization of the devices based on the DeviceCategory class.
 * It also contains all the routines.
 * 
 * @author Nunzio D'Amore
 *
 */

public class HouseMap implements Model{

	private String name;
	private Map<String,List<Device>> roomsMap = new LinkedHashMap<>();
	private Map<String, List<Device>> categoriesMap = new HashMap<>();
	private List<Routine> routines = new ArrayList<>();
	
	private AvailableCommandsFilterTool commandsFilterTool;
	private CommandsGroupUtility commandsGroupUtility;
	
	public HouseMap()
	{
		commandsFilterTool = new AvailableCommandsFilterTool(this);
		commandsGroupUtility = new CommandsGroupUtility(this);
		
	}
	
	@Override
	public CommandsGroupUtility getCommandsGroupUtility()
	{
		return commandsGroupUtility;
	}
	
	@Override
	public AvailableCommandsFilterTool getCommandsFilterTool()
	{
		return commandsFilterTool;
	}
	
	private boolean isIdPresent(int id)
	{
		return this
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
	
	public boolean isDeviceNamePresent(String name)
	{
		return getAllDevices()
				.stream()
				.anyMatch(d->d.getName().equals(name));
	}	
	/**
	 * Generate a random id not used in the room map.
	 * 
	 * @return unique id
	 */
	private int getUniqueId()
	{
		Random random = new Random();
		int id;
		
		do {
			id = random.nextInt();
		}while(isIdPresent(id));
		
		return id;
	}
	
	/**
	 * 
	 * @throws NullPointerException if {@code room} or {@code newDevice} is {@code null}
	 */
	@Override
	public boolean addDevice(String name, String room, List<DeviceFeature> features, boolean isAGroup)	
	{		
		boolean success = true;
		
		name = HAUtilities.capitalize(name);
		room = HAUtilities.capitalize(room);
		
		if(!isDeviceNamePresent(name) && features != null && !features.isEmpty())
		{
			Device device = DeviceFactory.createDevice(name , getUniqueId(), features, isAGroup);	
			
			HouseMaps.addToMapList(roomsMap, room, device);
			addDeviceIntoCategoryMap(device);
			
			getRoutines().stream().forEach(r -> r.update(this));			
		}
		else success = false;
		
		return success;
	}
	
	@Override
	public boolean addNewDeviceToGroup(DeviceGroup deviceGroup, String name, List<DeviceFeature> features)
	{
		boolean success = true;
		name = HAUtilities.capitalize(name);
		String deviceName = deviceGroup.getName() +" -> " + name;
		
		if(!isDeviceNamePresent(deviceName) 
				&& isDevicePresent(deviceGroup))
		{		
			int uniqueID = getUniqueId();
			
			Device child = DeviceFactory.createDevice(deviceName, uniqueID, features, false);
			
			
			deviceGroup.add(child);
			addDeviceIntoCategoryMap(child);			
		}
		else success = false;
		
		return success;
	}
	
	@Override
	public List<Device> getAllDevices()
	{
		List<Device> allDevices = new ArrayList<>();
		
		for(Device device : getDevicesList())
		{
			allDevices.add(device);
			
			if(device instanceof DeviceGroup)
			{
				allDevices.addAll(((DeviceGroup)device).getChildren());
			}
		}		
		
		return allDevices;
	}

	private void addDeviceIntoCategoryMap(Device device)
	{
		HouseMaps.splitDeviceIntoCategoryMap(categoriesMap, device);
	}
	
	@Override
	public List<DeviceGroup> getAllDeviceGroups()
	{
		return getAllDevices()
				.stream()
				.filter(d -> (d instanceof DeviceGroup))
				.map(d -> (DeviceGroup)d)
				.toList();
	}
	
	@Override	
	public void deleteDevice(Device device)
	{
		HouseMaps.removeFromMapList(roomsMap, device);
		HouseMaps.removeFromMapList(categoriesMap, device);	
		
		List<DeviceGroup> group = getAllDeviceGroups();
		group.stream().forEach(d -> d.removeChild(device));
		
		if(device instanceof DeviceGroup)
		{
			((DeviceGroup)device)
			.getChildren()
			.stream()
			.forEach(this::deleteDevice);
		}
		
		getRoutines().stream().forEach(r -> r.update(this));
		
	}
	
	@Override
	public List<Device> getDevicesList()
	{
		List<Device> devices = new ArrayList<>();
		
		roomsMap
		.values()
		.stream()
		.forEach(devices::addAll);
		
		return Collections.unmodifiableList(devices);
	}

	@Override
	public Map<String,List<Device>> getRoomsMap()
	{
		return Collections.unmodifiableMap(roomsMap);
	}
	
	@Override
	public List<Device> getDevicesByCategory(String category)
	{
		return getCategoryMap().get(category);
	}	
	
	@Override
	public List<Device> getDevicesByRoom(String room)
	{
		List<Device> roomDevices = new ArrayList<>();
		
		for(Device device : getRoomsMap().get(room))
		{
			roomDevices.add(device);
			
			if(device instanceof DeviceGroup)
			{
				roomDevices.addAll(((DeviceGroup) device).getChildren());
			}
		}
				
		return roomDevices;
	}
		
	/**
	 * Maps the devices contained in a specified room into a category map.
	 * @param room name
	 * @return category map
	 */
	@Override
	public Map<String,List<Device>> getRoomInCategoriesMap(String room)
	{
		List<Device> devicesInRoom = getRoomsMap().get(room);
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
	

	@Override
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
	
	
	@Override
	public  Map<String, List<Device>> getCategoryMap()
	{
		return Collections.unmodifiableMap(categoriesMap);
	}

	private boolean isRoutinePresent(Routine routine)
	{
		return routines
				.stream()
				.anyMatch(r -> r
						.getName()
						.equals(routine
								.getName()));
	}
	
	@Override
	public boolean addRoutine(Routine routine)
	{
		boolean result = false;
		
		if(!isRoutinePresent(routine) && routine != null 
				&& !routine.getName().isEmpty()
				&& !routine.getCommands().isEmpty()) 
		{
			routines.add(routine);
			result = true;
		}
		
		return result;
	}
	
	@Override
	public List<Routine> getRoutines()
	{
		return Collections.unmodifiableList(routines);
	}
	
	@Override
	public void setName(String name)
	{
		this.name = name;
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public String toString()
	{
		return "House @" + name + ":\nrooms map -> "+roomsMap+"\ncategories map -> "+categoriesMap;
	}

	@Override
	public void deleteRoutine(Routine selectedRoutine)
	{
		routines.remove(selectedRoutine);
	}

	@Override
	public boolean isFeatureStillAvailable(DeviceFeature feature)
	{
		return getAllDevices()
				.stream()
				.anyMatch(d -> d.getFeatures().contains(feature));
	}

}
