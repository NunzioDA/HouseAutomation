package it.homeautomation.model.features.implementation;

import java.util.ArrayList;
import java.util.List;

import it.homeautomation.model.command.Command;
import it.homeautomation.model.command.implementation.SetOpeningRollerShutter;
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
		return roller;
	}

	@Override
	public List<Command<?>> getCommands()
	{
		List<Command<?>> commands = new ArrayList<>();
		
		SetOpeningRollerShutter openingRollerShutter = new SetOpeningRollerShutter();
		openingRollerShutter.setFeature(this);
		
		commands.add(openingRollerShutter);
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
		return "Roller shutter";
	}

}
