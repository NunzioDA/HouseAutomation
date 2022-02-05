package it.homeautomation.controller;

import java.awt.Color;
import java.util.List;
import java.util.Optional;

import it.homeautomation.model.Device;
import it.homeautomation.model.command.Command;
import it.homeautomation.model.command.SingleValueCommand;
import it.homeautomation.model.features.DeviceFeature;

public class CommandsUtility
{
	public static final String ALL_DEVICES = "All devices";
	public static final String ALL_CATEGORIES = "All categories";
	public static final String ALL_ROOMS = "All rooms";
	
	private static boolean isAlphanumeric(Class<?> commandValueClass)
	{
		return (commandValueClass.equals(Float.class)|| commandValueClass.equals(Integer.class)|| commandValueClass.equals(String.class));
	}
	
	@SuppressWarnings("unchecked")
	private static String manageSingleValueCommands(Command<?> command, Object inputValue)
	{
		String error = "";
		//SingleValueCommand have only 1 value
		Class<?> commandValueClass = command.getValuesTypes().get(0);
		
		if(isAlphanumeric(commandValueClass))
		{
			String userInput = (String)inputValue;
			
			if(!userInput.isEmpty())
			{
				// Cast command to the right value
				if(commandValueClass.equals(Float.class))
				{
					try {
						float floatInput = Float.parseFloat(userInput);
						((SingleValueCommand<?, Float>)command).setValue(floatInput);
					}
					catch(NumberFormatException ex)
					{
						error = "Insert a number with a decimal point";
					}											
					
				}
				else if(commandValueClass.equals(Integer.class))
				{
					try {
						int integerValue = Integer.parseInt(userInput);
						
						((SingleValueCommand<?, Integer>)command).setValue(integerValue);
					}
					catch(NumberFormatException ex)
					{
						error = "Insert an integer number";
					}		
					
				}
				else if(commandValueClass.equals(String.class))
				{
					((SingleValueCommand<?, String>)command).setValue(userInput);
				}
			}
			else error = "Insert a value";
		}
		else
		{
			Color selectedColor = (Color)inputValue;
			((SingleValueCommand<?, Color>)command).setValue(selectedColor);
		}
		
		return error;
	}	
	
	public static String refreshCommands(HouseAutomationController controller, String device, String category, 
			String room, List<Object> inputValues, List<Command<?>> commandsList, Command<?> selectedCommand)
	{
		List<Device> devicesAffected = null;
		String error = "";
		
		if(category.equals(ALL_CATEGORIES) // ROOM
				&& !room.equals(ALL_ROOMS))
		{
			devicesAffected = controller.getDevicesByRoom(room);
		}			
		else if(!category.equals(ALL_CATEGORIES) // CATEGORY
				&& room.equals(ALL_ROOMS))
		{
			devicesAffected = controller.getDevicesByCategory(category);
		}
		else if(!category.equals(ALL_CATEGORIES) // CATEGORY IN A ROOM
				&& !room.equals(ALL_ROOMS))
		{				
			devicesAffected = controller.getRoomDevicesByCategory(room, category);
		}
		else if(category.equals(ALL_CATEGORIES) // ALL DEVICES
				&& room.equals(ALL_ROOMS) 
				&& device.equals(ALL_DEVICES))
		{
			devicesAffected = controller.getAllDevices();
		}

		
		if(devicesAffected != null) 
		{
			error = computeDevices(devicesAffected, inputValues, commandsList, selectedCommand);
		}
		
		return error;
	}
	
	public static String computeDevices(List<Device> devicesAffected, List<Object> inputValues, List<Command<?>> commandsList, Command<?> selectedCommand)
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
