package it.homeautomation.model.features.implementation;

import java.util.ArrayList;
import java.util.List;

import it.homeautomation.model.command.Command;
import it.homeautomation.model.command.implementation.SetTemperature;
import it.homeautomation.model.features.SingleValueFeature;

public abstract class TemperatureBasedFeature extends SingleValueFeature<Integer>
{
	int minTemperature = 0;
	int maxTemperature = 100;
	
	
	@Override
	public List<Command<?>> getCommands()
	{
		List<Command<?>> commands = new ArrayList<>();
		SetTemperature command = new SetTemperature();
		command.setFeature(this);
		commands.add(command);
		return commands;
	}
	
	public int getMaxTemperature()
	{
		return maxTemperature;
	}
	
	public int getMinTemperature()
	{
		return minTemperature;
	}
	
	public void setMinTemperature(int minTemperature)
	{
		this.minTemperature = minTemperature;
	}
	
	public void setMaxTemperature(int maxTemperature)
	{
		this.maxTemperature = maxTemperature;
	}
	
	@Override
	public Integer checkValue(Integer value)
	{
		return (value > maxTemperature)? maxTemperature : (value < minTemperature)? minTemperature : value;
	}
	
	@Override
	public Object getSateRappresentation()
	{
		return "Temp. " + getValue() + "°";
	}
	
}
