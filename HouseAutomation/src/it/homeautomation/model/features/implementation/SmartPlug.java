package it.homeautomation.model.features.implementation;

import java.util.List;

import it.homeautomation.model.command.Command;
import it.homeautomation.model.features.DeviceCategory;
import it.homeautomation.model.features.DeviceFeature;

public class SmartPlug implements DeviceCategory
{

	@Override
	public DeviceFeature getClone()
	{
		return new SmartPlug();
	}

	@Override
	public List<Command<?>> getCommands()
	{
		return null;
	}

	@Override
	public Object getSateRappresentation()
	{
		return null;
	}

	@Override
	public String getIconID()
	{
		// TODO Auto-generated method stub
		return "plug";
	}

	@Override
	public String getCategoryName()
	{
		return this.getClass().getSimpleName();
	}
	
	@Override
	public String toString()
	{
		return "Smart Plug";
	}

}
