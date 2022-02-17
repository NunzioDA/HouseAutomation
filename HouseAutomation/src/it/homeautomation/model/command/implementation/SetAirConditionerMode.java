package it.homeautomation.model.command.implementation;

import it.homeautomation.model.command.SingleValueCommand;
import it.homeautomation.model.features.implementation.AirConditioning;
import it.homeautomation.model.features.implementation.AirConditioning.Mode;

public class SetAirConditionerMode extends SingleValueCommand<AirConditioning.ModeFeature, AirConditioning.Mode>
{

	@Override
	public void execute()
	{
		getDeviceFeature().setValue(getValue());
		
	}

	@Override
	public Class<Mode> valueType()
	{
		return AirConditioning.Mode.class;
	}

	@Override
	public String toString()
	{
		return "Set air Mode";
	}
}
