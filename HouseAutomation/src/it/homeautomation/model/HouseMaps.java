package it.homeautomation.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.homeautomation.model.features.DeviceCategory;
import it.homeautomation.model.features.DeviceFeature;

/**
 * 
 * <p>This class consists of {@code static} utility methods for operating
 * on HouseMap maps.
 * 
 * @author Nunzio D'Amore
 *
 */

public class HouseMaps
{
	public static <T> boolean isKeyPresent(Map<T, ?> map, T key)
	{
		return map
				.keySet()
				.stream()
				.anyMatch(key::equals);
	}
	
	
	/**
	 * 
	 * <p> The method adds the given {@code value} to the list contained at key {@code key} in the map {@code map} .
	 * It initialize the list if it is not initialized yet.
	 * 
	 * @param <T> map key type.
	 * @param <Y> type contained in the list.
	 * 
	 */
	
	public static <T, Y> void addToMapList(Map<T, List<Y>> map, T key, Y value)
	{
		if(isKeyPresent(map, key))
		{
			map
			.get(key)
			.add(value);
		}
		else 
		{
			List<Y> newListInitialization = new ArrayList<>();
			newListInitialization.add(value);
			map.put(key, newListInitialization);
		}
	}
	
	public static <K, T> void removeFromMapList(Map<K, List<T>>map, T value)
	{
		Set<K> set = new HashSet<>(map.keySet());
		
		for(K key : set)
		{
			map.get(key).remove(value);
			
			if(map.get(key).size() == 0)
				map.remove(key);
		}
	}
	
	public static void splitDeviceIntoCategoryMap(Map<String, List<Device>> map, Device newDevice)
	{
		for(DeviceFeature feature : newDevice.getFeatures())
		{
			if(feature instanceof DeviceCategory)
			{
				DeviceCategory deviceCategory = (DeviceCategory) feature;
				
				addToMapList(map, deviceCategory.getCategoryName(), newDevice);
			}		
		}
	}
}
