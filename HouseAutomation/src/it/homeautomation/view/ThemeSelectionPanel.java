package it.homeautomation.view;


import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import it.homeautomation.hagui.HAButton;
import it.homeautomation.hagui.HALabel;
import it.homeautomation.hagui.HAPanel;
import it.homeautomation.hagui.HATools;

public class ThemeSelectionPanel extends HAPanel
{
	private static final long serialVersionUID = 1L;
	private WelcomeFrame frame;
	
	private final String selectLight = "Select light theme...", selectDark = "Select dark theme...";
	
	private HALabel text = new HALabel(selectLight, HEIGHT);
	private HAButton switchTheme = new HAButton("Switch Theme");
	
	private Color newBackgroundColor = new Color(0xeeeeee);
	private Color newForegroundColor = new Color(0x353535);
	private Color newPrimaryColor = new Color(0x289eff);
	private Color newPrimaryForegroundColor = new Color(0xeeeeee);
	
	private void init()
	{
		setBackground(newBackgroundColor);
		text.setForeground(newForegroundColor);
		switchTheme.setCustomColors(newPrimaryColor, newPrimaryForegroundColor);
		
		switchTheme.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{				
				reloadColors();
				
				if(!HATools.getBackgroundColor().equals(newBackgroundColor)) {
					text.setText(selectDark);
					HATools.setTheme(newBackgroundColor, newForegroundColor, newPrimaryColor, newPrimaryForegroundColor);
				}
				else 
				{
					text.setText(selectLight);
					HATools.resetTheme();
				}
				
				frame.updateTheme();
			}
		});
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weightx = 1;
		constraints.insets = new Insets(10, 20, 10, 20);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		add(text, constraints);
		
		constraints.gridx = 2;
		add(switchTheme, constraints);
	}
	
	public ThemeSelectionPanel(WelcomeFrame frame)
	{
		this.frame = frame;
		setLayout(new GridBagLayout());
		init();
	}

	
	@Override
	public void reloadColors()
	{
		setBackground(HATools.getBackgroundColor());
		text.reloadColors();
		switchTheme.reloadColors();
		switchTheme.reloadColors();		
	}

}
