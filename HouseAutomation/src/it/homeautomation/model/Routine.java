package it.homeautomation.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.homeautomation.controller.CommandsUtility;
import it.homeautomation.controller.HouseAutomationController;
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
	private String name;
	private List<RoutineEntry> commands = new ArrayList<>();
	
	public Routine()
	{
		name = "";
	}
	
	public Routine(String name)
	{
		this.setName(name);
	}	
	
	public void update(HouseAutomationController controller)
	{
		commands.forEach(r -> r.update(controller));
	}
	
	public void execute()
	{
		commands.forEach(RoutineEntry::execute);
	}
	
	public void addCommands(String description, List<Command<?>> commands, String device, String room, String category, List<Object> values)
	{
		this.commands.add(new RoutineEntry(description, commands, device, room, category, values));
	}
	
	public void remove(RoutineEntry routine)
	{
		commands.remove(routine);
	}
	
	public List<RoutineEntry> getCommands()
	{
		return Collections.unmodifiableList(commands);
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
	 * 
	 * @author Nunzio D'Amore
	 *
	 */
	
	public class RoutineEntry
	{
		String description;
		List<Command<?>> commandsList;
		
		String device;
		String category;
		String room;
		List<Object> valuesList = new ArrayList<>();
		
 		
 		public RoutineEntry(String description, List<Command<?>> commands, String device, String category, String room, List<Object> values)
		{
 			this.commandsList = commands;
 			this.device = device;
 			this.valuesList = values;
 			this.description = description;
 			this.category = category;
 			this.room = room;
		}

 		public void update(HouseAutomationController controller)
 		{
 			CommandsUtility.refreshCommands(controller, device, category, room, valuesList, commandsList, commandsList.get(0));
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
			return description + " " + commands;
		}
	}
}
