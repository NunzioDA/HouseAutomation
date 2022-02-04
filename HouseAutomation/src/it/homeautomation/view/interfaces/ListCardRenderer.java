package it.homeautomation.view.interfaces;

import java.awt.Component;

/**
 * 
 * Identifies components that can be used as template to visualize
 * the elements of a list
 * 
 * @author Nunzio D'Amore
 *
 * @param <E>
 */

public interface ListCardRenderer<E>
{
	public Component getListCardRendererComponent(E d);
}
