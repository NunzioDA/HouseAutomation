package it.homeautomation.model.command.implementation;

import java.util.ArrayList;
import java.util.List;

import it.homeautomation.model.command.SingleValueCommand;
import it.homeautomation.model.features.SingleValueFeature;

public class SetVolumeCommand extends SingleValueCommand<SingleValueFeature<Integer>, Integer>
{

	@Override
	public void execute() {
		getDeviceFeature().setValue(getValue());
	}
	
	@Override
	public List<Class<?>> getValuesTypes()
	{
		List<Class<?>> values = new ArrayList<>();
		values.add(Integer.class);
		return values;
	}
	@Override
	public String toString()
	{
		return "Set Volume [0-100%]";
	}
	
	@Override
	public Class<Integer> valueType()
	{
		return Integer.class;
	}
}
