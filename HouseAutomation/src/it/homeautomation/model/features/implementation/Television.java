package it.homeautomation.model.features.implementation;


import java.util.List;
import it.homeautomation.model.command.Command;
import it.homeautomation.model.features.DeviceCategory;
import it.homeautomation.model.features.SingleValueFeature;

public class Television extends SingleValueFeature<Integer> implements DeviceCategory
{
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
		return null;
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

}
