package it.homeautomation.hagui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * 
 * This panel contains the buttons
 * which allow the user to close, 
 * hide, maximize the frame
 * 
 * @author Nunzio D'Amore
 *
 */

public class HAFrameControlPanel extends JPanel implements HAThemeListener{
	
	private static final long serialVersionUID = 1L;
	private HAFrame frame;	
	
	HACPButton buttonClose;
	HACPButton resizeButton;
	HACPButton buttonHide;
	
	List<HACPButton> buttonsList = new ArrayList<>();
	
	public HAFrameControlPanel(final HAFrame frame)
	{		
		setLayout(null);
		
		
		this.frame = frame;
		setBackground(HAUtilities.getBackgroundColor());
		refreshButtons();
	}
	
	public void refreshButtons()
	{
		removeAll();
		buttonsList.clear();
		
		buttonHide = new HACPButton("_", HACPButton.Type.HIDE);			
		buttonsList.add(buttonHide);		

		if(frame.isResizable())
		{
			resizeButton = new HACPButton("", HACPButton.Type.RESIZE);
			buttonsList.add(resizeButton);
		}
		
		buttonClose = new HACPButton("x", HACPButton.Type.CLOSE);		
		buttonsList.add(buttonClose);

		
		buttonsList.stream().forEach(this::add);
		reloadColors();
		update();
	}
	
	@Override
	public void setSize(int w, int h)
	{
		super.setSize(w, h);
		update();
	}
	
	@Override
	public void setLocation(int x, int y)
	{
		super.setLocation(x, y);
		update();
	}

	
	public void update()
	{
		int i = 0;

		for(HACPButton button : buttonsList)
		{
			int width = getWidth()/buttonsList.size();
			button.setSize(width, getHeight());
			button.setLocation(width * i, 0);
			i++;
		}
	}
	
	/**
	 * 
	 * Frame control panel button.
	 * It sets automatically the color and the action 
	 * to perform on the frame based on the type.
	 * 
	 * @author Nunzio D'Amore
	 *
	 */
	private class HACPButton extends HAButton
	{
		private static final long serialVersionUID = 1L;

		public enum Type
		{
			CLOSE,
			HIDE,
			RESIZE
		}
		
		private Type type;
	
		public HACPButton(String text, Type type)
		{
			super(text);
			this.type = type;
			
			//setting background and foreground color based on type
			if(type == Type.CLOSE)
			{
				setCustomColors(new Color(229, 4, 4), Color.white);								
			}
			else setCustomColors(HAUtilities.getBackgroundColor(), HAUtilities.getForegroundColor());
			
			
			addActionListener(new ActionListener() 
			{
				
				@Override
				public void actionPerformed(ActionEvent arg0) 
				{
					// Performing action based on button type
					
					switch(HACPButton.this.type)
					{
					case CLOSE:
						HAFrameControlPanel.this.frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
						HAFrameControlPanel.this.frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSED));
					break;
					
					case HIDE:
						HAFrameControlPanel.this.frame.setState(JFrame.ICONIFIED);
					break;
					
					case RESIZE:
						if(!HAFrameControlPanel.this.frame.isMaximized())
							HAFrameControlPanel.this.frame.maximize();
						else HAFrameControlPanel.this.frame.revertMaximize();
					break;
					
					}
					
				}
			});			
			
		}
		
		@Override
		public void reloadColors()
		{
			if(type == Type.CLOSE)
			{
				setCustomColors(new Color(229, 4, 4), Color.white);								
			}
			else setCustomColors(HAUtilities.getBackgroundColor(), HAUtilities.getForegroundColor());
		}
		
		@Override
		public void paint(Graphics g)
		{
			super.paint(g);

			// printing the square icon if it is the resize button
			if(type == Type.RESIZE)
			{
				int width = 10;
				int height = 10;
				g.setColor(getForeground());
				g.drawRect(getWidth()/2 - (width / 2), getHeight()/2 - (height/2), width, height);
			}
		}
	}

	@Override
	public void reloadColors()
	{
		buttonsList.stream().forEach(HACPButton::reloadColors);		
	}
}