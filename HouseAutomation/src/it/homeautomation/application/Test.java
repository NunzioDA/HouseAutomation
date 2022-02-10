package it.homeautomation.application;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import it.homeautomation.controller.CommandsFilterTool;
import it.homeautomation.controller.HouseAutomationController;
import it.homeautomation.model.Device;
import it.homeautomation.model.HouseMap;
import it.homeautomation.model.Routine;
import it.homeautomation.model.command.Command;
import it.homeautomation.model.command.implementation.ChangeColorCommand;
import it.homeautomation.model.command.implementation.ChangeStateCommand;
import it.homeautomation.model.command.implementation.SwitchStateCommand;
import it.homeautomation.model.features.DeviceFeature;
import it.homeautomation.model.features.SingleValueFeature;
import it.homeautomation.model.features.implementation.ColorBased;
import it.homeautomation.model.features.implementation.Light;
import it.homeautomation.model.features.implementation.StateFeature;

class Test
{
	private HouseAutomationController controller;
	
	@org.junit.jupiter.api.Test
	void houseMapCreation()
	{
		controller = new HouseAutomationController(new HouseMap());
		controller.setHouseName("test house");
	}
	
	@org.junit.jupiter.api.Test
	void routineEmptyTest()
	{
		houseMapCreation();
		assertFalse(controller.addRoutine(controller.getRoutineInstance()), "Can not add an empty routine");
	}
	
	void initCommandsDeviceTest()
	{
		houseMapCreation();
		
		List<DeviceFeature> features = new ArrayList<>();
		features.add(new ColorBased());
		
		controller.addDevice("Device1", "Room", features, false);
		controller.addDevice("Device2", "Room2", features, false);
		
		features.clear();
		features.add(new Light());
		controller.addDevice("Device3", "Room", features, false);		
	}
	
	@org.junit.jupiter.api.Test
	void routineUpdateTest()
	{
		initCommandsDeviceTest();
		
		Routine routine = controller.getRoutineInstance();
		routine.setName("Routine");
		
		List<Command<?>> commands = new ArrayList<>();
		SwitchStateCommand command = new SwitchStateCommand();
		command.setFeature(new StateFeature());
		commands.add(command);
		
		routine.addCommands("Description", commands, 
				CommandsFilterTool.ALL_DEVICES, CommandsFilterTool.ALL_ROOMS, CommandsFilterTool.ALL_CATEGORIES, null);
		
		routine.update(controller);
		
		assertEquals(3, routine.getCommands().get(0).getCommandList().size());		
	}
	
	@org.junit.jupiter.api.Test
	void routineUpdateTest2()
	{
		initCommandsDeviceTest();
		
		Routine routine = controller.getRoutineInstance();
		routine.setName("Routine");
		
		List<Command<?>> commands = new ArrayList<>();
		SwitchStateCommand command = new SwitchStateCommand();
		command.setFeature(new StateFeature());
		commands.add(command);
		
		routine.addCommands("Description", commands, 
				CommandsFilterTool.ALL_DEVICES, "Room", CommandsFilterTool.ALL_CATEGORIES, null);
		
		routine.update(controller);
		
		assertEquals(2, routine.getCommands().get(0).getCommandList().size());		
	}
	
	@org.junit.jupiter.api.Test
	void routineUpdateTest3()
	{
		initCommandsDeviceTest();
		
		Routine routine = controller.getRoutineInstance();
		routine.setName("Routine");
		
		List<Command<?>> commands = new ArrayList<>();
		SwitchStateCommand command = new SwitchStateCommand();
		command.setFeature(new StateFeature());
		commands.add(command);
		
		routine.addCommands("Description", commands, 
				CommandsFilterTool.ALL_DEVICES, CommandsFilterTool.ALL_ROOMS, (new ColorBased()).toString(), null);
		
		routine.update(controller);
		
		assertEquals(2, routine.getCommands().get(0).getCommandList().size());		
	}
	
