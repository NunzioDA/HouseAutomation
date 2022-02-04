package it.homeautomation.model.command;

import it.homeautomation.model.features.SingleValueFeature;

public abstract class SingleValueCommand <Y extends SingleValueFeature<T>, T> implements Command<Y>{

	private Y deviceFeature;
	private T value;
	
	public void setFeature(Y deviceFeature)
	{
		this.deviceFeature = deviceFeature;
	}
	
	public Y getDeviceFeature()
	{
		return deviceFeature;
	}
	
	public void setValue(T value)
	{
		this.value = value;
	}
	
	public T getValue()
	{
		return value;
	}
	
}
