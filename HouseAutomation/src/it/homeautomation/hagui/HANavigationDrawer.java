package it.homeautomation.hagui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JPanel;
//import javax.swing.JSeparator;
import javax.swing.SwingConstants;

/**
 * <p>This class creates a Navigation Drawer. 
 * New buttons for user navigation can be dynamically added.
 * Class user will need to specify the content panel.
 * For each button there will be a corresponding panel which will be visualized in the content panel
 * when the button is pressed.
 * 
 * @author Nunzio D'Amore
 *
 */

public class HANavigationDrawer extends HAPanel
{
	private static final long serialVersionUID = 1L;
	
	private static final float TEXT_FONT_SIZE = 20f;
	
	private int toolButtonsPanelHeight = 250;
	
	// navigation button container
	private JPanel navigationButtonsPanel;	
	private JPanel contentPanel;
	private HALabel houseNameLabel;
	private HAFrame frame = null;
	private Map<HAButton, HANavigationDrawerPanel> buttonsPanels = new LinkedHashMap<>();
	
	public HANavigationDrawer(String houseName, int width, int height, HAFrame frame)
	{
		this.frame = frame;
		init(houseName, width, height);
	}
	
	public void setContentPanel(JPanel contentPanel)
	{
		this.contentPanel = contentPanel;
	}
	
	public void setButtonsPanelHeight(int height)
	{
		toolButtonsPanelHeight = height;
		updateUI();
	}
	
	private void commitPanelChange(HAButton button)
	{
		if(contentPanel != null)
		{
			HANavigationDrawerPanel panel = buttonsPanels.get(button);
			contentPanel.removeAll();
			contentPanel.add(panel);
			panel.updateContent();
			contentPanel.updateUI();
			
			buttonsPanels.keySet().forEach(b -> b.setEnabled(true));
			button.setEnabled(false);
		}
		
		if(frame != null)
			frame.bringFrameControllerToTop();
	}
	
	private void addNavigationActionListener(final HAButton button)
	{
		button.addActionListener(e -> commitPanelChange(button));
	}
	
	private void alignButtonTextLeft(HAButton button)
	{
		button.setHorizontalAlignment(SwingConstants.LEFT);
		String textMargin = HAUtilities.spaceMarginString(10) + button.getText();
		button.setText(textMargin);
	}	
	
	/**
	 * Creates a new button and updates the buttons panel.
	 * @param name specifies button name
	 * @param panel the corresponding panel
	 * 
	 * @throws NullPointerException if the name is null or empty
	 */
	public void addButton(String name, HANavigationDrawerPanel panel)
	{
		if(name == null || name.equals(""))
			throw new NullPointerException("The name can not be empty.");
		
		
		HAButton button = new HAButton(name);
		button.setCustomColors(getBackground(), HAUtilities.getForegroundColor());
		button.setDisabledColor(HAUtilities.getPrimaryColor(), HAUtilities.getPrimaryForegroundColor());
		alignButtonTextLeft(button);
		buttonsPanels.put(button, panel);
		
		if(buttonsPanels.size() == 1)
			commitPanelChange(button);
		
		addNavigationActionListener(button);
		updateButtonsPanel();
	}
	
	public void initToolButtonsPanel()
	{			
		navigationButtonsPanel = new JPanel();		
		navigationButtonsPanel.setLayout(new GridBagLayout());
	}
	
	private void init(String houseName, int width, int height)
	{
		this.setLayout(null);
		this.setLocation(0,0);		

		
		houseNameLabel = new HALabel(houseName, SwingConstants.CENTER);
		houseNameLabel.setOpaque(true);		
		houseNameLabel.setSize(width, 100);
		houseNameLabel.setLocation(0, 100);
		houseNameLabel.setFontSize(TEXT_FONT_SIZE);
		

		initToolButtonsPanel();
		
		this.add(houseNameLabel);
		this.add(navigationButtonsPanel);	
		reloadColors();
	}
		
//	private void addNavigationPanelSeparator(GridBagConstraints constrains)
//	{
//		constrains.weighty = 0f;
//		constrains.fill = GridBagConstraints.HORIZONTAL;
//		navigationButtonsPanel.add(new HASeparator(JSeparator.HORIZONTAL, 1), constrains);
//		constrains.gridy ++;
//	}
//	
	private void addNavigationPanelButton(HAButton button, GridBagConstraints constrains)
	{
		constrains.weighty = 1f;
		constrains.fill = GridBagConstraints.BOTH;
		navigationButtonsPanel.add(button, constrains);
		constrains.gridy ++;
	}
	
	private void updateButtonsPanel()
	{
		navigationButtonsPanel.removeAll();
		
		GridBagConstraints constrains = new GridBagConstraints();		
		constrains.weightx = 1.0f;
		constrains.gridx = 0;
		constrains.gridy = 0;

		for(HAButton button : buttonsPanels.keySet())
		{
			//addNavigationPanelSeparator(constrains);	
			addNavigationPanelButton(button, constrains);
		}
		
		//addNavigationPanelSeparator(constrains);

		navigationButtonsPanel.updateUI();
	}
	
	private void resizeButtonsPanel()
	{
		int navigationButtonsPanelHeight = getHeight()/2 - navigationButtonsPanel.getHeight()/2;
		navigationButtonsPanel.setSize(new Dimension(getWidth(), toolButtonsPanelHeight));
		navigationButtonsPanel.setLocation(0,navigationButtonsPanelHeight);
	}
	
	@Override
	public void setSize(Dimension d)
	{
		super.setSize(d);
		resizeButtonsPanel();
		houseNameLabel.setSize(d.width, 100);
	}

	@Override
	public void reloadColors()
	{
		Color nameLabelColor = HAUtilities
				.changeColorBrightness(HAUtilities
											.getDarkBackgroundColor(),-10);
		
		setBackground(HAUtilities.getDarkBackgroundColor());
		houseNameLabel.setBackground(nameLabelColor);
		houseNameLabel.setForeground(HAUtilities.getForegroundColor());		
		navigationButtonsPanel.setBackground(HAUtilities.getBackgroundColor());
		
		buttonsPanels.values().stream().forEach(HANavigationDrawerPanel::reloadColors);
	}
	
}
