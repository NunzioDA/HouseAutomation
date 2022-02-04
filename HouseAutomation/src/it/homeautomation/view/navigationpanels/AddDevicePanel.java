package it.homeautomation.view.navigationpanels;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import it.homeautomation.controller.HouseAutomationController;
import it.homeautomation.hagui.HAButton;
import it.homeautomation.hagui.HALabel;
import it.homeautomation.hagui.HAList;
import it.homeautomation.hagui.HANavigationDrawerPanel;
import it.homeautomation.hagui.HAScrollPane;
import it.homeautomation.hagui.HATextField;
import it.homeautomation.hagui.HATools;
import it.homeautomation.model.AvailableFeature;
import it.homeautomation.model.features.DeviceFeature;
import it.homeautomation.view.DeviceFeatureCard;
import it.homeautomation.view.FeatureList;

public class AddDevicePanel extends HANavigationDrawerPanel
{
	private static final long serialVersionUID = 1L;
	private static final String DEVICE_NAME_MISSING = "Device name missing.";
	private static final String ROOM_MISSING = "Select a room or insert a new one.";
	private static final String DEVICE_FEATURE_MISSING = "No feature selected";
	
	private HouseAutomationController houseController;
	
	private HATextField deviceNameField;
	private HATextField newRoomName;
	
	
	private DefaultListModel<DeviceFeatureCard.CardStatus> featuresListModel = new DefaultListModel<>();
	private FeatureList features = new FeatureList(featuresListModel);	
	HAScrollPane featuresListPane = new HAScrollPane(features);
	
	
	private DefaultListModel<String> roomListModel = new DefaultListModel<>();
	private HAList<String> roomsList = new HAList<>(roomListModel);	
	HAScrollPane roomsListPane = new HAScrollPane(roomsList);
	
	private HAButton confirm;
	
	private HALabel error;
	
	public AddDevicePanel(HouseAutomationController houseController)
	{
		super("Add new device");
		this.houseController = houseController;
		getContent().setLayout(new GridBagLayout());		
		init();
	}
	
	private List<DeviceFeature> getSelectedFeatures()
	{
		List<DeviceFeature> selectedFeatures = new ArrayList<>();
		
		for(Enumeration<DeviceFeatureCard.CardStatus> e = featuresListModel.elements(); e.hasMoreElements();)
		{
			DeviceFeatureCard.CardStatus element = e.nextElement();
			
			if(element.isChecked())
				selectedFeatures.add(element.getFeature().getClone());
		}
		
		return selectedFeatures;
	}
	
	public void addNewDevice()
	{
		String room = newRoomName.getText();
		
		if(newRoomName.getText().isEmpty())
			room = roomsList.getSelectedValue();
		
		
		List<DeviceFeature> selectedFeatures = getSelectedFeatures();
		
		houseController.addDevice(deviceNameField.getText(), room, selectedFeatures);
		
		updateContent();
	}
	
	public void initConfirmButton()
	{
		confirm = new HAButton("Confirm");
		confirm.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{				
				if(deviceNameField.getText().isEmpty())
				{
					error.setText(DEVICE_NAME_MISSING);
				}
				else if(features.getSelectedValuesList().size() == 0)
				{
					error.setText(DEVICE_FEATURE_MISSING);
				}
				else if(newRoomName.getText().isEmpty() && roomsList.getSelectedValuesList().size() == 0)
				{
					error.setText(ROOM_MISSING);
				}
				else addNewDevice();
			}
		});
	}
	
	private void initRoomField()
	{
		newRoomName = new HATextField(50);
		newRoomName.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e)
			{
				if(newRoomName.getText().isEmpty())
				{
					roomsList.setEnabled(true);
				}
			}
			@Override
			public void insertUpdate(DocumentEvent e)
			{
				roomsList.setEnabled(false);
			}
			
			@Override
			public void changedUpdate(DocumentEvent e){}
		});
	}
	
	@Override
	public void updateContent()
	{
		featuresListModel.removeAllElements();
		AvailableFeature.getList().stream().forEach(f -> featuresListModel.addElement(new DeviceFeatureCard.CardStatus(f)));
		roomListModel.removeAllElements();
		roomListModel.addAll(houseController.getRoomsList());
	}

	public void initComponents()
	{	
		deviceNameField = new HATextField(50);
		
		initRoomField();		
		
		
		error = new HALabel("", SwingConstants.RIGHT);
		error.setForeground(Color.red);
		
		roomsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		initConfirmButton();
	}
	

	public void init()
	{
		initComponents();
		
		GridBagConstraints constraints = new GridBagConstraints();
		
		
		constraints.insets = new Insets(40,0,20,50);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.insets.top = 0;
		constraints.gridwidth = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weighty = 0.1f;
		constraints.weightx = 1f;
		
		constraints.insets.bottom = 0;
		// adding field description device name
		getContent().add(HATools.newDescription("Device name"), constraints);
		
		constraints.insets.bottom = 50;
		constraints.gridy ++;		
		constraints.fill = GridBagConstraints.BOTH;
		// adding device name text field
		getContent().add(deviceNameField, constraints);

		
		constraints.gridy ++;		
		constraints.fill = GridBagConstraints.HORIZONTAL;
		// adding field description feature list
		constraints.insets.bottom = 0;
		getContent().add(HATools.newDescription("Select device features"), constraints);
		
		constraints.weighty = 1f;
		constraints.insets.bottom = 50;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridy ++;
		// adding features list
		
		features.setLayoutOrientation(JList.VERTICAL);	
		getContent().add(featuresListPane, constraints);		

		constraints.gridy ++;
		constraints.weighty = 0.2f;
		getContent().add(confirm, constraints);		
		
		constraints.gridx ++;
		constraints.weightx = 0.1f;
		constraints.fill = GridBagConstraints.VERTICAL;
		constraints.anchor = GridBagConstraints.EAST;
		getContent().add(error, constraints);		
		
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.weighty = 0.1f;
		constraints.weightx = 1f;
		constraints.insets.bottom = 0;
		
		constraints.insets.right = 0;
		constraints.anchor = GridBagConstraints.EAST;
		constraints.fill = GridBagConstraints.HORIZONTAL;		
		// adding field description room name
		getContent().add(HATools.newDescription("New room name"), constraints);
		
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets.bottom = 50;
		constraints.gridy ++;
		// adding field description device name
		getContent().add(newRoomName, constraints);

		constraints.gridy ++;
		constraints.fill = GridBagConstraints.HORIZONTAL;		
		constraints.insets.bottom = 0;
		// adding field description feature list
		getContent().add(HATools.newDescription("Select existing room"), constraints);
		
		constraints.weighty = 1f;
		constraints.insets.bottom = 50;
		constraints.gridy ++;
		constraints.fill = GridBagConstraints.BOTH;
		// adding field description rooms list		
		
		roomsList.setLayoutOrientation(JList.VERTICAL);		
		getContent().add(roomsListPane, constraints);
		

		reloadColors();
	}
	


	@Override
	public void reloadColors()
	{
		getContent().setBackground(HATools.getBackgroundColor());
		setBackground(HATools.getBackgroundColor());
		featuresListPane.reloadColors();
		roomsListPane.reloadColors();
		features.reloadColors();
		roomsList.reloadColors();
	}


}
