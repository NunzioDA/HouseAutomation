package it.homeautomation.view.implementation.frame;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import it.homeautomation.controller.HouseAutomationController;
import it.homeautomation.model.Device;
import it.homeautomation.model.Filter;
import it.homeautomation.model.command.Command;
import it.homeautomation.model.features.DeviceFeature;
import it.homeautomation.view.implementation.HAViewImplementation;
import it.homeautomation.view.implementation.commandpanels.SelectCommandPanel;
import it.homeautomation.view.interfaces.CommandCreationListener;

public class DeviceCommandExecutionFrame extends DisableMainFrame implements CommandCreationListener
{

	private static final long serialVersionUID = 1L;
	
	private SelectCommandPanel commandPanel;
	private HouseAutomationController controller ;
	private List<Command<?>> commands;
	private Device device;
	
	public DeviceCommandExecutionFrame(int width, int height, Device device, DeviceFeature deviceFeature)
	{
		super(deviceFeature.toString() + " feature commands", width, height);
		
		controller = HAViewImplementation.getSingleton().getController();
		
		commands = new ArrayList<>();
		commands.addAll(deviceFeature.getCommands());
		
		this.device = device;
		
		init();
		reloadColors();
		
		setResizable(false);
		setVisible(true);
	}
	
	private void init()
	{
		commandPanel = new SelectCommandPanel(controller);
		commandPanel.addCommandListener(this);
			
		Filter filter = Filter.selectAllFilter();
		filter.setDevice(device);
		
		commandPanel.refreshCommands(null, commands, filter);
		
		setContentLayout(new GridBagLayout());
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weighty = 0.1f;
		constraints.weightx = 1f;
		constraints.insets = new Insets(50, 20, 30, 20);
		
		addComponent(commandPanel, constraints);
	}
	
	@Override
	public void reloadColors()
	{

	}


	@Override
	public void resizeContent()
	{
		
	}

	@Override
	public void commandListCreated(String groupDescription, List<Command<?>> command, List<Object> values)
	{
		controller.executeCommands(command);
		setVisible(false);
		dispose();
	}	

}
