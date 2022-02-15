package it.homeautomation.view.implementation.frame;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import it.homeautomation.hagui.HAFrame;
import it.homeautomation.view.implementation.HAViewImplementation;

public abstract class DisableMainFrame extends HAFrame
{
	private static final long serialVersionUID = 1L;

	public DisableMainFrame(String title, int width, int height)
	{
		super(title, width, height);
		
		HAViewImplementation.getSingleton().addToDisablerList(this);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e)
			{
				HAViewImplementation.getSingleton().removeFromDisablerList(DisableMainFrame.this);
			}
		});
	}

	@Override
	public void dispose()
	{
		HAViewImplementation.getSingleton().removeFromDisablerList(this);
		super.dispose();
	}
}
