package org.voidsentinel.hexmap.view.ihm;

import org.voidsentinel.hexmap.HexTuto;
import org.voidsentinel.hexmap.control.GameStateMap;

import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.HAlignment;
import com.simsilica.lemur.VAlignment;
import com.simsilica.lemur.component.TbtQuadBackgroundComponent;

public class ButtonBackScreen extends ButtonSimple {

	public ButtonBackScreen() {
		super("");
		addClickCommands(new Command<Button>() {
			@Override
			public void execute(Button source) {
				// and move to next screen
				GameStateMap.getInstance().moveBack();
			}
		});
//		setIcon(ThemeInstance.getIconFile("previous"), 1, 1);
		setTextVAlignment(VAlignment.Center);
		setTextHAlignment(HAlignment.Center);
		setFontSize((int) (HexTuto.getInstance().getSettings().getHeight() / 40f)*1.f);

		TbtQuadBackgroundComponent bg = TbtQuadBackgroundComponent.create("themes/standard/interface/glassPanel_cornerTR.png",
				01f, 15, 15, 85, 85, 0f, false);
		setBackground(bg);
	}

	public ButtonBackScreen (boolean icon) {
		this();
		if (icon) {
//			setIcon(ThemeInstance.getIconFile("previous"), 1, 1);
		}
	}

}
