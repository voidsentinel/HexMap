package org.voidsentinel.hexmap.view;

import com.jme3.math.Vector3f;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.Panel;
import com.simsilica.lemur.component.IconComponent;

public class ScreenUtility {
	private static AppSettings settings = null;
	private static float xsize = 0f;
	private static float ysize = 0f;

	private static final int BORDER = 2;
	public static final float XSIZE = 30f;
	public static final float YSIZE = 30f;

	public static void setApplicationSettings(final AppSettings appSettings) {
		settings = appSettings;
		xsize = settings.getWidth() / XSIZE;
		ysize = settings.getHeight() / YSIZE;
	}

	public static Vector3f getPosition(int x, int y) {
		return new Vector3f(x * xsize + BORDER, settings.getHeight() - y * ysize - BORDER, 0);
	}

	public static Vector3f getPosition(int x, int y, boolean border) {
		if (border){
			return new Vector3f(x * xsize + BORDER, settings.getHeight() - y * ysize - BORDER, 0);			
		} else {
			return new Vector3f(x * xsize , settings.getHeight() - y * ysize, 0);						
		}
	}

	public static Vector3f getSize(float x, float y) {
		return new Vector3f(x * xsize - BORDER * 2, y * ysize - BORDER * 2, 0f);
	}

	/**
	 * place the panel a the given position, without changing its size
	 * 
	 * @param panel
	 *            the panel to place on screen
	 * @param x
	 *            the x position of the panel
	 * @param y
	 *            the y position of the panel
	 */
	public static void setScreenPosition(Panel panel, int x, int y) {
		panel.setLocalTranslation(ScreenUtility.getPosition(x, y));
	}

	public static void setScreenPosition(Panel panel, int x, int y, int xsize, int ysize) {
		panel.setLocalTranslation(ScreenUtility.getPosition(x, y));
		panel.setPreferredSize(ScreenUtility.getSize(xsize, ysize));
	}

	public static void setScreenPosition(Panel panel, int x, int y, int xsize, int ysize, boolean border) {
		panel.setLocalTranslation(ScreenUtility.getPosition(x, y, border));
		panel.setPreferredSize(ScreenUtility.getSize(xsize, ysize));
	}
	
	public static void setScreenSize(Panel panel, int xsize, int ysize) {
		panel.setPreferredSize(ScreenUtility.getSize(xsize, ysize));
	}

	/**
	 * set the icon to a given size, in grid size
	 * 
	 * @param icon
	 * @param xsize
	 * @param ysize
	 */
	public static void setImageSize(IconComponent icon, float xsize, float ysize) {
		Vector3f size = ScreenUtility.getSize(xsize, ysize);

		float xpercent = (size.x - 8) / icon.getImageTexture().getImage().getWidth();
		float ypercent = (size.y - 8) / icon.getImageTexture().getImage().getHeight();
		if (ypercent < xpercent) {
			icon.setIconScale(ypercent);
		} else {
			icon.setIconScale(xpercent);
		}
		icon.setMargin(4, 4);
		System.out.println("icon scale " + xpercent + "/" + ypercent);
	}

}
