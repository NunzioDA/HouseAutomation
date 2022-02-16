package it.homeautomation.view.implementation;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.border.MatteBorder;

import it.homeautomation.hagui.HAButton;
import it.homeautomation.hagui.HATextCenter;
import it.homeautomation.hagui.HAUtilities;
import it.homeautomation.view.implementation.frame.DisableMainFrame;

public class HAMessageBox extends DisableMainFrame
{
	private static final long serialVersionUID = 1L;
	
	HATextCenter text = new HATextCenter("");
	HAButton ok = new HAButton("Ok");
	
	public static HAMessageBox showMessage(String message, boolean error)
	{
		String title = "Message";
		MatteBorder borders = null;
		
		if(error) {
			title = "ERROR!";
			borders = BorderFactory.createMatteBorder(2, 2, 2, 2, Color.red);
		}
		
		HAMessageBox mexb = new HAMessageBox(title, message, error);
		
		if(error)
			mexb
			.getRootPane()
			.setBorder(borders);
		
		return mexb;		
	}
	
	
	public HAMessageBox(String title, String message, boolean error)
	{
		super(title,400, 300);
		
		if(error)
			Toolkit.getDefaultToolkit().beep();
		
		setContentLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weightx = 1;
		constraints.weighty = 0.71;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.insets = new Insets(50, 50, 0, 50);
		constraints.fill = GridBagConstraints.BOTH;
		
		addComponent(text, constraints);
		
		constraints.gridy ++;		
		constraints.weighty = 0.4f;
		constraints.insets.bottom = 30;
		addComponent(ok, constraints);
		
		reloadColors();
		setResizable(false);
		
		
		ok.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				setVisible(false);
				dispose();
			}
		});
		
		setVisible(true);
		
		text.setText(message);
		requestFocus();
	}

	@Override
	public void reloadColors()
	{
		if(text != null) {
			text.setForeground(HAUtilities.getForegroundColor());
			text.setBackground(HAUtilities.getBackgroundColor());
		}
		if(ok != null)
			ok.reloadColors();
	}

	@Override
	public void resizeContent()
	{

	}
	
}
