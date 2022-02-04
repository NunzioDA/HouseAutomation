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
	
}
