/**
 * 
 */
package org.voidsentinel.hexmap.view.ihm;

import org.apache.commons.lang3.StringUtils;
import org.voidsentinel.hexmap.HexTuto;
import org.voidsentinel.hexmap.repositories.FontRepository;
import org.voidsentinel.hexmap.repositories.ImageData;
import org.voidsentinel.hexmap.repositories.ImageRepository;

import com.jme3.font.BitmapFont;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.texture.Texture;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.VAlignment;
import com.simsilica.lemur.component.IconComponent;
import com.simsilica.lemur.component.InsetsComponent;
import com.simsilica.lemur.component.TbtQuadBackgroundComponent;

/**
 * @author Xerces
 *
 */
public class ThemeUtil {

	static public void setButtonTheme(Button button) {
		setButtonTheme(button, false, false, true);
	}

	static public void setButtonTheme(Button button, boolean large, boolean selected) {
		setButtonTheme(button, large, selected, true);
	}

	static public void setButtonTheme(Button button, boolean large, boolean selected, boolean filled) {
		BitmapFont buttonFont = FontRepository.datas.getData("button.font").getFont();
		float buttonFontSize = large ? 64f : 24f;
		ColorRGBA buttonColor = ColorRGBA.Black;
		ColorRGBA buttonColorFocus = ColorRGBA.Brown;
		Texture buttonTexture = null;

		if (selected) {
			buttonTexture = HexTuto.getInstance().getAssetManager()
					.loadTexture(ImageRepository.datas.getData("buttonSelectedBackground").getFilename());
		} else {
			buttonTexture = HexTuto.getInstance().getAssetManager()
					.loadTexture(ImageRepository.datas.getData("buttonBackground").getFilename());
		}

		button.setColor(buttonColor);
		button.setHighlightColor(buttonColorFocus);

		if (filled) {
			TbtQuadBackgroundComponent btTexture2 = TbtQuadBackgroundComponent.create(buttonTexture, 1f, 5, 5, 40, 44, .1f,
					false);
			button.setBackground(btTexture2);
		}
		button.setInsetsComponent(new InsetsComponent(1f, 2f, 1f, 2f));

		if (!StringUtils.isAllBlank(button.getText())) {
			button.setFont(buttonFont);
			button.setFontSize(buttonFontSize);
			button.setTextVAlignment(VAlignment.Top);
		}

	}

	static public void setButtonIcon(String iconName, Button button) {
		float size = 24f;
		setButtonIcon(iconName, size, button);
	}

	static public void setButtonIcon(String iconName, float size, Button button) {
		if (iconName != null) {
			ImageData image = ImageRepository.datas.getData(iconName);
			IconComponent icon = null;
			if (image != null) {
				String fileName = image.getFilename();
				if (fileName != null) {
					icon = new IconComponent(fileName);
					icon.setIconSize(new Vector2f(size, size));
				}
			}
			button.setIcon(icon);
		}
	}

}
