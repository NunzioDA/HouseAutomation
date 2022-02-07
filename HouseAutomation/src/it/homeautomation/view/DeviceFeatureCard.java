package it.homeautomation.view;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.net.URL;

import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import it.homeautomation.hagui.HAImageView;
import it.homeautomation.hagui.HAPanel;
import it.homeautomation.hagui.HAUtilities;
import it.homeautomation.model.features.DeviceFeature;

public class DeviceFeatureCard extends HAPanel implements ListCellRenderer<DeviceFeatureCard.CardStatus>
{	
	private static final long serialVersionUID = 1L;
	
	JCheckBox checkBox = new JCheckBox();

	@Override
	public Component getListCellRendererComponent(JList<? extends CardStatus> list, CardStatus value, int index,
			boolean isSelected, boolean cellHasFocus)
	{
		setLayout(new GridBagLayout());
		removeAll();
		checkBox.setSelected(value.isChecked());		
		checkBox.setText(value.getFeature().toString());

		reloadColors();		
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.WEST;
		add(checkBox,constraints);
		
		URL url = HAUtilities.getIconPath(value.getFeature().getIconID());
		
		if(url != null)
		{
			constraints.gridx = 1;
			constraints.weightx = 0.1;
			constraints.fill = GridBagConstraints.BOTH;
			add(new HAImageView(url, 2),constraints);
		}
		
		return this;
	}
	
	@Override
	public void reloadColors()
	{
		setBackground(HAUtilities.getDarkBackgroundColor());
		checkBox.setOpaque(false);
		checkBox.setForeground(HAUtilities.getForegroundColor());

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
