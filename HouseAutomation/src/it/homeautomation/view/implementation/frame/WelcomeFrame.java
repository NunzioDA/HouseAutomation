package it.homeautomation.view.implementation.frame;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingConstants;


import it.homeautomation.controller.HouseAutomationController;
import it.homeautomation.hagui.HAButton;
import it.homeautomation.hagui.HAFrame;
import it.homeautomation.hagui.HALabel;
import it.homeautomation.hagui.HATextField;
import it.homeautomation.hagui.HAUtilities;
import it.homeautomation.view.implementation.HAViewImplementation;
import it.homeautomation.view.implementation.ThemeSelectionPanel;

public class WelcomeFrame extends HAFrame
{
	private static final long serialVersionUID = 1L;

	private final float TITLE_SIZE = 30f;
	
	private final String titleText = "<html>HI,<br/>WE'RE ALMOST THERE!</html>";
	private final String descriptionText = "<html>Select your favorite theme and<br/> insert your house name to start.</html>";
	
	private HALabel title = new HALabel(titleText, SwingConstants.LEFT);
	private HALabel error = new HALabel("", SwingConstants.LEFT);
	private HALabel description = new HALabel(descriptionText, SwingConstants.RIGHT);
	private HALabel houseNameDesc = HAUtilities.newDescription("House Name:");
	private HATextField houseName = new HATextField(30);	
	private HAButton confirm = new HAButton("Confirm");
	private HouseAutomationController controller;
	
	public static WelcomeFrame getAdaptedWelcomFrame(String titleText)
	{		
		return new WelcomeFrame(titleText, 500, 500);
	}
	
	public WelcomeFrame(String title, int width, int height)
	{
		super(title, width, height);
		init();
		setVisible(true);
		controller = HAViewImplementation.getSingleton().getController();
	}
	
	private void initConfirmButton()
	{
		confirm.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(!houseName.getText().isEmpty()) {
					controller.startMainScreen(houseName.getText());
					WelcomeFrame.this.setVisible(false); 
					WelcomeFrame.this.dispose();
				}
				else error.setText("House name missing.");	
			}
		});
	}
	
	private void init()
	{	
		setResizable(false);
		
		setContentLayout(new GridBagLayout());
		title.setFont(HAUtilities.getRegularFont().deriveFont(TITLE_SIZE));
		GridBagConstraints constrains = new GridBagConstraints();
		error.setForeground(Color.red);
		
		constrains.anchor = GridBagConstraints.WEST;
		constrains.fill = GridBagConstraints.BOTH;
		constrains.insets = new Insets(40, 20, 20, 20);
		constrains.weightx = 1;
		constrains.weighty = 1;
		constrains.gridx = 0;
		constrains.gridy = 0;
		constrains.gridwidth = 2;
		addComponent(title, constrains);
		
		constrains.gridwidth = 1;		
		constrains.gridy ++;
		addComponent(error, constrains);
		
		constrains.gridx = 1;
		constrains.anchor = GridBagConstraints.EAST;
		addComponent(description, constrains);
		
		constrains.gridx = 0;
		constrains.gridy ++;
		constrains.gridwidth = 2;
		ThemeSelectionPanel temePanel = new ThemeSelectionPanel(this);
		addComponent(temePanel, constrains);
		
		constrains.gridy ++;
		constrains.gridwidth = 1;
		constrains.anchor = GridBagConstraints.WEST;
		constrains.insets = new Insets(0, 20, 0, 0);
		addComponent(houseNameDesc, constrains);
		
		constrains.gridy ++;
		constrains.gridwidth = 2;
		constrains.ipady = 10;
		constrains.fill = GridBagConstraints.BOTH;
		constrains.insets = new Insets(0, 20, 20, 20);
		addComponent(houseName, constrains);
		
		constrains.gridy ++;
		constrains.ipady = 30;
		constrains.fill = GridBagConstraints.BOTH;
		addComponent(confirm, constrains);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		initConfirmButton();
	}
	


	@Override
	public void resizeContent(){}

	@Override
	public void reloadColors()
	{
		if(title != null)
			title.reloadColors();
		
		if(description != null)
			description.reloadColors();
		
		if(houseNameDesc != null)
			houseNameDesc.reloadColors();
		
		if(houseName != null)
			houseName.reloadColors();
		
		if(confirm != null)
			confirm.reloadColors();
	}

}
