package it.homeautomation.view.commandmanagement;

import java.awt.GridLayout;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.JPanel;

import it.homeautomation.hagui.HAList;
import it.homeautomation.hagui.HAScrollPane;

public class CommandsExecutionLog extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private HAList<String> commandsLog = new HAList<>();
	private HAScrollPane commandsLogScrollPane = new HAScrollPane(commandsLog);
	
	public CommandsExecutionLog()
	{
		setLayout(new GridLayout());
		add(commandsLogScrollPane);
		
		//auto scroll
		commandsLogScrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
		    public void adjustmentValueChanged(AdjustmentEvent e) {
		        e.getAdjustable().setValue(e.getAdjustable().getMaximum());  
		    }
		});
	}
	
	public void startRoutineExecution(String name)
	{
		commandsLog.getDefaultModel().addElement("> Executing [ "+ name + " ] routine");
	}
	
	public void startCommandExecution()
	{
		commandsLog.getDefaultModel().addElement("> Executing command");
	}
	
	public void executeCommand(String command)
	{
		commandsLog.getDefaultModel().addElement(command);
	}
	
	public void endExecution()
	{
		commandsLog.getDefaultModel().addElement("> Done.");
	}

}
