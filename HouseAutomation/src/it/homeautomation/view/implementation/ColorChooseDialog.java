package it.homeautomation.view.implementation;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;

import it.homeautomation.hagui.HAButton;
import it.homeautomation.hagui.HAUtilities;
import it.homeautomation.view.interfaces.ColorSelectionListener;

public class ColorChooseDialog extends JDialog
{
	private static final long serialVersionUID = 1L;
	
	JColorChooser colorChooser = new JColorChooser();
	HAButton confirmButton = new HAButton("Confirm color");
	
	public ColorChooseDialog(JFrame frame, ColorSelectionListener listener)
	{
		super(frame, "Choose Color");
		frame.setEnabled(false);
		setSize(600,500);
		setBackground(HAUtilities.getBackgroundColor());
		colorChooser.setBackground(HAUtilities.getBackgroundColor());
		
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weighty = 1f;
		constraints.weightx = 1f;
		
		add(colorChooser, constraints);
		
		constraints.gridy ++;
		constraints.insets = new Insets(20, 20, 20, 20);
		constraints.weighty = 0.3f;
		add(confirmButton, constraints);
		
		addWindowListener(new WindowListener() {
			@Override
			public void windowClosed(WindowEvent e)
			{}			
			@Override
			public void windowActivated(WindowEvent e)
			{}
			@Override
			public void windowOpened(WindowEvent e)
			{}			
			@Override
			public void windowIconified(WindowEvent e)
			{}			
			@Override
			public void windowDeiconified(WindowEvent e)
			{}			
			@Override
			public void windowDeactivated(WindowEvent e)
			{}
			
			@Override
			public void windowClosing(WindowEvent e)
			{
				frame.setEnabled(true);
				listener.colorChosen(null);
			}			
			
		});
		
		confirmButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				listener.colorChosen(colorChooser.getColor());
				frame.setEnabled(true);
				setVisible(false);
				dispose();
			}
		});
		
		setVisible(true);
	}
}
