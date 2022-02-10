package it.homeautomation.model;

import java.util.List;
import java.util.Map;

public interface Model
{
	public void addDevice(String room, Device newDevice);
	public void addDeviceIntoCategoryMap(Device device);
	public void deleteDevice(Device device);
	public List<Device> getDevicesList();
	public Map<String,List<Device>> getRoomsMap();
	public  Map<String, List<Device>> getCategoryMap();
	public boolean addRoutine(Routine routine);
	public List<Routine> getRoutines();
	public void setName(String name);
	public String getName();
	public void deleteRoutine(Routine selectedRoutine);
}
