package it.homeautomation.view;

import java.util.List;

import it.homeautomation.model.command.Command;

public interface CommandCreationListener
{
	public void commandListCreated(String groupDescription, List<Command<?>> command, List<Object> values);
}
