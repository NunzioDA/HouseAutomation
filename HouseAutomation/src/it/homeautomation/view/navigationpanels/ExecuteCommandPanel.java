package it.homeautomation.view.navigationpanels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import it.homeautomation.controller.HouseAutomationController;
import it.homeautomation.hagui.HANavigationDrawerPanel;
import it.homeautomation.hagui.HAUtilities;
import it.homeautomation.model.command.Command;
import it.homeautomation.view.commandmanagement.CreateCommandPanel;
import it.homeautomation.view.commandmanagement.SelectCommandPanel;
import it.homeautomation.view.interfaces.CommandCreationListener;

public class ExecuteCommandPanel extends HANavigationDrawerPanel implements CommandCreationListener
{
	private static final long serialVersionUID = 1L;
	private HouseAutomationController controller;
	private CreateCommandPanel createCommand ;
	private SelectCommandPanel selectCommandPanel;
	
	
	public ExecuteCommandPanel(HouseAutomationController controller)
	{
		super("Execute Command");
		this.controller = controller;
		
		init();		
		
	}

	private void init()
	{
		selectCommandPanel = new SelectCommandPanel(controller);
		createCommand = new CreateCommandPanel(controller, selectCommandPanel);
		selectCommandPanel.addCommandListener(this);
		
		GridBagConstraints constraints = new GridBagConstraints();
		getContent().setLayout(new GridBagLayout());
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weighty = 1f;
		constraints.weightx = 1f;
		
		getContent().add(createCommand, constraints);
		
		constraints.gridy ++;
		constraints.insets = new Insets(0, 0, 30, 0);
		getContent().add(selectCommandPanel, constraints); 
		
		updateContent();
		reloadColors();
	}
	
	
	@Override
	public void reloadColors()
	{
		if(createCommand != null)
			createCommand.reloadColors();
		getContent().setBackground(HAUtilities.getBackgroundColor());
		setBackground(HAUtilities.getBackgroundColor());
	}

	@Override
	public void updateContent()
	{
		createCommand.refreshRooms();
	}

	@Override
	public void commandListCreated(String description, List<Command<?>> commands, List<Object> valuesList)
	{
		System.out.println(commands);
		commands.stream().forEach(Command::execute);
	}

}
