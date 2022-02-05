package it.homeautomation.model.features.implementation;

import java.util.ArrayList;
import java.util.List;

import it.homeautomation.model.command.Command;
import it.homeautomation.model.command.implementation.ChangeStateCommand;
import it.homeautomation.model.command.implementation.SwitchStateCommand;
import it.homeautomation.model.features.DeviceFeature;
import it.homeautomation.model.features.SingleValueFeature;

/**
 * <p>Default Feature contained in all the devices, describing whether the device is ON or OFF.
 * 
 * @author Nunzio D'Amore
 *
 */

public class StateFeature extends SingleValueFeature<StateFeature.State>
{

	public StateFeature()
	{
		setValue(StateFeature.State.ON);
	}
	
	public enum State
	{
		ON,
		OFF;
		
		public State getOppositeState()
		{
			return (this == ON)? OFF : ON;
		}
	}
	
	@Override
	public DeviceFeature getClone()
	{
		StateFeature clone = new StateFeature();
		clone.setValue(super.getValue());
		return clone;
	}

	@Override
	public List<Command<?>> getCommands()
	{
		List<Command<?>> commands = new ArrayList<>();
		ChangeStateCommand command = new ChangeStateCommand();
		command.setFeature(this);
		
		SwitchStateCommand command2 = new SwitchStateCommand();
		command2.setFeature(this);
		
		commands.add(command);
		commands.add(command2);
		
		return commands;
	}
	
	@Override
	public String toString()
	{
		return "State";
	}

	@Override
	public String getSateRappresentation()
	{
		return getValue().toString();
	}

	@Override
	public String getIconID()
	{
		return null;
	}


}
