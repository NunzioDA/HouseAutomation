package it.homeautomation.model.command.implementation;


import it.homeautomation.model.command.SingleValueCommand;
import it.homeautomation.model.features.implementation.RollerShutter;

public class SetOpeningRollerShutter extends SingleValueCommand<RollerShutter, Integer>
{
	@Override
	public void execute()
	{
		getDeviceFeature().setValue(getValue());
	}
	
	@Override
	public String toString()
	{
		return "Set opening percentage";
	}
	
	@Override
	public Class<Integer> valueType()
	{
		return Integer.class;
	}
}
