package it.homeautomation.model.command;

import java.util.List;

import it.homeautomation.model.features.DeviceFeature;

public interface Command<T extends DeviceFeature>
{
	public void execute();
	public List<Class<?>> getValuesTypes();
	public void setFeature(T feature);
	public T getDeviceFeature();
}
