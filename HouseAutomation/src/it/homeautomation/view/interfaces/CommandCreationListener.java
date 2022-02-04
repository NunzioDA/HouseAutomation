package it.homeautomation.view.interfaces;

import java.util.List;

import it.homeautomation.model.command.Command;

public interface CommandCreationListener
{
	public void commandListCreated(String description, List<Command<?>> command, List<Object> values);
}
