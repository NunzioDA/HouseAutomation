package it.homeautomation.view.commandmanagement;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.stream.Stream;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import it.homeautomation.hagui.HAButton;
import it.homeautomation.hagui.HAPanel;
import it.homeautomation.hagui.HATextField;
import it.homeautomation.hagui.HAUtilities;
import it.homeautomation.view.ColorChooseDialog;
import it.homeautomation.view.interfaces.ColorSelectionListener;

public class CommandDynamicInputArea extends HAPanel implements ColorSelectionListener
{
	private static final long serialVersionUID = 1L;

	private JPanel inputAndConfirmPanel;
	
	private HATextField alphaNumValues = new HATextField(20);
	private JPanel chooseColorPanel = new JPanel();
	private JPanel colorVisualizer = new JPanel();
	private HAButton startColorChooser = new HAButton("Select a color");
	
	private boolean colorSelected = false;
	
	public CommandDynamicInputArea(JPanel inputAndConfirmPanel)
	{
		this.inputAndConfirmPanel = inputAndConfirmPanel;
		init();
	}
	
	private void init()
	{
		setLayout(new GridLayout(1,1));
		initColorChooserPanel();
	}
	
	
	private void initColorChooserPanel()
	{
		colorVisualizer.setBackground(Color.black);
		chooseColorPanel.setLayout(new GridLayout(1,2));
		chooseColorPanel.add(colorVisualizer);
		chooseColorPanel.add(startColorChooser);
		startColorChooser.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JFrame frame = (JFrame)SwingUtilities.getWindowAncestor(CommandDynamicInputArea.this);
				new ColorChooseDialog(frame, CommandDynamicInputArea.this);
			}
		});
	}
	
	public Object getInput()
	{
		Object result = null;
		
		boolean isAlphaNum = Stream.of(getComponents()).anyMatch(alphaNumValues::equals);
		
		if(isAlphaNum)
		{			
			result = alphaNumValues.getText();			
		}
		else {
			boolean isColor = Stream.of(getComponents()).anyMatch(chooseColorPanel::equals);
			
			if(isColor)
				result = colorVisualizer.getBackground();
		}
		
		return result;
	}
	
	public boolean isColorSelected()
	{
		return colorSelected;
	}
	
	public  void manageInputPanel(List<Class<?>> values)
	{
		if(values.size()>0)
		{
			Class<?> classV = values.get(0);
			removeAll();
			
			if(classV.equals(String.class) || 
			   classV.equals(Integer.class) ||
			   classV.equals(Float.class))
			{
				add(alphaNumValues);
			}
			else if(classV.equals(Color.class))
			{
				add(chooseColorPanel);
			}
			else removeAll();
		}else removeAll();
		
		inputAndConfirmPanel.updateUI();
	}
	
	@Override
	public void reloadColors()
	{
		chooseColorPanel.setBackground(HAUtilities.getBackgroundColor());
		startColorChooser.reloadColors();
		setBackground(HAUtilities.getBackgroundColor());
	}
	
	@Override
	public void colorChosen(Color color)
	{
		if(color != null) 
		{
			colorSelected = true;
			colorVisualizer.setBackground(color);
		}
	}

}
