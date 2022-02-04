package it.homeautomation.model.command.implementation;
import java.util.ArrayList;
import java.util.List;

import it.homeautomation.model.command.SingleValueCommand;
import it.homeautomation.model.features.implementation.Dimmable;

public class DimmableCommand extends SingleValueCommand<Dimmable, Integer>{
	
	@Override
	public void execute() {
		int value = (getValue() <= 100 && getValue() >= 0)? getValue() : ((getValue() > 100)? 100 : 0);
		getDeviceFeature().setValue(value);
	}

	@Override
	public String toString()
	{
		return "Change brightness [0 - 100%]";
	}
	
	@Override
	public List<Class<?>> getValuesTypes()
	{
		List<Class<?>> values = new ArrayList<>();
		values.add(Integer.class);
		return values;
	}
}
