package it.homeautomation.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeviceGroup extends Device
{
	List<Device> children = new ArrayList<>();
	
	public DeviceGroup(String name, int id)
	{
		super(name, id);
	}

	public void add(Device device)
	{
		children.add(device);
	}
	
	public List<Device> getChildren()
	{
		return Collections.unmodifiableList(children);
	}
	
	public boolean removeChild(Device device)
	{
		return children.remove(device);
	}
}
