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
		String returnV = "Set ";
		
		TemperatureBasedFeature feature = getDeviceFeature();
		
		if(feature != null) {
			returnV += feature.toString() + " temperature";			
			returnV += "[" + feature.getMinTemperature() + "-" + feature.getMaxTemperature() + "°]";
			
		}else returnV += "temperature";
		
		return returnV;
	}

}
