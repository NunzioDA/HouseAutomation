package it.homeautomation.model;

import java.util.List;
import java.util.Map;

import it.homeautomation.model.features.DeviceFeature;

public interface Model
{
	public boolean addDevice(String name, String room, List<DeviceFeature> features, boolean isAGroup);
	public boolean addNewDeviceToGroup(DeviceGroup deviceGroup, String name, List<DeviceFeature> features);
	public void deleteDevice(Device device);
	public List<Device> getDevicesList();
	public Map<String,List<Device>> getRoomsMap();
	public  Map<String, List<Device>> getCategoryMap();
	public boolean addRoutine(Routine routine);
	public List<Routine> getRoutines();
	public void setName(String name);
	public String getName();
	public void deleteRoutine(Routine selectedRoutine);
	public boolean isFeatureStillAvailable(DeviceFeature feature);
	public List<Device> getRoomDevicesByCategory(String room, String category);
	public Map<String, List<Device>> getRoomInCategoriesMap(String room);
	public List<Device> getDevicesByCategory(String category);
	public List<Device> getDevicesByRoom(String room);
	public List<Device> getAllDevices();
	public List<DeviceGroup> getAllDeviceGroups();
	public CommandsGroupUtility getCommandsGroupUtility();
	public AvailableCommandsFilterTool getCommandsFilterTool();
}
