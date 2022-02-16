package it.homeautomation.model.features.implementation;


import java.util.ArrayList;
import java.util.List;

import it.homeautomation.model.command.Command;
import it.homeautomation.model.command.implementation.DimmableCommand;

public class Dimmable extends PercentageFeature{
	
	private static int DEFAULT = 100;
	
	public Dimmable()
	{
		setValue(DEFAULT);
	}
	
	@Override
	public List<Command<?>> getCommands()
	{
		List<Command<?>> commandsList = new ArrayList<>();
		DimmableCommand dimmableCommand = new DimmableCommand();
		dimmableCommand.setFeature(this);
		
		commandsList.add(dimmableCommand);
		
		return commandsList;
	}
	
	@Override
	public Dimmable getClone()
	{		
		Dimmable clone = new Dimmable();
		clone.setValue(super.getValue());
		
		return clone;
	}
	
	@Override
	public String toString()
	{
		return "Dimmable";
	}

	@Override
	public String getSateRappresentation()
	{		
		String value = (getValue() != null)? "Brigh." +  getValue().toString() + "%" : "none";
		
		return value;
	}

	@Override
	public String getIconID()
	{
		return "dimmable";
	}

}