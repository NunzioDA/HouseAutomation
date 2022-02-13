package it.homeautomation.model;

public class RoutineFactory
{
	public static Routine createEmptyRoutine()
	{
		return createRoutine("");
	}
	
	public static Routine createRoutine(String name)
	{
		return new Routine(name);
	}
}
