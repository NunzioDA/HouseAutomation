package it.homeautomation.model.command.implementation;

public class SetVolumeCommand extends PercentageCommand
{
	@Override
	public String toString()
	{
		return "Set Volume " + super.toString();
	}
}
