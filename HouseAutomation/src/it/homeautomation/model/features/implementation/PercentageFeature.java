package it.homeautomation.model.features.implementation;


import it.homeautomation.model.features.SingleValueFeature;

public abstract class PercentageFeature extends SingleValueFeature<Integer>
{

	@Override
	public Integer checkValue(Integer value)
	{
		value = (value <= 100 && value >= 0)? value : ((value > 100)? 100 : 0);
		return value;
	}

}
