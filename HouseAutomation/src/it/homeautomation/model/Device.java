package it.homeautomation.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import it.homeautomation.model.features.DeviceFeature;

/**
 * <p>Describe a device following the Composite design pattern.
 * A device is composed by features, each of which may contains
 * fields forming the device state.
 * 
 * @author Nunzio D'Amore
 *
 */

public class Device {	

	private String name;
	
	private int id;
	
	private List<DeviceFeature> features = new ArrayList<>();

	public Device(String name, int id)
	{
		this.name = name;
		this.id = id;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getId()
	{
		return id;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends DeviceFeature> Optional<T> isFeaturePresent(Class<T> featureClass)
	{
		return (Optional<T>) features
		.stream()
		.filter(f -> featureClass.isInstance(f))
		.findAny();
	}
	
	public void addFeature(DeviceFeature feature)
	{
		if(isFeaturePresent(feature.getClass()).isEmpty())
			features.add(feature);
	}
	
	public List<DeviceFeature>  getFeatures()
	{
		return Collections.unmodifiableList(features);
	}
	
	
	/**
	 * Dynamic cast the device to a specified feature.
	 * @param <T> Type of the feature the device is going to be casted to.
	 * It will be inferred from featureClass type.
	 * @param featureClass contains the class the device is going to be casted to.
	 * @return The the requested DeviceFeature instance contained in the features list. 
	 * @throws ClassCastException when the feature is not present.
	 */
	
	public <T extends DeviceFeature> T castToFeature(Class<T> featureClass) throws ClassCastException
	{
		Optional<T> optional = isFeaturePresent(featureClass);
		
		if(optional.isEmpty())
			throw new ClassCastException("Cannot cast device to <"+featureClass.getSimpleName()
			+">. Missing feature in this device.");
		
		return (T) optional.get();
	}

	
	@Override
	public String toString()
	{
		return name;
	}
}

