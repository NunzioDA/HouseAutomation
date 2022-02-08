package it.homeautomation.model.features.implementation;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import it.homeautomation.model.command.Command;
import it.homeautomation.model.command.implementation.ChangeColorCommand;
import it.homeautomation.model.features.DeviceCategory;
import it.homeautomation.model.features.SingleValueFeature;

public class ColorBased extends SingleValueFeature<Color> implements DeviceCategory{

	@Override
	public ColorBased getClone()
	{		
		ColorBased clone = new ColorBased();
		clone.setValue(super.getValue());
		
		return clone;
	}
	
	@Override
	public List<Command<?>> getCommands()
	{
		List<Command<?>> commandsList = new ArrayList<>();
		
		ChangeColorCommand changeColorCommand = new ChangeColorCommand();
		changeColorCommand.setFeature(this);
		
		commandsList.add(changeColorCommand);
		
		return commandsList;
	}	
	
	@Override
	public String toString()
	{
		return "Color Based";
	}

	@Override
	public String getCategoryName()
	{
		return this.toString();
	}

	@Override
	public Color getSateRappresentation()
	{
		Color result ;
		
		if(getValue() == null)
		{			
			result = Color.black;
		}
		else result = getValue();
		
		return result;
	}

	@Override
	public String getIconID()
	{
		return "colorbased";
	}

}
