package it.homeautomation.model.command.implementation;

import it.homeautomation.model.command.SingleValueCommand;
import it.homeautomation.model.features.implementation.TemperatureBasedFeature;

public class SetTemperature extends SingleValueCommand<TemperatureBasedFeature, Integer>
{

	@Override
	public Class<Integer> valueType()
	{
		return Integer.class;
	}

	@Override
	public void execute()
	{
		getDeviceFeature().setValue(getValue());
	}
	
	@Override
	public String toString()
	{
		String returnV = "Set temperature";
		
		if(getDeviceFeature() != null)
			returnV += "[" + getDeviceFeature().getMinTemperature() + "-" + getDeviceFeature().getMaxTemperature() + "°]";
		
		
		return returnV;
	}

}
