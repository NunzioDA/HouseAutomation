package it.homeautomation.view.navigationpanels;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Optional;

import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import it.homeautomation.controller.HouseAutomationController;
import it.homeautomation.hagui.HAButton;
import it.homeautomation.hagui.HALabel;
import it.homeautomation.hagui.HAList;
import it.homeautomation.hagui.HANavigationDrawerPanel;
import it.homeautomation.hagui.HAScrollPane;
import it.homeautomation.hagui.HATextField;
import it.homeautomation.hagui.HAUtilities;
import it.homeautomation.model.Routine;
import it.homeautomation.model.Routine.RoutineEntry;
import it.homeautomation.model.command.Command;
import it.homeautomation.view.commandmanagement.FilterCommandPanel;
import it.homeautomation.view.commandmanagement.SelectCommandPanel;
import it.homeautomation.view.interfaces.CommandCreationListener;

public class RoutineCreationPanel extends HANavigationDrawerPanel implements CommandCreationListener
{
	private static final long serialVersionUID = 1L;
	private static final String MISSING_NAME = "Routine name required...";
	private static final String MISSING_COMMANDS = "No commands have been added...";
	private static final String EXISTING_ROUTINE = "A Routine with this name already exists...";
	
	private HouseAutomationController controller;
	private FilterCommandPanel filterCommandsPanel;
	private SelectCommandPanel selectCommandPanel;
	private HATextField routineName = new HATextField(20);
	private JPanel routinePanel = new JPanel();
	private JPanel commandsManagementPanel = new JPanel();
	
	private Routine currentRoutine;
	private HAButton createRoutineButton = new HAButton("Create routine");
	
	private DefaultListModel<String> commandsDescription = new DefaultListModel<>();
	private HAList<String> commandsDescriptionList = new HAList<>(commandsDescription);
	private HAScrollPane commandsDescScroll = new HAScrollPane(commandsDescriptionList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	private HAButton removeCommand = new HAButton("Remove command");
	
	private HALabel error = new HALabel("", SwingConstants.RIGHT);
	
	public RoutineCreationPanel(HouseAutomationController controller)
	{
		super("Create routine");
		this.controller = controller;
		currentRoutine = controller.getRoutineInstance();
		init();
	}
	
	private void resetPanel()
	{
		commandsDescription.removeAllElements();
		currentRoutine = controller.getRoutineInstance();
	}
	
	private void initCreateRoutineButton()
	{
		createRoutineButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String routineNameStr = routineName.getText();
				
				if(!routineNameStr.isEmpty())
				{
					currentRoutine.setName(routineNameStr);
					
					if(currentRoutine.getCommands().size() !=0)
					{
						boolean success = controller.addRoutine(currentRoutine);
						
						if(success)
						{
							resetPanel();
						}
						else error.setText(EXISTING_ROUTINE);
					}
					else error.setText(MISSING_COMMANDS);
				}
				else error.setText(MISSING_NAME);
			}
		});
	}
	
	private void initRoutineListManagement()
	{
		initRemoveButton();
		commandsDescriptionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);		
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
					
					Optional<RoutineEntry> entry =
					currentRoutine
					.getCommands()
					.stream()
					.filter(f->f
							.getDescription()
							.equals(selectedD))
					.findAny();
					
					if(!entry.isEmpty())
					{
						currentRoutine.remove(entry.get());
						commandsDescription.remove(index);
					}
				}
			}
		});
	}
	
	private void initRoutinePanel() 
	{
		HALabel label = HAUtilities.newDescription("Insert routine name:");
		//Routine panel init
		GridLayout gl = new GridLayout(1,3);
		gl.setHgap(20);
		routinePanel.setLayout(gl);
		routinePanel.add(label);
		routinePanel.add(routineName);
		routinePanel.add(createRoutineButton);
	}
	
	private void initCommandsManagementPanel() 
	{
		GridLayout gl = new GridLayout(1,3);
		gl.setHgap(25);
		commandsManagementPanel.setLayout(gl);
		commandsDescriptionList.setVisibleRowCount(3);
		commandsManagementPanel.add(commandsDescScroll);
		commandsManagementPanel.add(removeCommand);
	}
	
	private  void initComponents()
	{
		selectCommandPanel = new SelectCommandPanel(controller);
		filterCommandsPanel = new FilterCommandPanel(controller, selectCommandPanel);
		selectCommandPanel.addCommandListener(this);
		
		initRoutineListManagement();
		initCreateRoutineButton();
		initRoutinePanel();
		initCommandsManagementPanel();
		
		error.setForeground(Color.red);
	}
	
	private void init()
	{
		initComponents();
		
		GridBagConstraints constraints = new GridBagConstraints();
		getContent().setLayout(new GridBagLayout());
		
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weightx = 1f;		
		constraints.weighty = 0.3f;
		constraints.insets = new Insets(0, 0, 20, 0);
		constraints.anchor = GridBagConstraints.SOUTHWEST;
		constraints.fill = GridBagConstraints.BOTH;		
		getContent().add(routinePanel, constraints);
		
		constraints.gridy ++;
		constraints.weighty = 0f;
		constraints.insets.bottom = 0;
		constraints.fill = GridBagConstraints.HORIZONTAL;		
		getContent().add(error, constraints);		
		
		constraints.gridy ++;
		constraints.weighty = 0.7f;
		constraints.insets.top = 20;
		constraints.insets.bottom = 20;
		constraints.fill = GridBagConstraints.BOTH;	
		getContent().add(filterCommandsPanel, constraints);
		
		constraints.gridy ++;
		constraints.insets.bottom = 0;
		getContent().add(selectCommandPanel, constraints); 		

		constraints.gridy ++;
		constraints.weighty = 0f;
		constraints.insets.bottom = 5;
		constraints.fill = GridBagConstraints.HORIZONTAL;		
		getContent().add(HAUtilities.newDescription("Confirmed commands"), constraints);	
		
		constraints.gridy ++;
		constraints.weighty = 0.5f;
		constraints.insets.top = 0;
		constraints.insets.bottom = 30;
		constraints.fill = GridBagConstraints.BOTH;
		getContent().add(commandsManagementPanel, constraints);
		
		
		updateContent();
		reloadColors();
	}
	
	
	@Override
	public void reloadColors()
	{
		if(filterCommandsPanel != null)
			filterCommandsPanel.reloadColors();
		
		routineName.reloadColors();
		getContent().setBackground(HAUtilities.getBackgroundColor());
		setBackground(HAUtilities.getBackgroundColor());
		routinePanel.setBackground(getBackground());
		commandsManagementPanel.setBackground(HAUtilities.getBackgroundColor());
	}

	@Override
	public void updateContent()
	{
		filterCommandsPanel.refreshRooms();
	}

	@Override
	public void commandListCreated(String description, List<Command<?>> commands, List<Object> valuesList)
	{
		Object device = filterCommandsPanel.getSelectedDevice();
		Object room = filterCommandsPanel.getSelectedRoom();
		Object category = filterCommandsPanel.getSelectedCategory();	
		
		if(device!= null && room != null && category != null)
		{
			String deviceS = device.toString();
			String roomS = room.toString();
			String categoryS = category.toString();
			
			currentRoutine.addCommands(description, commands, deviceS, categoryS, roomS, valuesList);		
			commandsDescription.addElement(description);
		}
	}


}
