package it.homeautomation.view.navigationpanels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;


import it.homeautomation.controller.HouseAutomationController;
import it.homeautomation.hagui.HANavigationDrawerPanel;
import it.homeautomation.hagui.HAUtilities;
import it.homeautomation.model.command.Command;
import it.homeautomation.view.commandmanagement.CommandsExecutionLog;
import it.homeautomation.view.commandmanagement.FilterCommandPanel;
import it.homeautomation.view.commandmanagement.SelectCommandPanel;
import it.homeautomation.view.implementation.HAViewImplementation;
import it.homeautomation.view.interfaces.CommandCreationListener;

public class ExecuteCommandPanel extends HANavigationDrawerPanel implements CommandCreationListener
{
	private static final long serialVersionUID = 1L;
	private HouseAutomationController controller;
	private FilterCommandPanel filterCommandsPanel ;
	private SelectCommandPanel selectCommandPanel;
	
	private CommandsExecutionLog commandsLog = new CommandsExecutionLog();
			
	public ExecuteCommandPanel()
	{
		super("Execute Command");
		this.controller = HAViewImplementation.getSingleton().getController();
		
		init();		
		
	}

	private void init()
	{
		selectCommandPanel = new SelectCommandPanel(controller);
		filterCommandsPanel = new FilterCommandPanel(controller, selectCommandPanel);
		selectCommandPanel.addCommandListener(this);
		
		GridBagConstraints constraints = new GridBagConstraints();
		getContent().setLayout(new GridBagLayout());
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weighty = 1f;
		constraints.weightx = 1f;
		getContent().add(filterCommandsPanel, constraints);
		
		constraints.gridy ++;
		constraints.insets = new Insets(20, 0, 0, 0);
		getContent().add(selectCommandPanel, constraints); 
		
		constraints.gridy ++;
		constraints.weighty = 0.1f;
		constraints.insets.bottom = 0;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		getContent().add(HAUtilities.newDescription("Execution Log"), constraints);
		
		constraints.gridy ++;
		constraints.weighty = 0.3f;
		constraints.insets.top = 0;
		constraints.insets.bottom = 30;
		constraints.fill = GridBagConstraints.BOTH;
		getContent().add(commandsLog, constraints); 
		
		updateContent();
		reloadColors();
	}
	
	
	@Override
	public void reloadColors()
	{
		if(filterCommandsPanel != null)
			filterCommandsPanel.reloadColors();
		getContent().setBackground(HAUtilities.getBackgroundColor());
		setBackground(HAUtilities.getBackgroundColor());
	}

	@Override
	public void updateContent()
	{
		filterCommandsPanel.refreshRooms();
	}

	@Override
	public void commandListCreated(String description, List<Command<?>> commands, List<Object> valuesList)
	{
		commandsLog.startCommandExecution();
		
		controller.executeCommands(commands);
		
		commandsLog.executeCommand(description);
		commandsLog.endExecution();
	}

}
