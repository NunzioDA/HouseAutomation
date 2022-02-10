package it.homeautomation.view.navigationpanels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Optional;

import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import it.homeautomation.controller.HouseAutomationController;
import it.homeautomation.hagui.HAButton;
import it.homeautomation.hagui.HAList;
import it.homeautomation.hagui.HANavigationDrawerPanel;
import it.homeautomation.hagui.HAScrollPane;
import it.homeautomation.hagui.HAUtilities;
import it.homeautomation.model.Routine;
import it.homeautomation.model.Routine.RoutineEntry;
import it.homeautomation.view.commandmanagement.CommandsExecutionLog;
import it.homeautomation.view.implementation.AddRoutineCommandFrame;
import it.homeautomation.view.implementation.HAViewImplementation;

public class ManageRoutinePanel extends HANavigationDrawerPanel
{
	private static final long serialVersionUID = 1L;
	
	private HAList<Routine> routinesList = new HAList<>();
	private HAScrollPane routinesScroll = new HAScrollPane(routinesList, HAScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, HAScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	
	private HAList<String> commandsDescriptionList = new HAList<>();
	private HAScrollPane routineCommandsScroll = new HAScrollPane(commandsDescriptionList, HAScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, HAScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	
	private HouseAutomationController controller;
	private HAButton execute = new HAButton("Execute");
	private HAButton deleteRoutine = new HAButton("Delete Routine");
	private HAButton addCommand = new HAButton("Add Command");
	private HAButton removeCommand = new HAButton("Remove Command");
	private JPanel leftButtonsContainer = new JPanel();
	private JPanel rightButtonsContainer = new JPanel();
	
	private CommandsExecutionLog commandsLog = new CommandsExecutionLog();
	
	public ManageRoutinePanel()
	{
		super("Manage Routines");
		this.controller = HAViewImplementation.getSingleton().getController();
		init();
	}
	
	private Routine getSelectedRoutine()
	{
		Routine routine = null;
		int index = routinesList.getSelectedIndex();

		if(index >= 0)
		{
			routine = routinesList.getDefaultModel().get(index);
		}		
		
		return routine;
	}
	
	private void initExecuteButton()
	{
		execute.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Routine selectedRoutine = getSelectedRoutine();
				if(selectedRoutine != null)
				{
					commandsLog.startRoutineExecution( selectedRoutine.getName());
					
					selectedRoutine.execute();					
					
					
					selectedRoutine
					.getCommands()
					.stream()
					.forEach(c->commandsLog
							.executeCommand(c
									.getDescription()));
					
					commandsLog.endExecution();
					
				}
			}
		});
	}
	
	private void initRemoveButton()
	{
		removeCommand.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				int index = commandsDescriptionList.getSelectedIndex();
				
				if(index >= 0)
				{
					String selectedD = commandsDescriptionList.getDefaultModel().get(index);
					
					Routine selectedRoutine = getSelectedRoutine();
					
					if(selectedRoutine != null)
					{
						Optional<RoutineEntry> entry =
								getSelectedRoutine()
								.getCommands()
								.stream()
								.filter(f->f
										.getDescription()
										.equals(selectedD))
								.findAny();
						if(!entry.isEmpty())
						{
							selectedRoutine.remove(entry.get());
							commandsDescriptionList.getDefaultModel().remove(index);
						}
					}
				}
			}
		});
	}
	
	private void initRoutineListListener()
	{
		routinesList.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				int index = routinesList.getSelectedIndex();
				
				if(index >= 0)
				{
					commandsDescriptionList.getDefaultModel().removeAllElements();
					
					Routine selectedRoutine = routinesList.getDefaultModel().get(index);
					
					selectedRoutine
					.getCommands()
					.stream()
					.forEach(r->commandsDescriptionList.getDefaultModel()
							.addElement(r
									.getDescription()));
				}
			}
		});
	}
	
	private void initAddCommandButton()
	{
		addCommand.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Routine selectedRoutine = getSelectedRoutine();
				
				if(selectedRoutine != null)
				{
					System.out.println(selectedRoutine);
					new AddRoutineCommandFrame(selectedRoutine, ManageRoutinePanel.this, controller, 500, 500);
				}
			}
		});
	}
	
	private void initDeleteRoutineButton()
	{
		deleteRoutine.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Routine selectedRoutine = getSelectedRoutine();
				
				if(selectedRoutine != null)
				{
					controller.deleteRoutine(selectedRoutine);
					updateContent();
				}
			}
		});
	}
	
	private void initButtonContainer(JPanel panel)
	{
		GridLayout gl = new GridLayout(1, 2);
		gl.setHgap(15);
		panel.setLayout(gl);
	}
	
	private void init()
	{
		initRemoveButton();
		initRoutineListListener();
		initExecuteButton();
		initAddCommandButton();
		initDeleteRoutineButton();
		
		getContent().setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;		
		
		constraints.weighty = 0.3f;
		constraints.weightx = 1f;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		getContent().add(HAUtilities.newDescription("Routines"), constraints);
		
		constraints.gridy ++;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 1f;
		getContent().add(routinesScroll, constraints);
		
		constraints.gridy ++;
		constraints.weighty = 0.3f;
		constraints.insets = new Insets(15, 0, 0, 0);
		initButtonContainer(leftButtonsContainer);
		leftButtonsContainer.add(execute);
		leftButtonsContainer.add(deleteRoutine);		
		getContent().add(leftButtonsContainer, constraints);
		
		
		constraints.gridx = 1;
		constraints.gridy = 0;		
		constraints.weighty = 0.3f;
		constraints.insets.top = 0;
		constraints.insets.left = 15;
		constraints.fill = GridBagConstraints.HORIZONTAL;		
		getContent().add(HAUtilities.newDescription("Selected Routine"), constraints);
		
		constraints.gridy ++;
		constraints.fill = GridBagConstraints.BOTH;		
		constraints.weighty = 1f;
		getContent().add(routineCommandsScroll, constraints);		
		
		constraints.gridy ++;
		constraints.insets.top = 15;
		constraints.weighty = 0.3f;
		initButtonContainer(rightButtonsContainer);
		rightButtonsContainer.add(removeCommand);
		rightButtonsContainer.add(addCommand);		
		
		getContent().add(rightButtonsContainer, constraints);
		
		constraints.gridx = 0;
		constraints.gridy ++;
		constraints.insets.left = 0;
		constraints.gridwidth = 2;
		constraints.weighty = 0.3f;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		getContent().add(HAUtilities.newDescription("Execution Log"), constraints);
		
		constraints.gridy ++;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets.top = 0;
		constraints.insets.bottom = 30;
		getContent().add(commandsLog, constraints);

		reloadColors();
	}
	
	@Override
	public void reloadColors()
	{
		getContent().setBackground(HAUtilities.getBackgroundColor());
		setBackground(HAUtilities.getBackgroundColor());
		leftButtonsContainer.setBackground(getBackground());
		rightButtonsContainer.setBackground(getBackground());
	}

	@Override
	public void updateContent()
	{		
		routinesList.getDefaultModel().removeAllElements();
		routinesList.getDefaultModel().addAll(controller.getRoutines());
		commandsDescriptionList.getDefaultModel().removeAllElements();
	}

}
