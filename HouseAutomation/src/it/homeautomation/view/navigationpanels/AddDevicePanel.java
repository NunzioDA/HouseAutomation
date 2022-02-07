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
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import it.homeautomation.controller.HouseAutomationController;
import it.homeautomation.hagui.HAButton;
import it.homeautomation.hagui.HALabel;
import it.homeautomation.hagui.HAList;
import it.homeautomation.hagui.HANavigationDrawerPanel;
import it.homeautomation.hagui.HAScrollPane;
import it.homeautomation.hagui.HATextField;
import it.homeautomation.hagui.HAUtilities;
import it.homeautomation.model.AvailableFeature;
import it.homeautomation.model.DeviceGroup;
import it.homeautomation.model.features.DeviceFeature;
import it.homeautomation.view.DeviceFeatureCard;
import it.homeautomation.view.FeatureList;

public class AddDevicePanel extends HANavigationDrawerPanel
{
	private static final long serialVersionUID = 1L;
	private static final String DEVICE_NAME_MISSING = "<html>Device name missing.</html>";
	private static final String ROOM_MISSING = "<html>Select a room or device group.</html>";
	private static final String DEVICE_FEATURE_MISSING = "<html>No feature selected</html>";
	
	private HouseAutomationController houseController;
	
	private HATextField deviceNameField;
	private HATextField newRoomName;
	
	
	private DefaultListModel<DeviceFeatureCard.CardStatus> featuresListModel = new DefaultListModel<>();
	private FeatureList features = new FeatureList(featuresListModel);	
	private HAScrollPane featuresListPane = new HAScrollPane(features);
	
	
	private DefaultListModel<String> roomListModel = new DefaultListModel<>();
	private HAList<String> roomsList = new HAList<>(roomListModel);	
	private HAScrollPane roomsListPane = new HAScrollPane(roomsList);
	
	private DefaultListModel<DeviceGroup> deviceGroupModel = new DefaultListModel<>();
	private HAList<DeviceGroup> deviceGroupList = new HAList<>(deviceGroupModel);	
	private HAScrollPane deviceGroupListPane = new HAScrollPane(deviceGroupList);
	
	private JCheckBox isAGroup = new JCheckBox("it is a group");
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
	
	private void addNewDevice()
	{
		String room = newRoomName.getText();
		
		if(newRoomName.getText().isEmpty())
			room = roomsList.getSelectedValue();
		
		
		List<DeviceFeature> selectedFeatures = getSelectedFeatures();
		
		boolean group = isAGroup.isEnabled() && isAGroup.isSelected();
		
		DeviceGroup groupDevice = deviceGroupList.getSelectedValue();
		
		if(groupDevice == null)
			houseController.addDevice(deviceNameField.getText(), room, selectedFeatures, group);
		
		else houseController.addNewDeviceToGroup(groupDevice, deviceNameField.getText(), selectedFeatures);
		
		updateContent();
	}
	
	@Override
	public void updateContent()
	{
		newRoomName.setText("");
		deviceNameField.setText("");
		error.setText("");	

		deviceGroupModel.removeAllElements();
		deviceGroupModel.addAll(houseController.getAllDeviceGroup());
		
		featuresListModel.removeAllElements();
		
		AvailableFeature
		.getList()
		.stream()
		.forEach(f -> featuresListModel
				.addElement(new DeviceFeatureCard.CardStatus(f)));
		
		roomListModel.removeAllElements();
		roomListModel.addAll(houseController.getRoomsList());
	}
	
