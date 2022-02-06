package it.homeautomation.view;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import it.homeautomation.controller.HouseAutomationController;
import it.homeautomation.hagui.HAFrame;
import it.homeautomation.hagui.HAUtilities;
import it.homeautomation.view.navigationpanels.AddDevicePanel;
import it.homeautomation.view.navigationpanels.ExecuteCommandPanel;
import it.homeautomation.view.navigationpanels.ManageRoutinePanel;
import it.homeautomation.view.navigationpanels.HomePanel;
import it.homeautomation.view.navigationpanels.RoutineCreationPanel;
import it.homeautomation.hagui.HANavigationDrawer;


public class MainFrame extends HAFrame
{
	private static final long serialVersionUID = 1L;
	private static final int SIDE_TOOL_PANEL_RATIO = 4;
	
	private HouseAutomationController houseController;
	
	private JPanel contentPanel;
	private HANavigationDrawer navigationDrawer;

	
	public MainFrame(String title, HouseAutomationController houseController, int width, int height)
	{		
		super(title, width, height);
		this.houseController = houseController;
		initComponents(houseController.getHouseName());
		setMinimumSize(new Dimension(500, 500));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void initContentPanel()
	{
		this.contentPanel = new JPanel();		
		contentPanel.setLayout(new GridLayout(1, 1));
	}
	
	private void initComponents(String houseName)
	{
		setContentLayout(null);
		initContentPanel();
		
		int width = getWidth();
		int height = getHeight();
		
		navigationDrawer = new HANavigationDrawer(houseName, width / SIDE_TOOL_PANEL_RATIO, height, this);
		navigationDrawer.setContentPanel(contentPanel);
		navigationDrawer.addButton("Home", new HomePanel(houseController));
		navigationDrawer.addButton("Add new Device", new AddDevicePanel(houseController));
		navigationDrawer.addButton("Execute command", new ExecuteCommandPanel(houseController));
		navigationDrawer.addButton("Create new routine", new RoutineCreationPanel(houseController));	
		navigationDrawer.addButton("Manage routines", new ManageRoutinePanel(houseController));	
		
		add(navigationDrawer);
		add(contentPanel);
	}
	


	public MainFrame(int width, int height)
	{
		super("", width, height);
	}

	@Override
	public void resizeContent()
	{
		int sidePanelWidth = getWidth() / SIDE_TOOL_PANEL_RATIO;
		
		if(navigationDrawer != null) 
		{	
			navigationDrawer.setSize(new Dimension(sidePanelWidth, getHeight()));
		}
		
		if(contentPanel != null)
		{
			contentPanel.setSize(new Dimension((SIDE_TOOL_PANEL_RATIO - 1) * sidePanelWidth, getHeight()));
			contentPanel.setLocation(sidePanelWidth,0);
		}
	}

	@Override
	public void reloadColors()
	{
		if(contentPanel != null)
			contentPanel.setBackground(HAUtilities.getBackgroundColor());
	}

	
}
