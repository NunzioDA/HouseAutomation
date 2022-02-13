package it.homeautomation.view.commandmanagement;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import it.homeautomation.controller.HouseAutomationController;
import it.homeautomation.hagui.HAButton;
import it.homeautomation.hagui.HALabel;
import it.homeautomation.hagui.HAList;
import it.homeautomation.hagui.HAPanel;
import it.homeautomation.hagui.HAScrollPane;
import it.homeautomation.hagui.HAUtilities;
import it.homeautomation.model.Filter;
import it.homeautomation.model.command.Command;
import it.homeautomation.view.interfaces.CommandCreationListener;

/**
 * This panel allow the user to select the command.
 * The class user should specify the commands based on 
 * the device or the category selected by using the method {@code refreshCommands}
 * This class also provide a dynamic panel that changes the input type based on the
 * command selected.
 *  
 *  @author Nunzio D'Amore
 */

public class SelectCommandPanel extends HAPanel
{
	private static final long serialVersionUID = 1L;
	
	private HouseAutomationController controller;
	private String categoryFilter, roomFilter;
	private Object deviceFilter;
	
	private HAList<Command<?>> commandsList = new HAList<>();
	private HAScrollPane commandListScrollPane = new HAScrollPane(commandsList);
	
	private JPanel inputAndConfirmPanel = new JPanel();	
	private HALabel error = new HALabel("", SwingConstants.RIGHT);
	private HAButton confirmCommand = new HAButton("Confirm command");
	
	private CommandDynamicInputArea inputArea = new CommandDynamicInputArea(inputAndConfirmPanel);	
	
	private List<CommandCreationListener> listeners = new ArrayList<>();
	private String filteredCommandDescription;

	public SelectCommandPanel(HouseAutomationController controller)
	{
		this.controller = controller;
		init();
	}
	
	public void addCommandListener(CommandCreationListener listener)
	{
		listeners.add(listener);
	}
	
	private Command<?> getSelectedCommand()
	{
		return commandsList.getSelectedValue();
	}
	
	private String getCommandsGroupDescription(Object value)
	{
		String description = filteredCommandDescription + " -> " + getSelectedCommand().toString();
		
		if(value != null)
		{
			String valueDescription = value.toString();
			
			if(value instanceof Color)
			{
				Color color = (Color)value;
				valueDescription = "[r = " + color.getRed() + ", g = " + color.getGreen() + ", b = "+ color.getBlue() + "]";
			}
			
			description +=  ": " + valueDescription;
		}
		return description;
	}
	
	public void resetPanel()
	{
		inputArea.removeAll();
		inputArea.updateUI();
		error.setText("");
		commandsList.clearSelection();
		confirmCommand.setEnabled(false);
	}
	
	private void confirmButtonAction()
	{
		List<Command<?>> confirmedCommands = new ArrayList<>();
		
		error.setText("");
		Command<?> selectedCommand = getSelectedCommand();
		
		if(selectedCommand != null)
		{
			// MANAGE SINGLE VALUE COMMANDS	
			
			Object inputValue = inputArea.getInput();
			
			if(inputValue instanceof Color &&
			!inputArea.isColorSelected())
			{
				error.setText("Select a color.");
			}
			
			if(error.getText().isEmpty())
			{
				List<Object> valuesList = new ArrayList<>();
				valuesList.add(inputValue);
				
				String description = getCommandsGroupDescription(inputValue);
				
				

				error.setText(controller.refreshCommands(new Filter(deviceFilter, roomFilter, categoryFilter), valuesList, confirmedCommands, selectedCommand));
				
				
				if(error.getText().isEmpty()) 
				{
					resetPanel();
					
					listeners
					.stream()
					.forEach(a-> a.commandListCreated(description,confirmedCommands, valuesList));
				}
			}			
		}
	}
	
	private void initConfirmButtonAction()
	{
		confirmCommand.addActionListener(new ActionListener() {			
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				confirmButtonAction();			
			}
		});
	}
	
	
	private void initCommandsListListener()
	{
		commandsList.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				int index = commandsList.getSelectedIndex();
				
				if(index >= 0)
				{	
					confirmCommand.setEnabled(true);
					Command<?> selectedCommand = commandsList.getDefaultModel().getElementAt(index);
					inputArea.manageInputPanel(selectedCommand.getValuesTypes());
				}

			}
		});
	}
	
	private void initValueSelectionPanel()
	{
		inputAndConfirmPanel.setLayout(new GridBagLayout());
		error.setForeground(Color.red);
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weighty = 1f;
		constraints.weightx = 1f;
		
		
		
		inputAndConfirmPanel.add(inputArea, constraints);
		
		constraints.insets = new Insets(20, 0, 0, 0);
		
		constraints.gridy ++;
		constraints.weighty = 0.3f;
		inputAndConfirmPanel.add(error, constraints);
		
		constraints.gridy ++;
		inputAndConfirmPanel.add(confirmCommand, constraints);
	}
	

	
	private void init()
	{
		initValueSelectionPanel();
		resetPanel();
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weighty = 0.1f;
		constraints.weightx = 1f;
		constraints.insets = new Insets(0,0,5,0);
		add(HAUtilities.newDescription("Select commands"), constraints);
		
		constraints.gridx ++;		
		add(HAUtilities.newDescription("Insert value"), constraints);
		
		constraints.weighty = 1f;
		constraints.gridx = 0;
		constraints.gridy ++;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets.bottom = 0;
		constraints.insets.right = 20;
		add(commandListScrollPane, constraints);	
		
		constraints.insets.right = 0;
		constraints.gridx ++;
		// making commandListScrollPane match the inputAndConfirmPanel size to 
		// force gridbaglayout to give the same width
		commandListScrollPane.setPreferredSize(inputAndConfirmPanel.getPreferredSize());
		add(inputAndConfirmPanel, constraints);
		
		reloadColors();
		
		commandsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		initCommandsListListener();
		initConfirmButtonAction();
	}
	
	public void clearCommandsList()
	{	
		commandsList.getDefaultModel().removeAllElements();
	}

	
	public void refreshCommands(String groupDescription, List<Command<?>> commands, String category, String room, Object device)
	{
		this.deviceFilter = device;
		this.categoryFilter = category;
		this.roomFilter = room;
		this.filteredCommandDescription = groupDescription;
		this.commandsList.getDefaultModel().removeAllElements();
		this.commandsList.getDefaultModel().addAll(commands);
		
		// making isAGroup match the text field size so 
		// that its column does not get tightened
		inputAndConfirmPanel.setPreferredSize(commandListScrollPane.getPreferredSize());
		
		confirmCommand.setEnabled(false);
		inputArea.removeAll();
	}
	
	@Override
	public void reloadColors()
	{
		if(inputAndConfirmPanel != null)
			inputAndConfirmPanel.setBackground(HAUtilities.getBackgroundColor());
		
		
		inputArea.reloadColors();
		
		commandsList.reloadColors();
		setBackground(HAUtilities.getBackgroundColor());
	}

}