	private void initConfirmButton()
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
				else if(deviceGroupList.getSelectedValuesList().size() == 0 &&
						newRoomName.getText().isEmpty() && roomsList.getSelectedValuesList().size() == 0)
				{
					error.setText(ROOM_MISSING);
				}				
				else addNewDevice();
			}
		});
	}
	
	private void initRoomField()
	{
		
		newRoomName.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e)
			{
				if(newRoomName.getText().isEmpty())
				{
					roomsList.setEnabled(true);
					deviceGroupList.setEnabled(true);
				}
			}
			@Override
			public void insertUpdate(DocumentEvent e)
			{
				roomsList.setEnabled(false);
				roomsList.clearSelection();
				
				deviceGroupList.setEnabled(false);
				deviceGroupList.clearSelection();
				
				isAGroup.setEnabled(true);
			}
			
			@Override
			public void changedUpdate(DocumentEvent e){}
		});
	}
	

	
	private void initGroupListListener()
	{
		deviceGroupList.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				if(deviceGroupList.getSelectedValue() != null)
					roomsList.clearSelection();				
				
				isAGroup.setEnabled(deviceGroupList.getSelectedValue() == null);
				
			}
		});
	}
	
	private void initRoomListListener()
	{
		roomsList.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				if(roomsList.getSelectedValue() != null)
					deviceGroupList.clearSelection();
			}
		});
	}

	private void initComponents()
	{	
		deviceNameField = new HATextField(50);
		newRoomName = new HATextField(50);

		error = new HALabel("", SwingConstants.RIGHT);
		error.setForeground(Color.red);
		
		roomsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		initRoomField();		
		initGroupListListener();
		initRoomListListener();
		initConfirmButton();
	}
	
	private void init()
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
		getContent().add(HAUtilities.newDescription("Device name"), constraints);
		
		constraints.insets.bottom = 50;
		constraints.gridy ++;		
		constraints.fill = GridBagConstraints.BOTH;
		// adding device name text field
		getContent().add(deviceNameField, constraints);

		
		constraints.gridy ++;		
		constraints.fill = GridBagConstraints.HORIZONTAL;
		// adding field description feature list
		constraints.insets.bottom = 0;
		getContent().add(HAUtilities.newDescription("Device features"), constraints);
		
		constraints.weighty = 1f;
		constraints.insets.bottom = 50;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridy ++;
		// adding features list
		
		features.setLayoutOrientation(JList.VERTICAL);	
		getContent().add(featuresListPane, constraints);		

		constraints.gridy ++;
		constraints.insets.bottom = 30;
		constraints.weighty = 0.2f;
		getContent().add(confirm, constraints);		
		
		constraints.gridx ++;
		constraints.weightx = 0.1f;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.CENTER;
		getContent().add(error, constraints);		
		

		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.weighty = 0.1f;
		constraints.weightx = 1f;
		constraints.insets.bottom = 0;
		constraints.anchor = GridBagConstraints.EAST;
		constraints.fill = GridBagConstraints.HORIZONTAL;		
		// adding field description room name
		getContent().add(HAUtilities.newDescription("New room name"), constraints);
		
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets.bottom = 50;
		constraints.gridy ++;
		// adding field description device name
		getContent().add(newRoomName, constraints);

		constraints.gridy ++;
		constraints.fill = GridBagConstraints.HORIZONTAL;		
		constraints.insets.bottom = 0;
		// adding field description feature list
		getContent().add(HAUtilities.newDescription("Existing room"), constraints);
		
		constraints.weighty = 1f;
		constraints.insets.bottom = 50;
		constraints.gridy ++;
		constraints.fill = GridBagConstraints.BOTH;
		// adding field description rooms list		
		roomsList.setLayoutOrientation(JList.VERTICAL);		
		getContent().add(roomsListPane, constraints);

		constraints.gridy = 1;
		constraints.gridx ++;		
		constraints.weighty = 0.1f;
		constraints.fill = GridBagConstraints.BOTH;	
		// making isAGroup match the text field size so 
		// that its column does not get tightened
		isAGroup.setPreferredSize(deviceNameField.getPreferredSize());
		getContent().add(isAGroup, constraints);
		
		constraints.gridy ++;		
		constraints.insets.right = 0;
		constraints.insets.bottom = 0;
		constraints.weighty = 0.1f;
		constraints.fill = GridBagConstraints.HORIZONTAL;	
		getContent().add(HAUtilities.newDescription("Device group"), constraints);
		
		constraints.gridy ++;
		constraints.weighty = 1f;
		constraints.insets.bottom = 50;
		constraints.fill = GridBagConstraints.BOTH;
		deviceGroupList.setLayoutOrientation(JList.VERTICAL);	
		getContent().add(deviceGroupListPane, constraints);
		
		
		reloadColors();
	}
	


	@Override
	public void reloadColors()
	{
		
		getContent().setBackground(HAUtilities.getBackgroundColor());
		setBackground(HAUtilities.getBackgroundColor());
		featuresListPane.reloadColors();
		roomsListPane.reloadColors();
		features.reloadColors();
		roomsList.reloadColors();
		isAGroup.setOpaque(false);
		isAGroup.setForeground(HAUtilities.getForegroundColor());
		
	}


}
