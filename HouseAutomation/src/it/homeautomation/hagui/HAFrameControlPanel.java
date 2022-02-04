package it.homeautomation.hagui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class HAFrameControlPanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private JFrame frame;
	
	
	HACPButton buttonClose;
	HACPButton buttonHide;
	
	
	public HAFrameControlPanel(final HAFrame frame)
	{		
		setLayout(null);
		
		this.frame = frame;
		
		buttonHide = new HACPButton("_",HACPButton.HIDE);			
		add(buttonHide);
		
		buttonClose = new HACPButton("x",HACPButton.CLOSE);		
		add(buttonClose);

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
	
	public void setFrameTheme(Color background, Color foreground)
	{
		this.buttonHide.setButtonColor(background);
		buttonHide.setBackground(background);
		buttonHide.setForeground(foreground);
		
	}
	
	public void update()
	{
		buttonHide.setSize(getWidth()/2, getHeight());
		buttonHide.setLocation(0, 0);
		
		buttonClose.setSize(getWidth()/2, getHeight());
		buttonClose.setLocation(getWidth()/2, 0);
	}
	
	private class HACPButton extends JButton
	{
		private static final long serialVersionUID = 1L;

		public static final int CLOSE=0,HIDE=1;
		
		private Color background;
		private int type;
	
		public HACPButton(String text, int type)
		{
			super(text);
			this.type = type;
			
			//setting background and foreground color based on type
			if(type==CLOSE)
			{
				setForeground(Color.WHITE);
				background = new Color(229, 4, 4);
								
			}
			else {
				background = new Color(0xEDEEEC);
				setForeground(Color.BLACK);
			}
			
			//Init panel aspect
			setBorder(null);
			setFocusPainted(false);
			setBackground(background);
			
			addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub				
					if(HACPButton.this.type == HACPButton.CLOSE) 
						HAFrameControlPanel.this.frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
					else HAFrameControlPanel.this.frame.setState(JFrame.ICONIFIED);
				}
			});
			
			//mouse listener to change button color on mouse events
			addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub
					

				}
				
				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
					HACPButton.this.setBackground(getButtonColor());
				}
				
				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub
					

					Color mouseEnteredColor = HATools.changeColorBrightness(getButtonColor(), 25);
					HACPButton.this.setBackground(mouseEnteredColor);
				}
				
				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
			
		}
		
		public void setForeground(Color foreground)
		{
			super.setForeground(foreground);
		}
		
		public Color getButtonColor()
		{
			return background;
		}

		public void setButtonColor(Color background)
		{
			this.background = background;
		}
	}
}