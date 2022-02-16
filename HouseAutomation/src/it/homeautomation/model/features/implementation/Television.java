package it.homeautomation.model.features.implementation;


import java.util.ArrayList;
import java.util.List;
import it.homeautomation.model.command.Command;
import it.homeautomation.model.command.implementation.ChangeChannel;
import it.homeautomation.model.features.DeviceCategory;
import it.homeautomation.model.features.SingleValueFeature;

public class Television extends SingleValueFeature<Integer> implements DeviceCategory
{
	public Television()
	{
		setValue(1);
	}
	
	@Override
	public Television getClone()
	{
		Television tv = new Television();
		tv.setValue(super.getValue());
		
		return tv;
	}
	
	@Override
	public String toString()
	{
		return "Television";
	}

	@Override
	public List<Command<?>> getCommands()
	{
		List<Command<?>> commands = new ArrayList<>();
		ChangeChannel ch = new ChangeChannel();
		ch.setFeature(this);
		commands.add(ch);
		return commands;
	}

	@Override
	public String getCategoryName()
	{
		return this.getClass().getSimpleName();
	}

	@Override
	public String getSateRappresentation()
	{		
		String value = (getValue() != null)? getValue().toString() : "none";

		return "CH-\n" + value;
	}

	@Override
	public String getIconID()
	{
		return "tv";
	}

}
