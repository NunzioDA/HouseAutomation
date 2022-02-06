package it.homeautomation.hagui;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.plaf.metal.MetalButtonUI;

public class HAButton extends JButton implements HAThemeListener
{
	private static final long serialVersionUID = 1L;
	private static final float BUTTON_FONT_SIZE = 15f;
	private static final int BUTTON_COLOR_BRIGHTENING = 15;
	private Color normalColor, disabled, disabledForeground = null;
	
	public HAButton(String text)
	{
		super(text);
		reloadColors();
		disabled = HAUtilities.changeColorBrightness(normalColor, -BUTTON_COLOR_BRIGHTENING);
		disabledForeground = null;

		setFont(HAUtilities.getThinFont().deriveFont(BUTTON_FONT_SIZE));
		initMouseReactionListener();
		
		setUI(new MetalButtonUI() {
		    protected Color getDisabledTextColor() {
		        Color foreground;
		        
		        if(disabledForeground != null)
				{
		        	foreground = disabledForeground;						
				}
		        else foreground = HAUtilities.getForegroundColor().darker();
		        
		        return foreground;
		    }
		});
		
		setBorder(null);
		setFocusPainted(false);
	}
	
	private void resetButtonColor()
	{
		this.setBackground(normalColor);
	}
	
	private void initMouseReactionListener()
	{
		addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseExited(MouseEvent e) {
				if(isEnabled())
					HAButton.this.resetButtonColor();
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				if(isEnabled()) {
					
					Color mouseEnteredColor = HAUtilities.changeColorBrightness(normalColor, BUTTON_COLOR_BRIGHTENING);
					HAButton.this.setBackground(mouseEnteredColor);
				}
			}
		});				
	
	}
	
	@Override
	public void setEnabled(boolean b)
	{
		// setting disabled color
		if(!b)
		{
			setBackground(disabled);
		}
		else resetButtonColor();
		
		super.setEnabled(b);		
	}
	
	public void setCustomColors(Color background, Color foreground)
	{
		this.normalColor = background;
		setBackground(normalColor);
		setForeground(foreground);
		disabled = HAUtilities.changeColorBrightness(normalColor, -BUTTON_COLOR_BRIGHTENING);
	}
	
	public void setDisabledColor(Color background, Color foreground)
	{
		disabled = background;
		disabledForeground = foreground;
	}
	
	@Override
	public int hashCode()
	{
		return getText().hashCode();
	}

	
	@Override
	public void reloadColors()
	{
		 setCustomColors(HAUtilities.getPrimaryColor(), HAUtilities.getPrimaryForegroundColor());
	}
}
