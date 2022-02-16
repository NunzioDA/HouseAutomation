package it.homeautomation.model.features.implementation;

import java.util.ArrayList;
import java.util.List;

import it.homeautomation.model.command.Command;
import it.homeautomation.model.features.DeviceCategory;
import it.homeautomation.model.features.DeviceFeature;

public class RollerShutter extends PercentageFeature implements DeviceCategory
{

	public RollerShutter()
	{
		setValue(100);
	}
	
	@Override
	public DeviceFeature getClone()
	{
		RollerShutter roller = new RollerShutter();
		roller.setValue(getValue());
		return null;
	}

	@Override
	public List<Command<?>> getCommands()
	{
		List<Command<?>> commands = new ArrayList<>();
		
 		return commands;
	}

	@Override
	public Object getSateRappresentation()
	{
		return getValue() + "% opened";
	}

	@Override
	public String toString()
	{
		return "Roller shutter";
	}
	
	@Override
	public String getIconID()
	{

		return "roller";
	}

	@Override
	public String getCategoryName()
	{
		return "Roller";
	}

}
