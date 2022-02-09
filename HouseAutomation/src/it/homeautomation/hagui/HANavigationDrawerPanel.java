package it.homeautomation.hagui;

import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public abstract class HANavigationDrawerPanel extends HAPanel
{
	private static final long serialVersionUID = 1L;
	
	
	private static final int TOP_MARGIN = 50;
	private static final int MARGIN = 30;
	
	private HALabel title;
	private JPanel content = new JPanel(); 
	
	public HANavigationDrawerPanel(String titleT)
	{
		title = HAUtilities.newTitle(titleT, HAUtilities.MAIN_TITLE, SwingConstants.LEFT);
		
		setLayout(null);
		

        add(title);
        add(content);        
	}
	
	@Override
	public void repaint()
	{
		if(title != null )
		{
			int width = getWidth() - MARGIN * 2;
			
			title.setSize(new Dimension(width, title.getPreferredSize().height));
			title.setLocation(MARGIN, TOP_MARGIN);
			
			int contentHeight = (int) (getHeight() - MARGIN - TOP_MARGIN - HAUtilities.MAIN_TITLE);
			
			content.setSize(new Dimension(width, contentHeight));
			content.setLocation(MARGIN, (int) (MARGIN + TOP_MARGIN + HAUtilities.MAIN_TITLE));
		}
		
		super.repaint();
	}
	
	public JPanel getContent()
	{
		return content;
	}
	
	public abstract void updateContent();
}
