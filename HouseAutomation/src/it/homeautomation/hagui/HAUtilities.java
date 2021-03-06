package it.homeautomation.hagui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.SwingConstants;


public class HAUtilities
{
	public static final float MAIN_TITLE = 50;
	public static final float MIDDLE_TITLE = 30;
	public static final float LAST_TITLE = 18;
	
	public static final String DEFAULT_ICONS_PATH = "/icons/";
	public static final String DEFAULT_ICONS_EXTENSION = ".png";
	
	private static final String FONT_PATH = "/fonts/";
	private static final String THIN_FONT_NAME = "regular.ttf";//"thin.ttf";
	private static final String REGULAR_FONT_NAME = "regular.ttf";
	private static final String LIGHT_FONT_NAME = "light.ttf";
	
	private static final int LIGHT_BACKGROUND_COLOR_OFFSET = 10;
	private static final int DARK_BACKGROUND_COLOR_OFFSET = -15;
	private static final int SHADOW_COLOR_OFFSET = -20;
		
	private static final Color BACKGROUND_COLOR_DEFAULT = new Color(0x353d44);
	private static final Color FOREGROUND_COLOR_DEFAULT = new Color(0xeeeeee);
	private static final Color PRIMARY_COLOR_DEFAULT = new Color(0x742896);
	private static final Color PRIMARY_FOREGROUND_COLOR_DEFAULT = new Color(0xffffff);
	private static final Color SECONDARY_COLOR_DEFAULT = new Color(0x742896);
	
	private static Color BACKGROUND_COLOR = null;
	private static Color FOREGROUND_COLOR = null;
	private static Color PRIMARY_COLOR = null;
	private static Color PRIMARY_FOREGROUND_COLOR = null;
	private static Color SECONDARY_COLOR = null;
	
	private static Font normalFont = null, thinFont = null, lightFont = null;
	
	public static URL getIconPath(String iconID)
	{
		return HAUtilities.class.getResource(HAUtilities.DEFAULT_ICONS_PATH + iconID + HAUtilities.DEFAULT_ICONS_EXTENSION);
	}
	
	public static void setTheme(Color background, Color foreground, Color primary, Color primaryForeground, Color secondary)
	{
		BACKGROUND_COLOR = background;
		FOREGROUND_COLOR = foreground;
		PRIMARY_FOREGROUND_COLOR = primaryForeground;
		PRIMARY_COLOR = primary;
		SECONDARY_COLOR = secondary;
	}
	
	
	public static void resetTheme()
	{
		BACKGROUND_COLOR = null;
		FOREGROUND_COLOR = null;
		PRIMARY_FOREGROUND_COLOR = null;
		PRIMARY_COLOR = null;
		SECONDARY_COLOR = null;
	}
	
