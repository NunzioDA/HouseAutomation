package it.homeautomation.hagui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.LayoutManager;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * 
 * <p>HAFrame is the default frame class, providing a global theme by initializing the frame.
 * It also provide the mouse listener to allow window dragging since it is undecorated.
 * 
 * @author Nunzio D'Amore
 *
 */

public abstract class HAFrame extends JFrame implements HAThemeListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final int TITLE_BAR_HEIGHT = 40;
	private static final float TITLE_FONT_SIZE = 15f;
	private static final int TITLE_LEFT_MARGIN = 15;
	private static final int FRAME_CONTROLLER_PANEL_WIDTH = 135;
	
	private HALabel titleLabel;
	private JPanel content = new JPanel();
	private HAFrameControlPanel haFrameControlPanel;
	
	private boolean listeningToMouseMovement = false;
	private Point mouseStartPosition;
	
	private Dimension saveWhenMaximized = null;
	
	public HAFrame(String title, int width, int height)
	{	
		super(title);
		
		haFrameControlPanel= new HAFrameControlPanel(this);
		
		titleLabel = new HALabel(title, SwingConstants.LEFT);
		titleLabel.setFont(HATools.getRegularFont().deriveFont(TITLE_FONT_SIZE));
		
		super.add(titleLabel);
		super.add(haFrameControlPanel);
		super.add(content);
	
		
		updateTheme();
		initFrameDragListener();
		
		HATools.initFrame(this, width, height);
	}
	
	@Override
	public void setResizable(boolean resizable)
	{
		super.setResizable(resizable);
		haFrameControlPanel.refreshButtons();
	}
	
	public boolean isMaximized()
	{
		return saveWhenMaximized != null;
	}
	
	public void maximize()
	{
		if(!isMaximized())
		{
			saveWhenMaximized = getSize();
			Rectangle rec = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
			Dimension size = new Dimension(rec.width, rec.height);
			setSize(size);
			setLocation(0, 0);
		}
	}
	
	public void revertMaximize()
	{
		if(isMaximized())
		{
			setSize(saveWhenMaximized);
			saveWhenMaximized = null;
		}
	}
	
	@Override
	public Component add(Component comp)
	{
		Component component = content.add(comp);
		resizeView();
		return component;
	}
	
	public void addComponent(Component comp, Object constraints)
	{
		content.add(comp, constraints);
		resizeView();
	}
	
	public void setContentLayout(LayoutManager manager)
	{
		content.setLayout(manager);
	}
	
	/**
	 * This method deactivates and reactivates a component 
	 * so that it goes on top of all the other components.
	 * @param component
	 */
	
	private void bringComponentToTheTop(Component component)
	{
		component.setVisible(false);
		component.setVisible(true);
	}
	
	public void bringFrameControllerToTop()
	{
		bringComponentToTheTop(haFrameControlPanel);
		bringComponentToTheTop(titleLabel);
	}
	
	/**
	 * This method adapts all the components to new frame size.
	 */
	public void resizeView()
	{			
		content.setSize(getSize().width,getSize().height);		
		content.setLocation(0, 0);
		content.updateUI();
		
		resizeContent();
		
		titleLabel.setLocation(TITLE_LEFT_MARGIN,0);		
		titleLabel.setSize(getSize().width - FRAME_CONTROLLER_PANEL_WIDTH, TITLE_BAR_HEIGHT);
		titleLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		
		haFrameControlPanel.setSize(FRAME_CONTROLLER_PANEL_WIDTH, TITLE_BAR_HEIGHT-5);
		haFrameControlPanel.setLocation(getSize().width - FRAME_CONTROLLER_PANEL_WIDTH, 0);	
		
		bringFrameControllerToTop();
	}
	
	/**
	 * This method must be implemented to adapt all the components to the frame size.
	 */
	public abstract void resizeContent();
	
	/**
	 * This method sets all the components color.
	 * 
	 * @param background sets the content panel and the top bar background color.
	 * @param foreground sets the title color.
	 */
	public void updateTheme()
	{		
		Color background = HATools.getBackgroundColor();
		Color foreground = HATools.getForegroundColor();
		this.getContentPane().setBackground(background);
//		this.haFrameControlPanel.setFrameTheme(background, foreground);
		content.setBackground(background);
		titleLabel.setForeground(foreground);
		
		reloadColors();
	}
	
	
	
	/**
	 * Initialize two listeners in order to make the frame 
	 * move following the mouse when the user clicks on the top bar.
	 */
	private void initFrameDragListener()
	{
		titleLabel.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				listeningToMouseMovement = false;
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				listeningToMouseMovement = true;
				int mouseDifferenceX = MouseInfo.getPointerInfo().getLocation().x - getLocation().x;
				int mouseDifferenceY = MouseInfo.getPointerInfo().getLocation().y - getLocation().y;
				
				mouseStartPosition = new Point(mouseDifferenceX, mouseDifferenceY);
			}
			
		});
		
		titleLabel.addMouseMotionListener(new MouseMotionAdapter() {		
			@Override
			public void mouseDragged(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
				if(listeningToMouseMovement)
				{
					//if maximized
					if(isMaximized())
					{
						mouseStartPosition.x = (mouseStartPosition.x * saveWhenMaximized.width)/getSize().width;
						
						revertMaximize();
					}

					
					//getting mouse location
					int mousePositionX = MouseInfo.getPointerInfo().getLocation().x;
					int mousePositionY = MouseInfo.getPointerInfo().getLocation().y;
										
					//setting new frame location following the mouse
					setLocation(mousePositionX - mouseStartPosition.x, mousePositionY- mouseStartPosition.y);
					//haFrameControlPanel.updateUI();
				}
			}
		});
	}
}
