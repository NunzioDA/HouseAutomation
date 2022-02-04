package it.homeautomation.view.navigationpanels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Optional;

import javax.swing.DefaultListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import it.homeautomation.controller.HouseAutomationController;
import it.homeautomation.hagui.HAButton;
import it.homeautomation.hagui.HAList;
import it.homeautomation.hagui.HANavigationDrawerPanel;
import it.homeautomation.hagui.HAScrollPane;
import it.homeautomation.hagui.HATools;
import it.homeautomation.model.Routine;
import it.homeautomation.model.Routine.RoutineEntry;

public class ManageRoutinePanel extends HANavigationDrawerPanel
{
	private static final long serialVersionUID = 1L;
	
	private DefaultListModel<Routine> routinesModel = new DefaultListModel<>();
	private HAList<Routine> routinesList = new HAList<>(routinesModel);
	private HAScrollPane routinesScroll = new HAScrollPane(routinesList, HAScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, HAScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	
	private DefaultListModel<String> commandsDescription = new DefaultListModel<>();
	private HAList<String> commandsDescriptionList = new HAList<>(commandsDescription);
	private HAScrollPane routineCommandsScroll = new HAScrollPane(commandsDescriptionList, HAScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, HAScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	
	private HouseAutomationController controller;
	private HAButton execute = new HAButton("Execute");	
	private HAButton removeCommand = new HAButton("Remove Command");
	
	private DefaultListModel<String> commandsLogModel = new DefaultListModel<>();
	private HAList<String> commandsLog = new HAList<>(commandsLogModel);
	private HAScrollPane commandsLogScrollPane = new HAScrollPane(commandsLog);
	
	public ManageRoutinePanel(HouseAutomationController controller)
	{
		super("Manage Routines");
		this.controller = controller;
		init();
	}
	
	private Routine getSelectedRoutine()
	{
		Routine routine = null;
		int index = routinesList.getSelectedIndex();
		
		if(index >= 0)
		{
			routine = routinesModel.get(index);
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
					commandsLogModel.addElement("---------> Executing [ "+ selectedRoutine.getName() + " ] routine");
					
					selectedRoutine.execute();					
					
					
					selectedRoutine
					.getCommands()
					.stream()
					.forEach(c->commandsLogModel
							.addElement(c
									.getDescription()));
					
					commandsLogModel.addElement("---------> Done.");
					
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
					String selectedD = commandsDescription.get(index);
					
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
							commandsDescription.remove(index);
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
					commandsDescription.removeAllElements();
					
					Routine selectedRoutine = routinesModel.get(index);
					
					selectedRoutine
					.getCommands()
					.stream()
					.forEach(r->commandsDescription
							.addElement(r
									.getDescription()));
				}
			}
		});
	}
	
	private void init()
	{
		initRemoveButton();
		initRoutineListListener();
		initExecuteButton();
		
		getContent().setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;		
		
		constraints.weighty = 0.3f;
		constraints.weightx = 1f;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		getContent().add(HATools.newDescription("Routines"), constraints);
		
		constraints.gridy ++;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 1f;
		getContent().add(routinesScroll, constraints);
		
		constraints.gridy ++;
		constraints.insets = new Insets(20, 0, 0, 0);
		getContent().add(execute, constraints);
		
		
		constraints.gridx = 1;
		constraints.gridy = 0;		
		constraints.weighty = 0.3f;
		constraints.insets.top = 0;
		constraints.insets.left = 30;
		constraints.fill = GridBagConstraints.HORIZONTAL;		
		getContent().add(HATools.newDescription("Selected Routine"), constraints);
		
		constraints.gridy ++;
		constraints.fill = GridBagConstraints.BOTH;		
		constraints.weighty = 1f;
		getContent().add(routineCommandsScroll, constraints);		
		
		constraints.gridy ++;
		constraints.insets.top = 20;
		getContent().add(removeCommand, constraints);
		
		constraints.gridx = 0;
		constraints.gridy ++;
		constraints.insets.left = 0;
		constraints.insets.top = 0;
		constraints.gridwidth = 2;
		constraints.weighty = 0.3f;
		constraints.insets.top = 20;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		getContent().add(HATools.newDescription("Execution Log"), constraints);
		
		constraints.gridy ++;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets.top = 0;
		constraints.insets.bottom = 30;
		getContent().add(commandsLogScrollPane, constraints);

		reloadColors();
	}
	
	@Override
	public void reloadColors()
	{
		getContent().setBackground(HATools.getBackgroundColor());
		setBackground(HATools.getBackgroundColor());
	}

	@Override
	public void updateContent()
	{
		routinesModel.removeAllElements();
		routinesModel.addAll(controller.getRoutines());
		commandsDescription.removeAllElements();
	}

}
