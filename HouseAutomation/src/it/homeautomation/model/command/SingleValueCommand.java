package it.homeautomation.model.command;

import java.util.ArrayList;
import java.util.List;

import it.homeautomation.model.features.SingleValueFeature;

public abstract class SingleValueCommand <Y extends SingleValueFeature<T>, T> implements ValueCommand<Y>{

	private Y deviceFeature;
	private T value;
	
	@Override
	public void setFeature(Y deviceFeature)
	{
		this.deviceFeature = deviceFeature;
	}
	public abstract Class<T> valueType();
	
	@Override
	public List<Class<?>> getValuesTypes()
	{
		List<Class<?>> values = new ArrayList<>();
		values.add(valueType());
		return values;
	}
	
	@Override
	public Y getDeviceFeature()
	{
		return deviceFeature;
	}
	
	public void setValue(T value)
	{
		this.value = deviceFeature.checkValue(value);
	}
	
	public T getValue()
	{
		return value;
	}
	
	@Override
	public List<Object> getValues()
	{
		List<Object> values = new ArrayList<>();
		values.add(value);
		return values;
	}
}
