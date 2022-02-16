package it.homeautomation.view.implementation;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JColorChooser;

import it.homeautomation.hagui.HAButton;
import it.homeautomation.hagui.HAUtilities;
import it.homeautomation.view.ColorSelectionListener;
import it.homeautomation.view.implementation.frame.DisableMainFrame;

public class ColorChooseDialog extends DisableMainFrame
{
	private static final long serialVersionUID = 1L;
	
	JColorChooser colorChooser = new JColorChooser();
	HAButton confirmButton = new HAButton("Confirm color");
	
	public ColorChooseDialog(ColorSelectionListener listener)
	{
		super( "Choose Color", 600, 500);

		setBackground(HAUtilities.getBackgroundColor());
		colorChooser.setBackground(HAUtilities.getBackgroundColor());
		
		setContentLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weighty = 1f;
		constraints.weightx = 1f;
		constraints.insets = new Insets(50, 0, 0, 0);
		addComponent(colorChooser, constraints);
		
		constraints.gridy ++;
		constraints.insets = new Insets(20, 20, 20, 20);
		constraints.weighty = 0.3f;
		addComponent(confirmButton, constraints);
		

		
		confirmButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				listener.colorChosen(colorChooser.getColor());
				setVisible(false);
				dispose();
			}
		});
		
		setVisible(true);
	}

	@Override
	public void reloadColors()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resizeContent()
	{
		// TODO Auto-generated method stub
		
	}
}
