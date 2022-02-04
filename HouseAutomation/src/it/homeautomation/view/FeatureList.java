package it.homeautomation.view;


import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

import it.homeautomation.hagui.HAList;
import it.homeautomation.view.DeviceFeatureCard.CardStatus;

public class FeatureList extends HAList<DeviceFeatureCard.CardStatus>
{
	private static final long serialVersionUID = 1L;

	public FeatureList(ListModel<CardStatus> model)
	{
		super(model);
		init();		
	}
	
	private void init()
	{
		setCellRenderer(new DeviceFeatureCard());
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		addMouseListener(new MouseAdapter() {			
			@Override
			public void mousePressed(MouseEvent e)
			{
				JList<?> list = (JList<?>)e.getSource();
				int index = list.locationToIndex(e.getPoint());
				getModel().getElementAt(index).invertChecked();
				FeatureList.this.repaint();
			}
		});
	}

}
