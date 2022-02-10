package it.homeautomation.view.implementation;

import it.homeautomation.controller.HouseAutomationController;
import it.homeautomation.view.View;

public class HAViewImplementation implements View
{
	private static HAViewImplementation singleton;
	private HouseAutomationController controller;
	
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
			new WelcomeFrame("Welcome", 500, 500);
	}

	@Override
	public void mainScreen(String houseName)
	{
		if(controller != null)
			new MainFrame(houseName, 1220, 700);
	}

	@Override
	public HouseAutomationController getController()
	{
		return controller;
	}

}
