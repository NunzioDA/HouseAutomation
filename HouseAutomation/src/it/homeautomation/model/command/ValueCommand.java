package it.homeautomation.model.command;

import java.util.List;

import it.homeautomation.model.features.DeviceFeature;

public interface ValueCommand<T extends DeviceFeature> extends Command<T>
{
	public List<Class<?>> getValuesTypes();
	public List<Object> getValues();
}
