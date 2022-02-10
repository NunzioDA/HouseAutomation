package it.homeautomation.application;

import it.homeautomation.controller.HouseAutomationController;
import it.homeautomation.model.HouseMap;

public class Main{
	
	public static void main(String[] args) 
	{
		new HouseAutomationController(new HouseMap()).startWelcomeScreen();
	}

}
