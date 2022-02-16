package it.homeautomation.model.features.implementation;


import it.homeautomation.model.features.DeviceCategory;
import it.homeautomation.model.features.DeviceFeature;

public class Fridge extends TemperatureBasedFeature implements DeviceCategory
{

	public Fridge()
	{
		setMinTemperature(1);
		setMaxTemperature(5);
		setValue(5);
	}
	
	@Override
	public DeviceFeature getClone()
	{
		Fridge fridge = new Fridge();
		fridge.setValue(getValue());
		return fridge;
	}

	@Override
	public String getIconID()
	{
		return "fridge";
	}
	
	@Override
	public String toString()
	{
		return "Fridge";
	}

	@Override
	public String getCategoryName()
	{
		return "Fridge";
	}
}
