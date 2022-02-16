package it.homeautomation.view.implementation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import it.homeautomation.hagui.HAImageView;
import it.homeautomation.hagui.HATextCenter;
import it.homeautomation.hagui.HAUtilities;
import it.homeautomation.model.Device;
import it.homeautomation.model.features.DeviceFeature;
import it.homeautomation.view.implementation.frame.DeviceCommandExecutonFrame;

public class DeviceStateVisualizer extends JPanel
{
	private static final long serialVersionUID = 1L;
	private GridBagConstraints stateConstraint = new GridBagConstraints();
	private List<ActionListener> actListeners = new ArrayList<>();
	private boolean clickable = false;
	
	
	public DeviceStateVisualizer(Device device, boolean clickable, boolean visualizeNonRappresentableFeatures)
	{	
		this.clickable = clickable;
		
		initStateVisualizer();
		
		device.getFeatures().stream().forEach(f -> {
			HAImageView panel = featureToPanel(f);
			
			if(panel != null) 
				addToStateVisualizer(panel, f, true);
			else if (visualizeNonRappresentableFeatures){ 
				
				String iconID = f.getIconID();
				
				if(iconID != null)
				{						
					URL url = HAUtilities.getIconPath(iconID);
					HAImageView featureV = new HAImageView();
					featureV.loadImage(url);
					
					addToStateVisualizer(featureV, f, false);
				}
				
			};
		});
	
	}
	
	public void addActionListener(ActionListener actList)
	{
		actListeners.add(actList);
	}
	
	private void addFeatureClickListener(JPanel panel, DeviceFeature feature)
	{
		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e)
			{				
				new DeviceCommandExecutonFrame(500, 300, feature);
				actListeners.stream().forEach(l -> l.actionPerformed(new ActionEvent(feature, 0, "")));
			}
			
			@Override
			public void mouseEntered(MouseEvent e)
			{
				panel.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, HAUtilities.getPrimaryColor()));
			}
			
			@Override
			public void mouseExited(MouseEvent e)
			{
				panel.setBorder(BorderFactory.createEmptyBorder());
			}
		});
	}
	
	private void addToStateVisualizer(JPanel panel, DeviceFeature feature, boolean thisFeatureClickable)
	{
		if(clickable && thisFeatureClickable) {
			addFeatureClickListener(panel, feature);
		}
		
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
	
	private HAImageView featureToPanel(DeviceFeature f)
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
				
				result.loadImage(url, 5);
				
				if(text != null)
					text.setBackground(HAUtilities.getDarkBackgroundColor());
			}
		
		}
		
		return result;
	}
	
}
