package it.homeautomation.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import it.homeautomation.controller.HouseAutomationController;
import it.homeautomation.hagui.HAFrame;
import it.homeautomation.hagui.HAUtilities;
import it.homeautomation.model.Routine;
import it.homeautomation.model.command.Command;
import it.homeautomation.view.commandmanagement.CreateCommandPanel;
import it.homeautomation.view.commandmanagement.SelectCommandPanel;
import it.homeautomation.view.interfaces.CommandCreationListener;
import it.homeautomation.view.navigationpanels.ManageRoutinePanel;

public class AddRoutineCommandFrame extends HAFrame implements CommandCreationListener
{

	private static final long serialVersionUID = 1L;
	
	private HouseAutomationController controller;
	private CreateCommandPanel createCommand ;
	private SelectCommandPanel selectCommandPanel;
	private Routine routine;
	private ManageRoutinePanel manageRoutine;
	public AddRoutineCommandFrame(Routine routine, ManageRoutinePanel manageRoutine, HouseAutomationController controller, int width, int height)
	{
		super("Add command to " + routine.getName(), width, height);
		this.controller = controller;
		this.routine = routine;
		this.manageRoutine = manageRoutine;
		init();
	}
	private void init()
	{
		selectCommandPanel = new SelectCommandPanel(controller);
		createCommand = new CreateCommandPanel(controller, selectCommandPanel);
		selectCommandPanel.addCommandListener(this);
		createCommand.refreshRooms();
		
		
		
		GridBagConstraints constraints = new GridBagConstraints();
		setContentLayout(new GridBagLayout());
		constraints.insets = new Insets(20, 20, 20, 20);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weighty = 1f;
		constraints.weightx = 1f;
		
		addComponent(createCommand, constraints);
		
		constraints.gridy ++;
		//constraints.insets = new Insets(0, 0, 30, 0);
		addComponent(selectCommandPanel, constraints);		

		reloadColors();
	}
	@Override
	public void reloadColors()
	{
		if(createCommand != null)
			createCommand.reloadColors();
		setBackground(HAUtilities.getBackgroundColor());
	}

	@Override
	public void resizeContent()
	{
		// TODO Auto-generated method stub
		
	}
	@Override
	public void commandListCreated(String description, List<Command<?>> commands, List<Object> values)
	{
		Object device = createCommand.getSelectedDevice();
		Object room = createCommand.getSelectedRoom();
		Object category = createCommand.getSelectedCategory();	
		
		if(device!= null && room != null && category != null)
		{
			String deviceS = device.toString();
			String roomS = room.toString();
			String categoryS = category.toString();
			
			routine.addCommands(description, commands, deviceS, categoryS, roomS, values);
			manageRoutine.updateContent();
		}
		
		setVisible(false);
		dispose();
	}

}
