package it.homeautomation.view.implementation.frame;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

import it.homeautomation.hagui.HAFrame;
import it.homeautomation.hagui.HAUtilities;
import it.homeautomation.view.implementation.navigationpanels.AddDevicePanel;
import it.homeautomation.view.implementation.navigationpanels.ExecuteCommandPanel;
import it.homeautomation.view.implementation.navigationpanels.HomePanel;
import it.homeautomation.view.implementation.navigationpanels.ManageRoutinePanel;
import it.homeautomation.view.implementation.navigationpanels.RoutineCreationPanel;
import it.homeautomation.hagui.HANavigationDrawer;


public class MainFrame extends HAFrame
{
	private static final long serialVersionUID = 1L;
	private static final int SIDE_TOOL_PANEL_RATIO = 4;
	
	
	private JPanel contentPanel;
	private HANavigationDrawer navigationDrawer;
	private HomePanel home = new HomePanel();
	
	public static MainFrame getAdaptedMainFrame(String houseName)
	{
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) (screenSize.getWidth() * 0.61f);
		int height = (int) (width * 0.57f);
		
		return new MainFrame(houseName, width, height);
	}
	
	public MainFrame(String houseName, int width, int height)
	{		
		super("Home Automation", width, height);	
		
		initComponents(houseName);
		setMinimumSize(new Dimension(500, 500));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		//Show add panel
		navigationDrawer.showPanel(1);
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
		
		add(navigationDrawer);
		add(contentPanel);
		
		
		navigationDrawer.addButton("Home", home);
		navigationDrawer.addButton("Add new Device", new AddDevicePanel());
		navigationDrawer.addButton("Execute command", new ExecuteCommandPanel());
		navigationDrawer.addButton("Create new routine", new RoutineCreationPanel());	
		navigationDrawer.addButton("Manage routines", new ManageRoutinePanel());
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

	public void updateHomePage()
	{
		home.updateContent();
	}
}
