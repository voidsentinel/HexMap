package org.voidsentinel.hexmap.view.ihm;

import org.voidsentinel.hexmap.HexTuto;
import org.voidsentinel.hexmap.control.GameStateMap;

import com.jme3.math.ColorRGBA;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.HAlignment;
import com.simsilica.lemur.VAlignment;
import com.simsilica.lemur.component.TbtQuadBackgroundComponent;

public class ButtonScreen extends ButtonSimple {
     private static final ColorRGBA STDCOLOR = new ColorRGBA(110f/255f, 166f/255f, 166f/255f, 1f);

	public ButtonScreen(String label, final String screenId) {
		super("");
		addClickCommands(new Command<Button>() {
			@Override
			public void execute(Button source) {
				// and move to next screen
				GameStateMap.getInstance().moveToState(screenId);
			}
		});
		
		
		
		setFontSize((int) (HexTuto.getInstance().getSettings().getHeight() / 40f)*1.f);
		setText("   "+label);
		setTextVAlignment(VAlignment.Center);
		setTextHAlignment(HAlignment.Left);
		
		this.setColor(STDCOLOR);
		setShadowColor(null);

		TbtQuadBackgroundComponent bg = TbtQuadBackgroundComponent.create("themes/standard/interface/glassPanel_cornerTR.png",
				01f, 15, 15, 85, 85, 0f, false);
		this.setBackground(bg);
		
		this.setHighlightColor(ColorRGBA.White);
				
	}

}
