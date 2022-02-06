package it.homeautomation.model.command.implementation;

import java.util.ArrayList;
import java.util.List;

import it.homeautomation.model.command.SingleValueCommand;
import it.homeautomation.model.features.SingleValueFeature;

public class PercentageCommand extends SingleValueCommand<SingleValueFeature<Integer>, Integer>{
	
	
	@Override
	public void execute() {
		int value = (getValue() <= 100 && getValue() >= 0)? getValue() : ((getValue() > 100)? 100 : 0);
		getDeviceFeature().setValue(value);
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
		return "[0-100%]";
	}
}
