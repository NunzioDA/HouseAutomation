package it.homeautomation.model.command.implementation;

import java.awt.Color;

import it.homeautomation.model.command.SingleValueCommand;
import it.homeautomation.model.features.implementation.ColorBased;

public class ChangeColorCommand extends SingleValueCommand<ColorBased, Color> {	
	
	@Override
	public void execute() {
			getDeviceFeature().setValue(getValue());
	}

	@Override
	public String toString()
	{
		return "Change color";
	}

	@Override
	public Class<Color> valueType()
	{
		return Color.class;
	}
}
