package it.homeautomation.view;

import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import it.homeautomation.hagui.HAPanel;
import it.homeautomation.hagui.HATools;
import it.homeautomation.model.features.DeviceFeature;

public class DeviceFeatureCard extends HAPanel implements ListCellRenderer<DeviceFeatureCard.CardStatus>
{	
	private static final long serialVersionUID = 1L;
	
	JCheckBox checkBox = new JCheckBox();

	@Override
	public Component getListCellRendererComponent(JList<? extends CardStatus> list, CardStatus value, int index,
			boolean isSelected, boolean cellHasFocus)
	{
		setLayout(new GridLayout());

		checkBox.setSelected(value.isChecked());		
		checkBox.setText(value.getFeature().toString());
		
		reloadColors();
		
		add(checkBox);
		
		return this;
	}
	
	@Override
	public void reloadColors()
	{
		setBackground(HATools.getBackgroundColor());
		checkBox.setBackground(HATools.getDarkBackgroundColor());
		checkBox.setForeground(HATools.getForegroundColor());

	}
	
	public static class CardStatus
	{
		private boolean checked = false;
		private DeviceFeature feature;
		
		public CardStatus(DeviceFeature feature)
		{
			this.setFeature(feature);
		}

		public boolean isChecked()
		{
			return checked;
		}

		public void invertChecked()
		{
			this.checked = !this.checked;
		}

		public DeviceFeature getFeature()
		{
			return feature;
		}

		public void setFeature(DeviceFeature feature)
		{
			this.feature = feature;
		}
		
	}




}
