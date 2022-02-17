package it.homeautomation.view.implementation.navigationpanels;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Optional;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;

import it.homeautomation.controller.HouseAutomationController;
import it.homeautomation.hagui.HAButton;
import it.homeautomation.hagui.HALabel;
import it.homeautomation.hagui.HAList;
import it.homeautomation.hagui.HANavigationDrawerPanel;
import it.homeautomation.hagui.HAScrollPane;
import it.homeautomation.hagui.HATextField;
import it.homeautomation.hagui.HAUtilities;
import it.homeautomation.model.Filter;
import it.homeautomation.model.Routine;
import it.homeautomation.model.Routine.RoutineEntry;
import it.homeautomation.model.command.Command;
import it.homeautomation.view.CommandCreationListener;
import it.homeautomation.view.implementation.HAViewImplementation;
import it.homeautomation.view.implementation.commandpanels.FilterCommandPanel;
import it.homeautomation.view.implementation.commandpanels.SelectCommandPanel;

public class RoutineCreationPanel extends HANavigationDrawerPanel implements CommandCreationListener
{
	private static final long serialVersionUID = 1L;
	private static final String MISSING_NAME = "<html>Routine name required...</html>";
	private static final String MISSING_COMMANDS = "<html>No commands have been added...</html>";
	
	private HouseAutomationController controller;
	private FilterCommandPanel filterCommandsPanel;
	private SelectCommandPanel selectCommandPanel;
	private HATextField routineName = new HATextField(200);
	private JPanel routinePanel = new JPanel();
	private JPanel commandsManagementPanel = new JPanel();
	
	private Routine currentRoutine;
	private HAButton createRoutineButton = new HAButton("Create routine");
	
	private HAList<String> commandsDescriptionList = new HAList<>();
	private HAScrollPane commandsDescScroll = new HAScrollPane(commandsDescriptionList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	private HAButton removeCommand = new HAButton("Remove command");
	
	private HALabel error = new HALabel("", SwingConstants.RIGHT);
	
	public RoutineCreationPanel()
	{
		super("Create routine");
		this.controller = HAViewImplementation.getSingleton().getController();
		currentRoutine = controller.getRoutineInstance();
		init();
	}
	
	private void resetPanel()
	{
		commandsDescriptionList.getDefaultModel().removeAllElements();
		currentRoutine = controller.getRoutineInstance();
		routineName.setText("");
		error.setText("");
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
					String selectedD = commandsDescriptionList.getDefaultModel().get(index);
					
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
						commandsDescriptionList.getDefaultModel().remove(index);
					}
				}
			}
		});
	}
	
	private void initRoutinePanel() 
	{
		//Routine panel init
		GridBagLayout gl = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();
		routinePanel.setLayout(gl);
		
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weightx = 1f;		
		constraints.weighty = 0f;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.NORTH;		
		constraints.ipady = 15;
		routinePanel.add(routineName, constraints);
		
		constraints.gridy ++;
		constraints.ipady = 0;
		constraints.weighty = 1f;
		routinePanel.add(error, constraints);
		
		constraints.gridy ++;		
		constraints.ipady = 40;
		constraints.weighty = 0;
		constraints.fill = GridBagConstraints.BOTH;		
		routinePanel.add(createRoutineButton, constraints);
	}
	
	private void initCommandsManagementPanel() 
	{
		GridBagLayout gl = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();

		commandsManagementPanel.setLayout(gl);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weightx = 1f;		
		constraints.weighty = 1f;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.insets = new Insets(0, 0, 20, 0);
		commandsManagementPanel.add(commandsDescScroll, constraints);
		
		constraints.gridy ++;
		constraints.weighty = 0f;
		constraints.ipady = 40;
		constraints.insets.bottom = 0;
		commandsManagementPanel.add(removeCommand, constraints);
	}
	
	private void initComponents()
	{
		selectCommandPanel = new SelectCommandPanel(controller);
		filterCommandsPanel = new FilterCommandPanel(controller, selectCommandPanel);
		selectCommandPanel.addCommandListener(this);
		
		Color background = HAUtilities.getDarkBackgroundColor();
		removeCommand.setCustomColors(background, HAUtilities.getForegroundColor());
		removeCommand.setBorder(new MatteBorder(2,2,2,2, HAUtilities.getPrimaryColor()));
		
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
		constraints.insets = new Insets(20, 0, 0, 0);
		constraints.anchor = GridBagConstraints.SOUTHWEST;		
		constraints.weighty = 0.1f;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridwidth = 2;
		getContent().add(filterCommandsPanel, constraints);
		
		constraints.gridy ++;
		constraints.insets.bottom = 0;
		constraints.weighty = 1f;
		getContent().add(selectCommandPanel, constraints); 		

		constraints.gridy ++;
		constraints.weighty = 0f;
		constraints.insets.bottom = 5;
		constraints.gridwidth = 1;
		constraints.insets.right = 10;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		HALabel confirmComLab = HAUtilities.newDescription("Confirmed routine commands:");
		getContent().add(confirmComLab, constraints);	
		
		constraints.gridy ++;
		constraints.weighty = 1f;
		constraints.insets.top = 0;
		constraints.insets.bottom = 30;
		constraints.fill = GridBagConstraints.BOTH;		
		getContent().add(commandsManagementPanel, constraints);
		
		constraints.gridx ++;
		constraints.gridy --;
		constraints.weighty = 0f;
		constraints.insets.bottom = 5;
		constraints.insets.right = 0;
		constraints.insets.left = 10;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		
		HALabel insertRoutNamLab = HAUtilities.newDescription("Insert routine name:");
		// making insertRoutNamLab match the confirmComLab size to 
		// force gridbaglayout to give the same width
		insertRoutNamLab.setMinimumSize(confirmComLab.getPreferredSize());
		getContent().add(insertRoutNamLab, constraints);
		
		constraints.gridy ++;
		constraints.weighty = 1f;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets.bottom = 30;		
		getContent().add(routinePanel, constraints);
		
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
			
			description = currentRoutine.addCommands(description, commands, new Filter(deviceS, roomS, categoryS), valuesList);	
			commandsDescriptionList.getDefaultModel().addElement(description);
		}
	}
	

}
