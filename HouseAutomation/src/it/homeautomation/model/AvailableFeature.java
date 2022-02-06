package it.homeautomation.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import it.homeautomation.model.features.DeviceCategory;
import it.homeautomation.model.features.DeviceFeature;
import it.homeautomation.model.features.implementation.Audio;
import it.homeautomation.model.features.implementation.ColorBased;
import it.homeautomation.model.features.implementation.Dimmable;
import it.homeautomation.model.features.implementation.Light;
import it.homeautomation.model.features.implementation.SmartPlug;
import it.homeautomation.model.features.implementation.StateFeature;
import it.homeautomation.model.features.implementation.Television;

/**
 * <p>When a device is created, it requires the features of which it is composed. 
 * This class helps keeping track of all the available features
 * the user can select, providing a global access.
 * 
 * <p>This structure brings a dynamic approach to editing and adding features. 
 * When a new DeviceFeature class is created, an instance is added to the 
 * availableFeature list by editing the initSingleton method,
 * without having to apply further code changes.
 * 
 * <p>Note that it is also useful to easily remove remove deprecated features.
 * 
 * @author Nunzio D'Amore
 */
public class AvailableFeature
{
	/**
	 * Shared list containing the available features that can be added to the new  device.
	 */
	
	private static final List<DeviceFeature> availableFeatures = new ArrayList<>();

	private static final StateFeature defaultFeature = new StateFeature();
	
	/**
	 * Initialize the list with feature instances.
	 */	
	private static void initSingleton()
	{
		availableFeatures.add(new Television());
		availableFeatures.add(new Audio());
		availableFeatures.add(new Light());
		availableFeatures.add(new SmartPlug());		
		availableFeatures.add(new ColorBased());
		availableFeatures.add(new Dimmable());		
	}		
	
	/**
	 * Following the Singleton structure, this method initialize
	 * the singleton instance if it has not been initialized yet.
	 * 
	 * @return the ArrayList containing the available features.
	 */	
	public static List<DeviceFeature> getList()
	{
		if(availableFeatures.isEmpty())
			initSingleton();
		
		return Collections.unmodifiableList(availableFeatures);
	}
	
	public static StateFeature getDefaultFeature()
	{
		return defaultFeature;
	}
	
	public static Optional<DeviceCategory> getCategoryByName(String name)
	{
		Optional<DeviceFeature> feature =
		availableFeatures
		.stream()
		.filter(f -> f instanceof DeviceCategory)
		.filter(f -> ((DeviceCategory)f).getCategoryName().equals(name))
		.findAny();
		
		Optional<DeviceCategory> category = Optional.of((DeviceCategory)feature.get());
		
		return category;
	}
	
}
