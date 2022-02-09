package it.homeautomation.controller;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import it.homeautomation.model.Device;
import it.homeautomation.model.command.Command;
import it.homeautomation.model.command.SingleValueCommand;
import it.homeautomation.model.features.DeviceFeature;

/**
 * CommandsGroupUtility performs a refresh of a commands group based
 * on the given filter.
 * 
 * A commands group is defined as a set of commands of the same type
 * and value performing the action on a set of devices.
 * Of course all the devices in the set, MUST have the feature the command
 * is based on.
 * For this purpose the class filter the devices based on the filters and
 * the given command.
 * 
 * This class is useful for :
 * -updating routines when a device is deleted or 
 * added
 * -creating commands.
 * 
 * @author Nunzio D'Amore
 *
 */

public class CommandsGroupUtility
{	
	public HouseAutomationController controller;
	
	public CommandsGroupUtility(HouseAutomationController controller)
	{
		this.controller = controller;
	}
	
	public static boolean isAlphanumeric(Class<?> commandValueClass)
	{
		return (commandValueClass.equals(Float.class)|| commandValueClass.equals(Integer.class)|| commandValueClass.equals(String.class));
	}
	
	@SuppressWarnings("unchecked")
	public static String manageSingleValueCommands(Command<?> command, Object inputValue)
	{
		String error = "";
		//SingleValueCommand have only 1 value
		Class<?> commandValueClass = command.getValuesTypes().get(0);
		Object inputParsed = null;
		
		if(isAlphanumeric(commandValueClass))
		{
			String userInput = (String)inputValue;
			
			if(!userInput.isEmpty())
			{
				// Cast command to the right value
				if(commandValueClass.equals(Float.class))
				{
					try {
						inputParsed = Float.parseFloat(userInput);
					}
					catch(NumberFormatException ex)
					{
						error = "Insert a number with a decimal point";
					}										
					
				}
				else if(commandValueClass.equals(Integer.class))
				{
					try {
						inputParsed = Integer.parseInt(userInput);						
					}
					catch(NumberFormatException ex)
					{
						error = "Insert an integer number";
					}					
				}
				else if(commandValueClass.equals(String.class))
				{
					inputParsed = userInput;
				}
			}
			else error = "Insert a value";
		}
		else if(commandValueClass.equals(Color.class) || commandValueClass.isEnum())
		{
			inputParsed = inputValue;
		}
		
		if(inputParsed != null)
			((SingleValueCommand<?, Object>)command).setValue(inputParsed);
			
		
		return error;
	}	
	
	public String refreshCommands(Object device, String category, 
			String room, List<Object> inputValues, List<Command<?>> commandsList, Command<?> selectedCommand)
	{
		List<Device> devicesAffected = null;
		String error = "";
		
		if(device instanceof String)
		{
			if(category.equals(CommandsFilterTool.ALL_CATEGORIES) // ROOM
					&& !room.equals(CommandsFilterTool.ALL_ROOMS))
			{
				devicesAffected = controller.getDevicesByRoom(room);
			}			
			else if(!category.equals(CommandsFilterTool.ALL_CATEGORIES) // CATEGORY
					&& room.equals(CommandsFilterTool.ALL_ROOMS))
			{
				devicesAffected = controller.getDevicesByCategory(category);
			}
			else if(!category.equals(CommandsFilterTool.ALL_CATEGORIES) // CATEGORY IN A ROOM
					&& !room.equals(CommandsFilterTool.ALL_ROOMS))
			{				
				devicesAffected = controller.getRoomDevicesByCategory(room, category);
			}
			else if(category.equals(CommandsFilterTool.ALL_CATEGORIES) // ALL DEVICES
					&& room.equals(CommandsFilterTool.ALL_ROOMS) 
					&& device.equals(CommandsFilterTool.ALL_DEVICES))
			{
				devicesAffected = controller.getAllDevices();
			}
		}
		else if(device instanceof Device) {
			devicesAffected = new ArrayList<>();
			devicesAffected.add((Device)device);
		}
		
		if(devicesAffected != null) 
		{
			error = computeDevices(devicesAffected, inputValues, commandsList, selectedCommand);
		}
		
		return error;
	}
	
	private String computeDevices(List<Device> devicesAffected, List<Object> inputValues, List<Command<?>> commandsList, Command<?> selectedCommand)
	{
		String error = "";
		commandsList.clear();
		
		for(Device device : devicesAffected)
		{
			DeviceFeature feature = selectedCommand.getDeviceFeature();
			
			Optional<Command<?>> commandOp = device
			.castToFeature(feature.getClass())
			.getCommands()
			.stream()
			.filter(c -> c.getClass().equals(selectedCommand.getClass()))
			.findAny();						
			
			
			if(!commandOp.isEmpty()) 
			{
				Command<?> command = commandOp.get();
				
				if(command instanceof SingleValueCommand<?, ?>)
				{
					error = manageSingleValueCommands(command, inputValues.get(0));						
				}
				
				if(error.isEmpty())
				{
					commandsList.add(command);
				}
			}
		}
		return error;
	}
	
}
