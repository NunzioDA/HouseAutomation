package it.homeautomation.model.command.implementation;
import it.homeautomation.model.command.SingleValueCommand;
import it.homeautomation.model.features.SingleValueFeature;

public class ChangeChannel extends SingleValueCommand<SingleValueFeature<Integer>, Integer>
{

	@Override
	public void execute()
	{
		getDeviceFeature().setValue(getValue());
	}
	
	@Override
	public String toString()
	{
		return "Change Channel";
	}
	
	@Override
	public Class<Integer> valueType()
	{
		return Integer.class;
	}
}
