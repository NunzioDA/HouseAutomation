package it.homeautomation.model;


import java.util.List;
import java.util.Objects;

import it.homeautomation.model.features.DeviceFeature;

/**
 * 
 * <p>Simple Factory pattern to crate devices.
 * 
 * @author Nunzio D'Amore
 *
 */

public class DeviceFactory
{		
	public static Device createDevice(String name, int id, List<DeviceFeature> features)
	{
		Objects.requireNonNull(features,"");
		
		Device newDevice = new Device(name,id);	
		
		newDevice.addFeature(AvailableFeature.getDefaultFeature().getClone());
		features.forEach(newDevice::addFeature);
		
		return newDevice;
	}
	
}
