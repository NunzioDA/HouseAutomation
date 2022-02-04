package it.homeautomation.view;


import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

import it.homeautomation.hagui.HAImageView;
import it.homeautomation.hagui.HALabel;
import it.homeautomation.hagui.HAPanel;
import it.homeautomation.hagui.HATextCenter;
import it.homeautomation.hagui.HATools;
import it.homeautomation.model.Device;
import it.homeautomation.view.interfaces.ListCardRenderer;

/**
 * This class is the device visualization template.
 * 
 * @author Nunzio D'Amore
 *
 */
public class DeviceCard extends HAPanel implements ListCardRenderer<Device>
{
	private static final long serialVersionUID = 1L;
	public static final Dimension dimensions = new Dimension(250,250);
	
	private HAImageView image = new HAImageView();
	private HALabel deviceName = new HALabel("", SwingConstants.LEFT);
	
	private JScrollPane myScrollPane;
	
	private JPanel stateVisualizer = new JPanel();
	private GridBagConstraints stateConstraint = new GridBagConstraints();
	
	public void initMouseListener()
	{
		addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseExited(MouseEvent e)
			{
				reloadColors();
			}
			
			@Override
			public void mouseEntered(MouseEvent e)
			{
				setBackground(HATools.getLightBackgroundColor());
				
				for(MouseListener m : myScrollPane.getMouseListeners())
					m.mouseEntered(e);
			}
			
		});
	}
	
	private void addToStateVisualizer(JPanel panel)
	{
		stateVisualizer.add(panel, stateConstraint);
		
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
		stateVisualizer.setLayout(new GridBagLayout());
		stateVisualizer.setBackground(HATools.getDarkBackgroundColor());
		stateConstraint.gridx = 0;
		stateConstraint.gridy = 0;
		stateConstraint.anchor = GridBagConstraints.NORTHWEST;
		stateConstraint.weightx = 1;
		stateConstraint.weighty = 1;
		stateConstraint.insets = new Insets(5, 5, 5, 5);
		stateConstraint.fill = GridBagConstraints.BOTH;
		
	}
	
	private void init()
	{
		initMouseListener();
		initStateVisualizer();
		image.setPreferredSize(new Dimension(dimensions.width/3, dimensions.height/3));
		
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.insets = new Insets(20, 20, 10, 20);
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 1;
		constraints.weighty = 1;
		add(image, constraints);
		
		constraints.insets.top = 10;
		constraints.insets.bottom = 0;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weighty = 0.1f;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.gridy ++;
		add(deviceName, constraints);
	
		constraints.insets.bottom = 20;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weighty = 0.5f;
		constraints.gridy ++;
		add(stateVisualizer, constraints);
		setPreferredSize(dimensions);
	}
	

	
	public DeviceCard(JScrollPane myScrollPane)
	{
		this.myScrollPane = myScrollPane;
		init();
		reloadColors();
	}

	private static JPanel stateObjectToPanel(Object object)
	{
		JPanel result = null;		
		
		if(object != null)
		{
			result = new JPanel();
			result.setLayout(new GridLayout());
			
			if(!(object instanceof Color))
			{
				result.setBackground(HATools.getLightBackgroundColor());
				String value =  object.toString();
			
				HATextCenter text = new HATextCenter(value);
				
				result.add(text);
			}
			else result.setBackground((Color)object);
			
			result.setPreferredSize(new Dimension(40,40));
		}
		
		return result;
	}

	@Override
	public Component getListCardRendererComponent(Device device)
	{
		
		deviceName.setText(device.getName());
		
		device.getFeatures().stream().forEach(f -> {
				JPanel panel = stateObjectToPanel(f.getSateRappresentation());
				if(panel != null)
					addToStateVisualizer(panel);
			});

		return this;
	}


	@Override
	public void reloadColors()
	{
		//setting shadow
		setBorder(new BevelBorder(SoftBevelBorder.RAISED, HATools.getBackgroundColor(), HATools.getShadowColor()));
		
		setBackground(HATools.getDarkBackgroundColor());
		deviceName.reloadColors();
	}
}
