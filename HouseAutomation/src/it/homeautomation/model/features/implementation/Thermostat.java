package it.homeautomation.model.features.implementation;

import it.homeautomation.model.features.DeviceFeature;

public class Thermostat extends TemperatureBasedFeature
{

	public Thermostat()
	{
		setMinTemperature(0);
		setMaxTemperature(35);
		setValue(10);
	}
	
	@Override
	public DeviceFeature getClone()
	{
		Thermostat thermostat = new Thermostat();
		thermostat.setValue(getValue());
		return thermostat;
	}

	@Override
	public String getIconID()
	{
		return "thermostat";
	}
	
	@Override
	public String toString()
	{
		return "Thermostat";
	}
	
}
