package it.homeautomation.model.features.implementation;

import it.homeautomation.model.features.DeviceFeature;

public class Freezer extends TemperatureBasedFeature
{
	public Freezer()
	{
		setMinTemperature(-30);
		setMaxTemperature(0);
		setValue(-10);
	}
	
	@Override
	public DeviceFeature getClone()
	{
		Freezer freezer = new Freezer();
		freezer.setValue(getValue());
		return freezer;
	}

	@Override
	public String getIconID()
	{
		return "freezer";
	}
	
	@Override
	public String toString()
	{
		return "Freezer";
	}

}
