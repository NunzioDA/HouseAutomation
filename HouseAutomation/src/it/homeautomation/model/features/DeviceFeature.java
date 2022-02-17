package it.homeautomation.model.features;

import java.util.List;


import it.homeautomation.model.command.Command;

public interface DeviceFeature 
{
	public DeviceFeature getClone();
	public List<Command<?>> getCommands();
	public Object getSateRappresentation();
	public String getIconID();
	public List<DeviceFeature> getSubFeatures();
}
