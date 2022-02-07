package it.homeautomation.view.commandmanagement;

import java.awt.GridLayout;

import javax.swing.DefaultListModel;
import javax.swing.JPanel;

import it.homeautomation.hagui.HAList;
import it.homeautomation.hagui.HAScrollPane;

public class CommandsExecutionLog extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private DefaultListModel<String> commandsLogModel = new DefaultListModel<>();
	private HAList<String> commandsLog = new HAList<>(commandsLogModel);
	private HAScrollPane commandsLogScrollPane = new HAScrollPane(commandsLog);
	
	public CommandsExecutionLog()
	{
		setLayout(new GridLayout());
		add(commandsLogScrollPane);
	}
	
	public void startRoutineExecution(String name)
	{
		commandsLogModel.addElement("> Executing [ "+ name + " ] routine");
	}
	
	public void startCommandExecution()
	{
		commandsLogModel.addElement("> Executing command");
	}
	
	public void executeCommand(String command)
	{
		commandsLogModel.addElement(command);
	}
	
	public void endExecution()
	{
		commandsLogModel.addElement("> Done.");
	}

}
