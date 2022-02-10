package it.homeautomation.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


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
	
	/**
	 * 
	 * @throws NullPointerException if {@code room} or {@code newDevice} is {@code null}
	 */
	@Override
	public void addDevice(String room, Device newDevice)	
	{
		HouseMaps.addToMapList(roomsMap, room, newDevice);
		addDeviceIntoCategoryMap(newDevice);
	}
	
	@Override
	public void addDeviceIntoCategoryMap(Device device)
	{
		HouseMaps.splitDeviceIntoCategoryMap(categoriesMap, device);
	}
	
	@Override
	public void deleteDevice(Device device)
	{
		HouseMaps.removeFromMapList(roomsMap, device);
		HouseMaps.removeFromMapList(categoriesMap, device);	

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
		
		if(!isRoutinePresent(routine)) 
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
}
