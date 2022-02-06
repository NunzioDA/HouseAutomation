package it.homeautomation.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.Optional;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

import it.homeautomation.controller.HouseAutomationController;
import it.homeautomation.hagui.HAImageView;
import it.homeautomation.hagui.HALabel;
import it.homeautomation.hagui.HAPanel;
import it.homeautomation.hagui.HAThemeListener;
import it.homeautomation.hagui.HAUtilities;
import it.homeautomation.model.Device;
import it.homeautomation.model.features.DeviceCategory;
import it.homeautomation.model.features.DeviceFeature;
import it.homeautomation.view.interfaces.DeviceDeletedListener;
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
	
	private JPanel stateVisualizer;
	
	
	private Device myDevice;
	private HouseAutomationController controller;
	private DeviceDeletedListener listener;
	
	public DeviceCard(JScrollPane myScrollPane, HouseAutomationController controller, DeviceDeletedListener listener)
	{
		this.myScrollPane = myScrollPane;
		this.controller = controller;
		this.listener = listener;
	}
	
	@Override
	public Component getListCardRendererComponent(Device device)
	{
		this.myDevice = device;
		deviceName.setText(device.getName());		
		
		stateVisualizer = new DeviceStateVisualizer(device);
		
		init();
		reloadColors();
		// visualizing main icon
		Optional<DeviceFeature> firstCategoryOpt = device
				.getFeatures()
				.stream()
				.filter(f -> (f instanceof DeviceCategory))
				.findFirst();
		
		if(!firstCategoryOpt.isEmpty())
		{
			DeviceCategory firstCategory = (DeviceCategory)firstCategoryOpt.get();
			String iconID = firstCategory.getIconID();
			URL imagePath;
			
			if(iconID != null)
			{
				imagePath = HAUtilities.getIconPath(iconID);
				image.loadImage(imagePath);
			}
			
		}

		return this;
	}

	public void initMouseListener()
	{
		addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e)
			{
				new DeviceManagementFrame(myDevice, controller, listener);
			}
			
			@Override
			public void mouseExited(MouseEvent e)
			{
				reloadColors();
				
				for(Component c : getComponents())
					if(c instanceof HAThemeListener)
						((HAThemeListener) c).reloadColors();
			}
			
			@Override
			public void mouseEntered(MouseEvent e)
			{
				setBackground(HAUtilities.getLightBackgroundColor());
				
				for(Component c : getComponents())
					if(c instanceof HAThemeListener)
						c.setBackground(HAUtilities.getLightBackgroundColor());
				
				for(MouseListener m : myScrollPane.getMouseListeners())
					m.mouseEntered(e);
			}
			
		});
	}
	
	private void init()
	{
		initMouseListener();
		image.setPreferredSize(new Dimension(dimensions.width/3, dimensions.height/3));
		
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.insets = new Insets(20, 10, 10, 10);
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
	
		constraints.insets.left = 0;
		constraints.insets.right = 0;
		constraints.insets.bottom = 20;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weighty = 0.5f;
		constraints.gridy ++;
		add(stateVisualizer, constraints);
		setPreferredSize(dimensions);
	}

	@Override
	public void reloadColors()
	{
		//setting shadow
		setBorder(new BevelBorder(SoftBevelBorder.RAISED, HAUtilities.getBackgroundColor(), HAUtilities.getShadowColor()));
		if(stateVisualizer != null)
			stateVisualizer.setBackground(HAUtilities.getBackgroundColor());
		setBackground(HAUtilities.getDarkBackgroundColor());
		deviceName.reloadColors();
	}
}
