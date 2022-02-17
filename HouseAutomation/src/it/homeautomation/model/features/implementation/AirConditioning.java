package it.homeautomation.model.features.implementation;

import java.util.ArrayList;
import java.util.List;

import it.homeautomation.model.command.Command;
import it.homeautomation.model.command.implementation.SetAirConditionerMode;
import it.homeautomation.model.features.DeviceFeature;
import it.homeautomation.model.features.SingleValueFeature;

public class AirConditioning extends TemperatureBasedFeature
{
	private ModeFeature modeFeature = new ModeFeature();
	
	public AirConditioning()
	{
		setMaxTemperature(35);
		setMinTemperature(5);
		setValue(20);
	}
	
	@Override
	public AirConditioning getClone()
	{
		AirConditioning conditoining = new AirConditioning();
		conditoining.setValue(getValue());
		return conditoining;
	}

	@Override
	public String getIconID()
	{
		return "airconditioner";
	}
	
	@Override
	public List<Command<?>> getCommands()
	{
		List<Command<?>> commands = super.getCommands();
		
		commands.addAll(modeFeature.getCommands());
		
		return commands;
	}
	
	@Override
	public List<DeviceFeature> getSubFeatures()
	{
		List<DeviceFeature> features = new ArrayList<>();
		features.add(modeFeature);
		return features;
	}
	
	@Override
	public Object getSateRappresentation()
	{
		return super.getSateRappresentation() + " " + modeFeature.getSateRappresentation();
	}
	
	@Override
	public String toString()
	{
		return "Air Conditioning";
	}

	public enum Mode
	{
		AUTO,
		COOL,
		DRY,
		FAN,
		HEAT
	}
	
	public class ModeFeature extends SingleValueFeature<AirConditioning.Mode>
	{

		public ModeFeature()
		{
			setValue(Mode.AUTO);
		}
		
		@Override
		public DeviceFeature getClone()
		{
			return null;
		}

		@Override
		public List<Command<?>> getCommands()
		{
			List<Command<?>> commands = new ArrayList<>();
			
			SetAirConditionerMode mode = new SetAirConditionerMode();
			mode.setFeature(this);
			
			commands.add(mode);
			return commands;
		}

		@Override
		public Object getSateRappresentation()
		{
			return getValue().toString();
		}

		@Override
		public String getIconID()
		{
			return null;
		}
		
	}
}
