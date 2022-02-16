package it.homeautomation.model.features.implementation;

import it.homeautomation.model.features.DeviceCategory;
import it.homeautomation.model.features.DeviceFeature;

public class Oven extends TemperatureBasedFeature implements DeviceCategory
{

	public Oven()
	{
		setMinTemperature(0);
		setMaxTemperature(250);
		setValue(0);
	}
	
	@Override
	public DeviceFeature getClone()
	{
		Oven oven = new Oven();
		oven.setValue(getValue());
		return oven;
	}

	@Override
	public String getIconID()
	{
		return "oven";
	}
	
	@Override
	public String toString()
	{
		return "Oven";
	}

	@Override
	public String getCategoryName()
	{
		return "Oven";
	}
}
