package it.homeautomation.view.implementation;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import it.homeautomation.controller.CommandsGroupUtility;
import it.homeautomation.hagui.HAButton;
import it.homeautomation.hagui.HAFrame;
import it.homeautomation.hagui.HALabel;
import it.homeautomation.hagui.HAList;
import it.homeautomation.hagui.HAUtilities;
import it.homeautomation.model.command.Command;
import it.homeautomation.model.command.SingleValueCommand;
import it.homeautomation.model.features.DeviceFeature;
import it.homeautomation.view.commandmanagement.CommandDynamicInputArea;

public class DeviceCommandExecutonFrame extends HAFrame
{

	private static final long serialVersionUID = 1L;
	
	private HAList<Command<?>> deviceCommandsList = new HAList<>();
	private JPanel inputAndConfirmPanel = new JPanel();
	private CommandDynamicInputArea inputArea = new CommandDynamicInputArea(inputAndConfirmPanel) ;
	
	private HALabel error = new HALabel("", SwingConstants.RIGHT);
	private HAButton confirmCommand = new HAButton("Execute command");
	private DeviceCommandExecutedListener listener;
	
	public DeviceCommandExecutonFrame(int width, int height, DeviceFeature deviceFeature, DeviceCommandExecutedListener listener)
	{
		super(deviceFeature.toString() +" feature commands", width, height);
		
		this.listener = listener;
		
		deviceCommandsList
		.getDefaultModel()
		.addAll(deviceFeature
				.getCommands());
		
		init();
		reloadColors();
		
		setResizable(false);
		setVisible(true);
	}
	
	private void initCommandListListener()
	{
		deviceCommandsList.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				Command<?> command = deviceCommandsList.getSelectedValue();
				
				if(command != null)
					inputArea.manageInputPanel(command.getValuesTypes());
			}
		});
	}
	
	private void initConfirmButtonAction()
	{
		confirmCommand.addActionListener(new ActionListener() {			
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Object inputValue = inputArea.getInput();
				
				if(inputValue != null)
				{
					if(inputValue instanceof Color && !inputArea.isColorSelected())
					{
						error.setText("Select a color.");
					}
					else if(inputValue.toString().isEmpty())
							error.setText("Insert a value");
					
					if(error.getText().isEmpty())
					{
						Command<?> selectedCommand = deviceCommandsList.getSelectedValue();
						
						if(selectedCommand instanceof SingleValueCommand<?,?>)
							error.setText(CommandsGroupUtility
									.manageSingleValueCommands((SingleValueCommand<?,?>) selectedCommand, inputValue));
						
						if(error.getText().isEmpty()) 
						{
							selectedCommand.execute();
							listener.commandExecuted(selectedCommand);
							setVisible(false);
							dispose();
						}
					}
				}
				else error.setText("Select a command.");
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
		initConfirmButtonAction();
		initCommandListListener();
		
		setContentLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weighty = 0.1f;
		constraints.weightx = 1f;
		constraints.insets = new Insets(50, 20, 0, 20);
		
		addComponent(HAUtilities.newDescription("Select command"), constraints);
		
		constraints.weighty = 1f;
		constraints.gridy ++;
		constraints.insets.top = 0;
		constraints.insets.bottom = 30;
		addComponent(deviceCommandsList, constraints);
		
		constraints.gridx ++;
		addComponent(inputAndConfirmPanel, constraints);
	}
	
	@Override
	public void reloadColors()
	{
		if(inputAndConfirmPanel != null)
			inputAndConfirmPanel.setBackground(HAUtilities.getBackgroundColor());	
		
		if(inputArea != null)
			inputArea.reloadColors();	
	}


	@Override
	public void resizeContent()
	{
		
	}
	
	public interface DeviceCommandExecutedListener
	{
		public void commandExecuted(Command<?> command);
	}
	
}
