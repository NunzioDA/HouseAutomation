package it.homeautomation.view.implementation;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.SoftBevelBorder;

import it.homeautomation.controller.HouseAutomationController;
import it.homeautomation.hagui.HAImageView;
import it.homeautomation.hagui.HALabel;
import it.homeautomation.hagui.HAPanel;
import it.homeautomation.hagui.HAThemeListener;
import it.homeautomation.hagui.HAUtilities;
import it.homeautomation.model.Device;
import it.homeautomation.model.DeviceGroup;
import it.homeautomation.view.ListCardRenderer;
import it.homeautomation.view.implementation.frame.DeviceManagementFrame;

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
	
	public DeviceCard(JScrollPane myScrollPane)
	{
		this.myScrollPane = myScrollPane;
		this.controller = HAViewImplementation.getSingleton().getController();
	}
	
	public static URL getFirstFeatureImagePath(Device device)
	{
		// visualizing main icon
		// get the first feature excluding StatusFeature (always at index 0)
		
		String iconID = device.getFeatures().get(1).getIconID();
		URL imagePath = null;
		
		if(iconID != null)
		{
			imagePath = HAUtilities.getIconPath(iconID);
			
		}
		
		return imagePath;
	}
	
	@Override
	public Component getListCardRendererComponent(Device device)
	{
		this.myDevice = device;
		deviceName.setText(device.getName());		
		
		stateVisualizer = new DeviceStateVisualizer(device, false, false);
		
		init();
		reloadColors();
		
		URL imagePath = getFirstFeatureImagePath(device);
		image.loadImage(imagePath);
		
		return this;
	}

	private void setAllComponentsBackground(Color color)
	{
		setBackground(color);
		
		for(Component c : getComponents())
			if(c instanceof HAThemeListener)
				c.setBackground(color);
	}
	
	private void initMouseListener()
	{
		addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e)
			{
				new DeviceManagementFrame(myDevice, controller);
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
				setAllComponentsBackground(HAUtilities.changeColorBrightness(getBackground(), 20));
				
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
	public void paint(Graphics g)
	{
		super.paint(g);
		
		if(myDevice instanceof DeviceGroup)
		{
			URL url = HAUtilities.getIconPath("group");			
			
			if(url != null)
			{
				BufferedImage image;
				try {
					image = ImageIO.read(url);
					
					int xStartPosition = getWidth() - 30;
					int yStartPosition = 20;
					int height = 20;
					
					int ratio = image.getWidth() / image.getHeight();
					int width = ratio * height;
					
					g.drawImage(image, xStartPosition, yStartPosition, width, height, this);

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public void reloadColors()
	{		
		
		if(stateVisualizer != null)
			stateVisualizer.setBackground(HAUtilities.getBackgroundColor());
		setBackground(HAUtilities.getLightBackgroundColor());
		
		
		Color shadow = HAUtilities.getShadowColor();

		//setting shadow
		setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED, HAUtilities.getBackgroundColor(), HAUtilities.getLightBackgroundColor(), shadow, shadow));
		
		deviceName.reloadColors();
	}
}
