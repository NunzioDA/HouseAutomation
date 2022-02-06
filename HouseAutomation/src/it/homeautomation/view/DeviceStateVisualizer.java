package it.homeautomation.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.net.URL;

import javax.swing.JPanel;

import it.homeautomation.hagui.HAImageView;
import it.homeautomation.hagui.HATextCenter;
import it.homeautomation.hagui.HAUtilities;
import it.homeautomation.model.Device;
import it.homeautomation.model.features.DeviceFeature;

public class DeviceStateVisualizer extends JPanel
{
	private static final long serialVersionUID = 1L;
	private GridBagConstraints stateConstraint = new GridBagConstraints();
	
	public DeviceStateVisualizer(Device device)
	{
		initStateVisualizer();
		
		device.getFeatures().stream().forEach(f -> {
			HAImageView panel = stateObjectToPanel(f);
			
			if(panel != null) 
				addToStateVisualizer(panel);
		});
	
	}
	
	private void addToStateVisualizer(JPanel panel)
	{
		add(panel, stateConstraint);
		
		if(stateConstraint.gridx < 3)
		{
			stateConstraint.gridx ++;
		}
		else {
			stateConstraint.gridx = 0;
			stateConstraint.gridy ++;
		}
	}	
	
	private void initStateVisualizer()
	{
		setLayout(new GridBagLayout());
		
		stateConstraint.gridx = 0;
		stateConstraint.gridy = 0;
		stateConstraint.anchor = GridBagConstraints.NORTHWEST;
		stateConstraint.weightx = 1;
		stateConstraint.weighty = 1;
		stateConstraint.insets = new Insets(5, 5, 5, 5);
		stateConstraint.fill = GridBagConstraints.BOTH;
		
	}
	
	private HAImageView stateObjectToPanel(DeviceFeature f)
	{
		HAImageView result = null;		
		HATextCenter text = null;
		Object stateRappresentation = f.getSateRappresentation();
		
		if(stateRappresentation != null)
		{
			result = new HAImageView();
			result.setLayout(new GridLayout());
			
			if(!(stateRappresentation instanceof Color))
			{
				String value =  stateRappresentation.toString();
			
				text = new HATextCenter(value, 20);
				
				result.add(text);
			}
			else result.setBackground((Color)stateRappresentation);
			
			result.setPreferredSize(new Dimension(40,40));
		}
		
		if(result != null) 
		{	
	
			String iconID = f.getIconID();
			
			if(iconID != null)
			{
					
				URL url = HAUtilities.getIconPath(iconID);
				
				result.loadImage(url, 10);
				
				if(text != null)
					text.setBackground(HAUtilities.getDarkBackgroundColor());
			}
		
		}
		
		return result;
	}
	
}
