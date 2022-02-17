package it.homeautomation.model.features;

import java.util.Collections;
import java.util.List;

public abstract class SingleValueFeature<T>  implements DeviceFeature
{	
	
	private T value;

	public void setValue(T value) {
		this.value = value;
	}

	public T getValue() {
		return value;
	}
	
	@Override
	public List<DeviceFeature> getSubFeatures()
	{
		return Collections.emptyList();
	}
	
	/**
	 * If the feature only accepts a bounded set of value
	 * the Command can process the value by calling this method.
	 * 
	 * The feature must override it if the values set is bounded.
	 * 
	 * @param command value
	 * @return processed value
	 */
	
	public T checkValue(T value)
	{
		return value;
	}
	
}
