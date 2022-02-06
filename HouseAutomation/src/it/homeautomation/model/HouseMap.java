package it.homeautomation.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;


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

public class HouseMap{

	private String name;
	private Map<String,List<Device>> roomsMap = new LinkedHashMap<>();
	private Map<String, List<Device>> categoriesMap = new HashMap<>();
	private List<Routine> routines = new ArrayList<>();
	
	public HouseMap(String name)
	{
		this.name = name;
	}	

	private boolean isIdPresent(int id)
	{
		return roomsMap
			.values()
			.stream()
			.anyMatch(l -> l
					.stream()
					.anyMatch(d -> d
							.getId() == id) );
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
	
	
	private boolean isDevicePresent(Device device)
	{
		return isIdPresent(device.getId());
	}
	
	/**
	 * 
	 * @throws NullPointerException if {@code room} or {@code newDevice} is {@code null}
	 */
	public boolean addDevice(String room, Device newDevice)	
	{
		Objects.requireNonNull(room, "room must not be null");
		Objects.requireNonNull(newDevice, "device must not be null");
		
		boolean result = false;
		
		if(!isDevicePresent(newDevice))
		{
			HouseMaps.addToMapList(roomsMap, room, newDevice);
			HouseMaps.splitDeviceIntoCategoryMap(categoriesMap, newDevice);
			result = true;
		}
		
		return result;		
	}
	
	public boolean addNewDeviceGroupChild(DeviceGroup group, Device child)
	{
		boolean returnV = isDevicePresent(group);
		
		if(returnV)
		{
			returnV =  !isDevicePresent(child);
			
			if(returnV)
			{
				group.add(child);
				HouseMaps.splitDeviceIntoCategoryMap(categoriesMap, child);
			}
		}
		
		return returnV;
	}
	
	public void deleteDevice(Device device)
	{
		HouseMaps.removeFromMapList(roomsMap, device);
		HouseMaps.removeFromMapList(categoriesMap, device);
		
		if(device instanceof DeviceGroup)
		{
			((DeviceGroup)device)
			.getChildren()
			.stream()
			.forEach(d -> HouseMaps.removeFromMapList(categoriesMap, d));
		}
	}
	
	public List<Device> getDevicesList()
	{
		List<Device> devices = new ArrayList<>();
		roomsMap.values().stream().forEach(devices::addAll);
		return Collections.unmodifiableList(devices);
	}
	
	public List<Device> getDeviceByRoom(String room)
	{
		return Collections.unmodifiableList(roomsMap.get(room));
	}
	
	public List<String> getRoomsList()
	{
		return Collections.unmodifiableList(new ArrayList<>(roomsMap.keySet()));
	}
	
	public Map<String,List<Device>> getRoomsMap()
	{
		return Collections.unmodifiableMap(roomsMap);
	}
	
	public  Map<String, List<Device>> getCategoryMap()
	{
		return Collections.unmodifiableMap(categoriesMap);
	}

	/**
	 * Maps the devices contained in a specified room into a category map.
	 * @param room name
	 * @return category map
	 */
	public Map<String,List<Device>> getRoomInCategoriesMap(String room)
	{
		List<Device> devicesInRoom = roomsMap.get(room);
		Map<String,List<Device>> returnMap = new HashMap<>();
		
		System.out.println(room);
		for(Device device : devicesInRoom)
		{
			HouseMaps.splitDeviceIntoCategoryMap(returnMap, device);
			
			if(device instanceof DeviceGroup)
				for(Device child : ((DeviceGroup)device).getChildren() )
					HouseMaps.splitDeviceIntoCategoryMap(returnMap, child);
		}
		
		return returnMap;
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
	
	public List<Routine> getRoutines()
	{
		return Collections.unmodifiableList(routines);
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
	
	@Override
	public String toString()
	{
		return "House @" + name + ":\nrooms map -> "+roomsMap+"\ncategories map -> "+categoriesMap;
	}

	public void deleteRoutine(Routine selectedRoutine)
	{
		routines.remove(selectedRoutine);
	}
}
