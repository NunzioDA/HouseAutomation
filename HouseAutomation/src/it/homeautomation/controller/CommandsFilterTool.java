package it.homeautomation.controller;

import java.util.List;
import java.util.Optional;

import it.homeautomation.model.AvailableFeature;
import it.homeautomation.model.Device;
import it.homeautomation.model.command.Command;
import it.homeautomation.model.features.DeviceCategory;
import it.homeautomation.model.features.implementation.StateFeature;

public class CommandsFilterTool
{
	private HouseAutomationController controller;
	
	public CommandsFilterTool(HouseAutomationController controller)
	{
		this.controller = controller;
	}
	
	public String selectedAllDevicesInRoom(String room)
	{
		return "All devices in " + room;
	}
	
	public String selectedACategoryInRoom(String categoryS, String room, List<Command<?>> commands)
	{
		String description = categoryS + " devices in " + room;

		selectedACategory(categoryS, commands);
		
		return description;
	}
	
	public String selectedADevice(Device device, List<Command<?>> commands)
	{						
		device.getFeatures().forEach(f->{ 
			if(f.getCommands() != null && !(f instanceof StateFeature))
				commands.addAll(f.getCommands());
		});
		
		return device.getName();
	}
	
	public String allDeviceSelected()
	{
		return "All devices";
	}
	
	public String selectedACategory(String categoryS, List<Command<?>> commands)
	{
		List<Device> devices = controller.getDevicesByCategory(categoryS);
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
	
	public String filterCommands(Object roomSelected, Object deviceS, Object categorySelected, List<Command<?>> commands)
	{
		String description = "";
		
		if(roomSelected != null && deviceS != null && categorySelected != null)
		{
			String roomS = roomSelected.toString();
			String categoryS = categorySelected.toString();
			boolean error = false;	
			
			
			if(roomS.equals(CommandsGroupUtility.ALL_ROOMS) && CommandsGroupUtility.ALL_DEVICES.equals(deviceS)  
					&& categoryS.equals(CommandsGroupUtility.ALL_CATEGORIES))
			{
				// ALL DEVICES SELECTED				
				description = controller.getCommandsFilterTool().allDeviceSelected();
			}
			else if(roomS.equals(CommandsGroupUtility.ALL_ROOMS) && !categoryS.equals(CommandsGroupUtility.ALL_CATEGORIES))
			{
				// SELECTED A CATEGORY
				description = controller.getCommandsFilterTool().selectedACategory(categoryS, commands);				
			}
			else if(!CommandsGroupUtility.ALL_DEVICES.equals(deviceS))
			{
				// SELECTED A DEVICE
				description = controller.getCommandsFilterTool().selectedADevice((Device)deviceS, commands);
			}
			else if(!roomS.equals(CommandsGroupUtility.ALL_ROOMS) && 
					!categoryS.equals(CommandsGroupUtility.ALL_CATEGORIES))
			{
				// SELECTED A CATEGORY IN A ROOM				
				description = controller.getCommandsFilterTool().selectedACategoryInRoom(categoryS, roomS, commands);	
			}
			else if(!roomS.equals(CommandsGroupUtility.ALL_ROOMS) && 
					categoryS.equals(CommandsGroupUtility.ALL_CATEGORIES))
			{
				// SELECTED ALL DEVICE IN A ROOM
				description = controller.getCommandsFilterTool().selectedAllDevicesInRoom(roomS);	
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
}