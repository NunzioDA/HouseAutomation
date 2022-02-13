package it.homeautomation.model.features;


public abstract class SingleValueFeature<T>  implements DeviceFeature
{	
	
	private T value;

	public void setValue(T value) {
		this.value = value;
	}

	public T getValue() {
		return value;
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
