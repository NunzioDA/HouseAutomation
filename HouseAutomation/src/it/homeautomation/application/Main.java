package it.homeautomation.application;

import it.homeautomation.controller.HouseAutomationController;
import it.homeautomation.model.HouseMap;
import it.homeautomation.view.implementation.HAViewImplementation;

public class Main{
	
	public static void main(String[] args) 
	{
		
		new HouseAutomationController(new HouseMap(), HAViewImplementation.getSingleton()).startWelcomeScreen();
	}

}
