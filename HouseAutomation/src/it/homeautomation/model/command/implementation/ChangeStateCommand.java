package it.homeautomation.model.command.implementation;

import java.util.ArrayList;
import java.util.List;

import it.homeautomation.model.command.SingleValueCommand;
import it.homeautomation.model.features.SingleValueFeature;
import it.homeautomation.model.features.implementation.StateFeature;

public class ChangeStateCommand extends SingleValueCommand<SingleValueFeature<StateFeature.State>, StateFeature.State>
{

	@Override
	public void execute()
	{
		getDeviceFeature().setValue(getValue());		
	};

	@Override
	public String toString()
	{
		return "Turn ON/OFF";
	}
	
	@Override
	public List<Class<?>> getValuesTypes()
	{
		List<Class<?>> values = new ArrayList<>();
		values.add(StateFeature.State.class);
		return values;
	}
}
