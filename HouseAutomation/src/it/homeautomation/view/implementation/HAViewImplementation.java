package it.homeautomation.view.implementation;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import it.homeautomation.controller.HouseAutomationController;
import it.homeautomation.view.View;
import it.homeautomation.view.implementation.frame.MainFrame;
import it.homeautomation.view.implementation.frame.WelcomeFrame;

public class HAViewImplementation implements View
{
	private static HAViewImplementation singleton;
	private HouseAutomationController controller;
	private MainFrame mainFrame;
	private List<Component> mainFrameDisableFrames = new ArrayList<>();
	
	public static HAViewImplementation getSingleton()
	{
		if(singleton == null)
			singleton = new HAViewImplementation();
		
		return singleton;
	}
	
	@Override
	public void  setController(HouseAutomationController controller)
	{
		this.controller = controller;
	}
	
	@Override
	public void welcomeScreen()
	{
		if(controller != null)
			WelcomeFrame.getAdaptedWelcomFrame("Welcome");
	}

	@Override
	public void mainScreen(String houseName)
	{
		if(controller != null)
			mainFrame = MainFrame.getAdaptedMainFrame(houseName);
	}
	
	public void removeFromDisablerList(Component frame)
	{
		mainFrameDisableFrames.remove(frame);
		if(mainFrameDisableFrames.size() == 0)
		{
			mainFrame.setEnabled(true);
			mainFrame.requestFocus();
		}		
	}

	public void addToDisablerList(Component frame)
	{
		mainFrameDisableFrames.add(frame);		
		mainFrame.setEnabled(false);
	}
	
	@Override
	public HouseAutomationController getController()
	{
		return controller;
	}

	@Override
	public void showMessage(String message, boolean error)
	{
		HAMessageBox.showMessage(message, error);
	}

	@Override
	public void deviceStateUpdate()
	{
		mainFrame.updateHomePage();
	}
	
	
}