	public static void initFrame(HAFrame frame, int width, int height)
	{
		setResizeListener(frame);
		
		frame.setSize(width, height);
		frame.setLocation(200, 200);
		frame.setLayout(null);
		frame.setUndecorated(true);
		frame.getRootPane().setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, HAUtilities.getDarkBackgroundColor()));
		frame.centerInScreen();
	}
	
	private static void setResizeListener(HAFrame frame)
	{
		frame.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) 
			{
				frame.resizeView();
			}
		});
	}
	
	private static Font initFont(String name)
	{
		String path = FONT_PATH + name;
		
		Font font = null;
		
		try {
		     font = Font.createFont(Font.TRUETYPE_FONT, HAUtilities.class.getResourceAsStream(path));
		     
		     GraphicsEnvironment ge = 
		             GraphicsEnvironment.getLocalGraphicsEnvironment();
		     
		     ge.registerFont(font);
		         
		} catch (IOException|FontFormatException e) {
		     System.err.print("Font "+name+" not found");
		}
		
		return font;
	}
	
	public static String capitalize(String str)
	{
		String result = "";
		
		if(str != null && !str.isEmpty())
		{
			StringBuffer strBf = new StringBuffer(str);
			strBf.setCharAt(0, Character.toUpperCase(str.charAt(0)));
			str = strBf.toString();
			
			int spaceIndex = str.indexOf(" ");
			spaceIndex = (spaceIndex == -1)? str.length() : spaceIndex;
			result = str.substring(0, spaceIndex);
			
			if(spaceIndex != str.length())
				result += " "+capitalize(str.substring(spaceIndex + 1));
		}
		
		return result;
	}
		
	public static Color changeColorBrightness(Color color, int offset)
	{
		Color newColor = null;
		
		if(color != null)
		{
			int newRed = color.getRed() + offset;
			int newGreen = color.getGreen() + offset;
			int newBlue = color.getBlue() + offset;
			
			int red, green, blue;
	
			red   = (newRed > 255)?  255 : (newRed < 0)? 0 : newRed;
			green = (newGreen > 255)?  255 : (newGreen < 0)? 0 : newGreen;
			blue  = (newBlue > 255)?  255 : (newBlue < 0)? 0 : newBlue;
		
			newColor = new Color(red, green, blue);
		}
		
		return newColor;
	}
	
	
	public static String spaceMarginString(int n)
	{
		char[] chars = new char[n];
	    Arrays.fill(chars, ' ');
	    return new String(chars);
	}
	
	public static Font getRegularFont()
	{
		if(normalFont == null)
			normalFont = initFont(REGULAR_FONT_NAME);
		
		return normalFont;
	}
	
	public static Font getLightFont()
	{
		if(lightFont == null)
			lightFont = initFont(LIGHT_FONT_NAME);
		
		return lightFont;
	}
	
	public static Font getThinFont()
	{
		if(thinFont == null)
			thinFont = initFont(THIN_FONT_NAME);
		
		return thinFont;
	}
	
	public static Color getShadowColor()
	{
		return changeColorBrightness(getBackgroundColor(), SHADOW_COLOR_OFFSET);
	}
	

	
	public static HALabel newTitle(String description, float fontSize)
	{
		return newTitle(description, fontSize, SwingConstants.LEFT);
	}
	
	public static HALabel newTitle(String description, float fontSize, int orientation)
	{
		HALabel label = new HALabel(capitalize(description), orientation);
		
		
		Font font = HAUtilities.getRegularFont();
		
		if(fontSize == MIDDLE_TITLE || fontSize == LAST_TITLE)
			font = getLightFont();
		
		label.setFont(font.deriveFont(fontSize));
		return label; 
	}
	
	public static HALabel newDescription(String description)
	{
		return new HALabel(description, SwingConstants.LEFT); 
	}
	
	public static Color getForegroundColor()
	{
		Color result = FOREGROUND_COLOR;
		
		if(result == null)
			result = FOREGROUND_COLOR_DEFAULT;
		
		return result;
	}
	
	public static Color getSecondaryColor()
	{
		Color result = SECONDARY_COLOR;
		
		if(result == null)
			result = SECONDARY_COLOR_DEFAULT;
		
		return result;
	}
	
	public static Color getBackgroundColor()
	{
		Color result = BACKGROUND_COLOR;
		
		if(result == null)
			result = BACKGROUND_COLOR_DEFAULT;
			
		return result;
	}
	
	public static Color getPrimaryForegroundColor()
	{
		Color result = PRIMARY_FOREGROUND_COLOR;
		
		if(result == null)
			result = PRIMARY_FOREGROUND_COLOR_DEFAULT;
		
		return result;
	}
	
	public static Color getPrimaryColor()
	{
		Color result = PRIMARY_COLOR;
		
		if(result == null)
			result = PRIMARY_COLOR_DEFAULT;
		
		return result;
	}

	public static Color getDarkBackgroundColor()
	{
		return changeColorBrightness(getBackgroundColor(), DARK_BACKGROUND_COLOR_OFFSET);
	}
	
	public static Color getLightBackgroundColor()
	{
		return changeColorBrightness(getBackgroundColor(), LIGHT_BACKGROUND_COLOR_OFFSET);
	}
	
	


}
