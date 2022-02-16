package it.homeautomation.view;

import it.homeautomation.controller.HouseAutomationController;

public interface View
{
	public void welcomeScreen();
	public void mainScreen(String houseName);	
	public void setController(HouseAutomationController controller);
	public HouseAutomationController getController();
	public void showMessage(String message, boolean error);
	public void deviceStateUpdate();
}
