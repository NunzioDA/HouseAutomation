package it.homeautomation.model.features.implementation;

import java.util.ArrayList;
import java.util.List;

import it.homeautomation.model.command.Command;
import it.homeautomation.model.command.implementation.SetVolumeCommand;
import it.homeautomation.model.features.DeviceCategory;

public class Audio extends PercentageFeature implements DeviceCategory
{

	public Audio()
	{
		setValue(100);
	}
	
	@Override
	public Audio getClone()
	{
		Audio audio = new Audio();
		audio.setValue(getValue());
		return audio;
	}

	@Override
	public List<Command<?>> getCommands()
	{
		List<Command<?>> commands = new ArrayList<>();
		SetVolumeCommand vol = new SetVolumeCommand();
		vol.setFeature(this);
		commands.add(vol);
		return commands;
	}

	@Override
	public Object getSateRappresentation()
	{
		return "Vol." + getValue()+"%";
	}

	@Override
	public String toString()
	{
		return "Audio";
	}
	
	@Override
	public String getIconID()
	{
		return "audio";
	}

	@Override
	public String getCategoryName()
	{
		return getClass().getSimpleName();
	}

}
