package it.homeautomation.hagui;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;

public class HAComboBox<E> extends JComboBox<E>
{
	private static final long serialVersionUID = 1L;

	public HAComboBox(DefaultComboBoxModel<E> model)
	{
		super(model);
		
		
        setEditable(true);
        setFont(HATools.getThinFont().deriveFont(20f));
		setForeground(HATools.getForegroundColor());
        getEditor().getEditorComponent().setBackground(HATools.getBackgroundColor());
		((JTextField) getEditor().getEditorComponent()).setForeground(HATools.getForegroundColor());
	}
}
