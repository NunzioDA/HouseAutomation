package it.homeautomation.hagui;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.metal.MetalComboBoxButton;

public class HAComboBox<E> extends JComboBox<E>
{
	private static final long serialVersionUID = 1L;

	public HAComboBox(DefaultComboBoxModel<E> model)
	{
		super(model);
		
		
        setEditable(true);
        setFont(HAUtilities.getThinFont().deriveFont(20f));
		setForeground(HAUtilities.getForegroundColor());
        getEditor().getEditorComponent().setBackground(HAUtilities.getBackgroundColor());
		((JTextField) getEditor().getEditorComponent()).setForeground(HAUtilities.getForegroundColor());
		((JTextField) getEditor().getEditorComponent()).setEditable(false);
		
		setNormalColors();		
		
	}
	
	private MetalComboBoxButton getButton()
	{
		MetalComboBoxButton bu = null;
		for(Component c : getComponents())
			if(c instanceof MetalComboBoxButton) 
			{
				bu = ((MetalComboBoxButton)c);				
			}
		
		return bu;
	}
	
	private void setNormalColors()
	{
		((JTextField) getEditor().getEditorComponent()).setBorder(new MatteBorder(2, 2, 2, 0, HAUtilities.getPrimaryColor()));
		getButton().setBackground(HAUtilities.getPrimaryColor());
		getButton().setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
	}
	
	private void setDisabledColors()
	{
		getButton().setBackground(HAUtilities.getDarkBackgroundColor());	
		((JTextField) getEditor().getEditorComponent()).setBorder(new MatteBorder(2, 2, 2, 0, HAUtilities.getDarkBackgroundColor()));
	}
	
	@Override
	public void setEnabled(boolean b)
	{
		super.setEnabled(b);
		
		if(b) {
			setNormalColors();
		}
		else {
			setDisabledColors();
		}
	}
	
	
}
