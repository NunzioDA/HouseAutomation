package it.homeautomation.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.homeautomation.model.command.Command;

/**
 * 
 * <p>A Routine is a list of commands to be executed.
 * 
 * @author Nunzio D'Amore
 *
 */

public class Routine
{
	private String name = "";
	private List<RoutineEntry> commandsEntry = new ArrayList<>();
	
	public Routine(String name)
	{
		this.setName(name);
	}	
	
	public void update(Model model)
	{
		List<RoutineEntry> deletable = new ArrayList<>();
		
		commandsEntry.forEach(r ->
		{ 
			if(r.update(model))
				deletable.add(r); 
		});
		
		commandsEntry.removeAll(deletable);
	}
	
	public void execute()
	{
		commandsEntry.forEach(RoutineEntry::execute);
	}
	
	public void addCommands(String description, List<Command<?>> commands,
			String deviceFilter, String roomFilter, String categoryFilter, List<Object> values)
	{
		this.commandsEntry.add(new RoutineEntry(description, commands, deviceFilter, roomFilter, categoryFilter, values));
	}
	
	public void remove(RoutineEntry routine)
	{
		commandsEntry.remove(routine);
	}
	
	public List<RoutineEntry> getCommands()
	{
		return Collections.unmodifiableList(commandsEntry);
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	
	@Override
	public String toString()
	{		
		return name;
	}
	
	/**
	 * 
	 * A RoutineEntry is a pair String, command list.
	 * It allows to get a description of the command list.
	 * The command list contains a group of commands of the same
	 * type.
	 * 
	 * This allows the routine to be up to date when the user adds
	 * or removes a device.
	 * 
	 * @author Nunzio D'Amore
	 *
	 */
	
	public class RoutineEntry
	{
		String description;
		List<Command<?>> commandsList;
		
		Command<?> selectedCommand;
		
		String device;
		String category;
		String room;
		List<Object> valuesList;
		
 		
 		public RoutineEntry(String description, List<Command<?>> commands, String device, 
 				String room, String category, List<Object> values)
		{
 			this.commandsList = commands;
 			this.device = device;
 			this.valuesList = values;
 			this.description = description;
 			this.category = category;
 			this.room = room;
 			
 			if(commandsList != null && commandsList.size() > 0)
 				selectedCommand = commandsList.get(0);
		}

 		public boolean update(Model model)
 		{
 			boolean deletable = false;
 			
 			if(!device.equals(AvailableCommandsFilterTool.ALL_DEVICES) 
 					&& category.equals(AvailableCommandsFilterTool.ALL_CATEGORIES)) // Single device selected
 			{
 				deletable = !model.isFeatureStillAvailable(selectedCommand.getDeviceFeature());
 			} 			
 			else model.getCommandsGroupUtility().refreshCommands(device, category, room, 
 						valuesList, commandsList, selectedCommand);
 			
 			return deletable;
 		}
 		
 		public List<Command<?>> getCommandList()
 		{
 			return Collections.unmodifiableList(commandsList);
 		}
 		
		public void execute()
		{		
			commandsList.stream().forEach(Command::execute);
		}
		
		
		public String getDescription()
		{
			return description;
		}
		
		@Override
		public String toString()
		{
			return description + " " + commandsEntry;
		}
	}
}
