package it.homeautomation.model.command.implementation;


import it.homeautomation.model.command.SingleValueCommand;
import it.homeautomation.model.features.SingleValueFeature;

public class DimmableCommand extends SingleValueCommand<SingleValueFeature<Integer>, Integer>{

	@Override
	public void execute() {
		getDeviceFeature().setValue(getValue());
	}
	
	@Override
	public Class<Integer> valueType()
	{
		return Integer.class;
	}
	
	@Override
	public String toString()
	{
		return "Change brightness [0-100%]";
	}
}
