package it.homeautomation.model;

public class Filter
{
	public static final String ALL_DEVICES = "All devices";
	public static final String ALL_CATEGORIES = "All categories";
	public static final String ALL_ROOMS = "All rooms";
	
	private Object device;
	private String room;
	private String category;
	
	public Filter(Object device, String room, String category)
	{
		this.device = device;
		this.room = room;
		this.category = category;
	}
	
	public static Filter selectAllFilter()
	{
		return new Filter(ALL_DEVICES, ALL_ROOMS, ALL_CATEGORIES);
	}
	
	public Object getDevice()
	{
		return device;
	}
	public void setDevice(Object device)
	{
		this.device = device;
	}
	public String getRoom()
	{
		return room;
	}
	public void setRoom(String room)
	{
		this.room = room;
	}
	public String getCategory()
	{
		return category;
	}
	public void setCategory(String category)
	{
		this.category = category;
	}
}
