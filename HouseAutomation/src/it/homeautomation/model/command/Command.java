package it.homeautomation.model.command;

import it.homeautomation.model.features.DeviceFeature;

public interface Command<T extends DeviceFeature>
{
	public void execute();
	public void setFeature(T feature);
	public T getDeviceFeature();
}
