package it.homeautomation.model.command.implementation;

import java.util.ArrayList;
import java.util.List;

import it.homeautomation.model.command.Command;
import it.homeautomation.model.features.SingleValueFeature;
import it.homeautomation.model.features.implementation.StateFeature;
import it.homeautomation.model.features.implementation.StateFeature.State;

public class SwitchStateCommand implements Command<SingleValueFeature<StateFeature.State>>
{
	SingleValueFeature<StateFeature.State> feature;
	
	@Override
	public void execute()
	{
		feature.setValue(feature.getValue().getOppositeState());
	}

	@Override
	public String toString()
	{
		return "Switch State";
	}
	
	@Override
	public List<Class<?>> getValuesTypes()
	{
		List<Class<?>> values = new ArrayList<>();
		return values;
	}

	@Override
	public void setFeature(SingleValueFeature<State> feature)
	{
		this.feature = feature;
	}

	@Override
	public SingleValueFeature<State> getDeviceFeature()
	{
		return feature;
	}
}
