package it.homeautomation.model.command.implementation;

public class DimmableCommand extends PercentageCommand{

	@Override
	public String toString()
	{
		return "Change brightness" + super.toString();
	}
}
