package org.voidsentinel.hexmap.view.ihm;

import java.util.logging.Logger;

import org.voidsentinel.hexmap.HexTuto;

import com.jme3.math.Vector3f;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.style.ElementId;


public class ButtonSimple extends Button {
	
	protected static final Logger LOG = Logger.getLogger(ButtonSimple.class.toString());

	public ButtonSimple(String s) {
		super(s);
	}

	public ButtonSimple(String s, String style) {
		super(s, style);
	}

	public ButtonSimple(String s, ElementId elementId) {
		super(s, elementId);
	}

	public ButtonSimple(String s, ElementId elementId, String style) {
		super(s, elementId, style);
	}

	public ButtonSimple(String s, boolean applyStyles, ElementId elementId, String style) {
		super(s, applyStyles, elementId, style);
	}

	public void setPreferredSize(float xpercent, float ypercent) {
		float xsize = HexTuto.getInstance().getSettings().getWidth() * xpercent;
		float ysize = HexTuto.getInstance().getSettings().getHeight() * ypercent;
		this.setPreferredSize(new Vector3f(xsize, ysize, 0));
	}

//	public void setPosition(int x, int y) {
//		this.setLocalTranslation(ScreenUtility.getPosition(x, y));
//	}
//
//	public void setPosition(int x, int y, int xsize, int ysize) {
//		this.setLocalTranslation(ScreenUtility.getPosition(x, y));
//		this.setPreferredSize(ScreenUtility.getSize(xsize, ysize));
//	}
//
//	public void setIcon(String file, float xsize, float ysize) {
//		IconComponent icon = new IconComponent(file);
//		Vector3f size = ScreenUtility.getSize(xsize, ysize);
//
//		float xpercent = (size.x - 8) / icon.getImageTexture().getImage().getWidth();
//		float ypercent = (size.y - 8) / icon.getImageTexture().getImage().getHeight();
//		if (ypercent < xpercent) {
//			icon.setIconScale(ypercent);
//		} else {
//			icon.setIconScale(xpercent);
//		}
//		icon.setMargin(4, 4);		
//		super.setIcon(icon);
//		
////		LOG.info(this.getName());
////		Iterator<Spatial> it = this.getChildren().iterator();
////		while(it.hasNext()){
////			LOG.info("..."+it.next().getName());
////		}
//	}

}