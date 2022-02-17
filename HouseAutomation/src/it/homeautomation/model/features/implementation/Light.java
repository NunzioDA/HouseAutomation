package it.homeautomation.model.features.implementation;

import java.util.Collections;
import java.util.List;

import it.homeautomation.model.command.Command;
import it.homeautomation.model.features.DeviceCategory;
import it.homeautomation.model.features.DeviceFeature;

public class Light implements DeviceCategory
{

	@Override
	public DeviceFeature getClone()
	{
		return new Light();
	}

	@Override
	public List<Command<?>> getCommands()
	{
		return Collections.emptyList();
	}

	@Override
	public String getCategoryName()
	{
		return "Light";
	}
	
	@Override
	public String toString()
	{
		return getCategoryName();
	}

	@Override
	public Object getSateRappresentation()
	{
		return null;
	}

	@Override
	public String getIconID()
	{
		return "light";
	}

	@Override
	public List<DeviceFeature> getSubFeatures()
	{
		return Collections.emptyList();
	}
}