	List<Command<?>> categoryDevicesCommands()
	{
		List<Command<?>> commands = new ArrayList<>();
		
		// CREATING THE COMMAND TO EXECUTE
		ChangeColorCommand changeColor = new ChangeColorCommand();
		changeColor.setFeature(new ColorBased());
		
		// SETTING THE COMMAND VALUE 
		List<Object> values = new ArrayList<>();
		values.add(Color.red);
		
		
		// UPDATING THE COMMAND LIST
		controller
		.getCommandsGroupUtility()
		.refreshCommands(CommandsFilterTool.ALL_DEVICES, 
				 (new ColorBased()).toString(), CommandsFilterTool.ALL_ROOMS, values, commands, changeColor);
		
		return commands;
	}
	
	@org.junit.jupiter.api.Test
	void commandExecutionOnCategoryTest()
	{
		initCommandsDeviceTest();
		List<Command<?>> commands = categoryDevicesCommands();		
		
		// EXECUTING COMMANDS
		commands.forEach(Command::execute);
		
		List<Device> colorBasedDevices = controller.getDevicesByCategory((new ColorBased()).getCategoryName());
		
		boolean thereIsSomeColorBasedDevicesNotRed = colorBasedDevices
				.stream()
				.anyMatch(d -> !d.castToFeature(ColorBased.class)
						.getValue().equals(Color.red));
		
		assertFalse(thereIsSomeColorBasedDevicesNotRed, "Some device did not respond to command");
	}
	
	@org.junit.jupiter.api.Test
	void routineExecution()
	{
		initCommandsDeviceTest();
		Routine routine = controller.getRoutineInstance();
		routine.setName("Routine");
		
		List<Command<?>> commands = categoryDevicesCommands();
		
		// SETTING THE COMMAND VALUE 
		List<Object> values = new ArrayList<>();
		values.add(Color.red);
		
		routine.addCommands("Description", commands, 
				CommandsFilterTool.ALL_DEVICES, CommandsFilterTool.ALL_ROOMS, (new ColorBased()).toString(), null);
		
		commands = new ArrayList<>();
		
		Device firstDevice = controller
				.getAllDevices()
				.get(0);
		
		ChangeStateCommand stateFeature = (ChangeStateCommand) firstDevice
				.getFeatures()
				.get(0)
				.getCommands()
				.stream()
				.filter(c -> (c instanceof ChangeStateCommand))
				.findAny()
				.get();
		
		stateFeature.setValue(StateFeature.State.OFF);
		
		commands.add(stateFeature);
		routine.addCommands("Description", commands, 
				"Device1", CommandsFilterTool.ALL_ROOMS, CommandsFilterTool.ALL_CATEGORIES, null);
		
		routine.execute();
		
		
		List<Device> colorBasedDevices = controller.getDevicesByCategory((new ColorBased()).getCategoryName());
		boolean thereIsSomeColorBasedDevicesNotRed = colorBasedDevices
				.stream()
				.anyMatch(d -> !d.castToFeature(ColorBased.class)
						.getValue().equals(Color.red));		
		
		
		@SuppressWarnings("unchecked")
		StateFeature.State state = ((SingleValueFeature<StateFeature.State>)firstDevice.getFeatures().get(0)).getValue();
		
		@SuppressWarnings("unchecked")
		boolean allOtherDevicesAreON = !controller
				.getAllDevices()
				.stream()
				.filter(d -> !d.getName().equals(firstDevice.getName()))
				.anyMatch(d -> ((SingleValueFeature<StateFeature.State>)d.getFeatures().get(0))
						.getValue()
						.equals(StateFeature.State.OFF));
		
		assertFalse(thereIsSomeColorBasedDevicesNotRed, "All color based devices should be red");
		assertEquals(StateFeature.State.OFF, state);
		assertTrue(allOtherDevicesAreON, "Only the first device should be OFF");
	}
	



}