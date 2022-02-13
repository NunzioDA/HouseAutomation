package it.homeautomation.model;

import java.util.List;
import java.util.Optional;

import it.homeautomation.model.command.Command;
import it.homeautomation.model.features.DeviceCategory;
import it.homeautomation.model.features.implementation.StateFeature;

public class AvailableCommandsFilterTool
{	
	private Model model;
	
	public AvailableCommandsFilterTool(Model model)
	{
		this.model = model;
	}
	
	public String filterCommands(Object roomSelected, Object deviceS, Object categorySelected, List<Command<?>> commands)
	{
		String description = "";
		
		if(roomSelected != null && deviceS != null && categorySelected != null)
		{
			String roomS = roomSelected.toString();
			String categoryS = categorySelected.toString();
			boolean error = false;	
			
			
			if(roomS.equals(Filter.ALL_ROOMS) && Filter.ALL_DEVICES.equals(deviceS)  
					&& categoryS.equals(Filter.ALL_CATEGORIES))
			{
				// ALL DEVICES SELECTED				
				description = allDeviceSelected();
			}
			else if(roomS.equals(Filter.ALL_ROOMS) && !categoryS.equals(Filter.ALL_CATEGORIES))
			{
				// SELECTED A CATEGORY
				description = selectedACategory(categoryS, commands);				
			}
			else if(!Filter.ALL_DEVICES.equals(deviceS))
			{
				// SELECTED A DEVICE
				description = selectedADevice((Device)deviceS, commands);
			}
			else if(!roomS.equals(Filter.ALL_ROOMS) && 
					!categoryS.equals(Filter.ALL_CATEGORIES))
			{
				// SELECTED A CATEGORY IN A ROOM				
				description = selectedACategoryInRoom(categoryS, roomS, commands);	
			}
			else if(!roomS.equals(Filter.ALL_ROOMS) && 
					categoryS.equals(Filter.ALL_CATEGORIES))
			{
				// SELECTED ALL DEVICE IN A ROOM
				description = selectedAllDevicesInRoom(roomS);	
			}
			else error = true;
			
			if(!error) 
			{
				commands.addAll(AvailableFeature
						.getDefaultFeature()
						.getCommands());
			}
			else description = null;
		}
		
		return description;
	}
	
	private String selectedAllDevicesInRoom(String room)
	{
		return "All devices in " + room;
	}
	
	private String selectedACategoryInRoom(String categoryS, String room, List<Command<?>> commands)
	{
		String description = categoryS + " devices in " + room;

		selectedACategory(categoryS, commands);
		
		return description;
	}
	
	private String selectedADevice(Device device, List<Command<?>> commands)
	{						
		device.getFeatures().forEach(f->{ 
			if(f.getCommands() != null && !(f instanceof StateFeature))
				commands.addAll(f.getCommands());
		});
		
		return device.getName();
	}
	
	private String allDeviceSelected()
	{
		return "All devices";
	}
	
	private String selectedACategory(String categoryS, List<Command<?>> commands)
	{
		List<Device> devices = model.getDevicesByCategory(categoryS);
		String description = "";
		
		if(!devices.isEmpty())
		{
			Optional<DeviceCategory> op = AvailableFeature.getCategoryByName(categoryS);
			
			if(!op.isEmpty())
			{
				List<Command<?>> filter = devices
						.get(0)
						.castToFeature(op
								.get()
								.getClass())
						.getCommands();
				
				if(filter != null)
					commands.addAll(filter);
				
				description = op.get().getCategoryName() + " Category";
			}					
		}
		
		return description;
	}
	
	
}
