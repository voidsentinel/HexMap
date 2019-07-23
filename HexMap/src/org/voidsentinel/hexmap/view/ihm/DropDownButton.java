/**
 * 
 */
package org.voidsentinel.hexmap.view.ihm;

import org.voidsentinel.hexmap.HexTuto;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.simsilica.lemur.Axis;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.FillMode;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.TextField;
import com.simsilica.lemur.VAlignment;
import com.simsilica.lemur.component.ColoredComponent;
import com.simsilica.lemur.component.IconComponent;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import com.simsilica.lemur.component.SpringGridLayout;
import com.simsilica.lemur.style.ElementId;

/**
 * @author guipatry
 *
 */
public class DropDownButton extends Button {

	public static float	ICONSIZE			= 32f;

	private boolean		opened			= false;
	private Container		contents			= null;
	private Button			lastSelected	= null;
	private TextField		hooverField		= null;
	private String			toolTip			= "";
	

	public DropDownButton(String text, IconComponent icon, ElementId elementId) {
		super(text, true, elementId, GuiGlobals.getInstance().getStyles().getDefaultStyle());
		if (icon != null)
			icon.setIconSize(new Vector2f(ICONSIZE, ICONSIZE));
		this.setIcon(icon);
		this.setTextVAlignment(VAlignment.Center);
		this.setBackground(new QuadBackgroundComponent(new ColorRGBA(0, 0.5f, 1f, 0.5f)));// azure
		this.addClickCommands(new ToggleOpenCommand());

		contents = new Container(elementId.child("content"));
		contents.setLayout(new SpringGridLayout(Axis.Y, Axis.X, FillMode.First, FillMode.Even));
		//contents.setBackground(new QuadBackgroundComponent(new ColorRGBA(1, 0.56f, 0f, 0.5f)));// chrome yellow		
	}

	public DropDownButton(String text, String iconFile, ElementId elementId) {
		this(text, new IconComponent(iconFile), elementId);
	}

	public void addButton(String titleString, IconComponent icon, String helpString, Command<Button> command) {
		Button bt = new Button(titleString, this.getElementId().child("menuButton" + contents.getChildren().size()),
		      GuiGlobals.getInstance().getStyles().getDefaultStyle());
		if (icon != null)
			icon.setIconSize(new Vector2f(ICONSIZE, ICONSIZE));
		bt.setIcon(icon);
		bt.setTextVAlignment(VAlignment.Center);
		bt.setBackground(new QuadBackgroundComponent(new ColorRGBA(0.36f, 0.54f, 0.66f, 0.5f)));
		if (helpString != null) {
			bt.setUserData("tooltip", helpString);
			bt.addCommands(ButtonAction.HighlightOn, new HighlightOnCommand());
			bt.addCommands(ButtonAction.HighlightOff, new HighlightOffCommand());
			bt.addClickCommands(new ClickCommand(command));
		}
		contents.addChild(bt);
	}

	public void setOpen(boolean open) {
		if (open != opened) {
			opened = open;
			if (open) {
				HexTuto.getInstance().getGuiNode().attachChild(contents);
				contents.setLocalTranslation(this.getLocalTranslation().x,
				      this.getLocalTranslation().y - this.getSize().y - 2, 0);
			} else {
				HexTuto.getInstance().getGuiNode().detachChild(contents);
			}
		}
	}

	public boolean isOpen() {
		return opened;
	}

	/**
	 * @return the hooverField
	 */
	public TextField getHooverField() {
		return hooverField;
	}

	/**
	 * @param hooverField the hooverField to set
	 */
	public void setHooverField(TextField hooverField) {
		this.hooverField = hooverField;
	}

	/**
	 * @return the toolTip
	 */
	public String getToolTip() {
		return toolTip;
	}

	/**
	 * @param toolTip the toolTip to set
	 */
	public void setToolTip(String toolTip) {
		this.toolTip = toolTip;
	}

	/**
	 * 
	 * @author guipatry
	 *
	 */
	protected class ToggleOpenCommand implements Command<Button> {
		@Override
		public void execute(Button source) {
			setOpen(!isOpen());
		}
	}

	protected class ClickCommand implements Command<Button> {
		Command<Button> internal = null;

		public ClickCommand(Command<Button> initial) {
			internal = initial;
		}

		@Override
		public void execute(Button source) {
			// put old selected as correct color
			if (lastSelected != null) {
				lastSelected.setColor(ColorRGBA.White);
				if (lastSelected.getIcon() != null) {
					((ColoredComponent) lastSelected.getIcon()).setColor(ColorRGBA.White);
				}
			}
			// copy source info into ddb button
			if (source.getIcon() != null) {
				setIcon(source.getIcon().clone());
			} else {
				setIcon(null);
			}
			setText(source.getText());
			setOpen(false);
			// change it into the list (for next display)
			if (source.getIcon() != null) {
				((ColoredComponent) source.getIcon()).setColor(ColorRGBA.Yellow);
			}
			source.setColor(ColorRGBA.Yellow);
			// select the option and close the menu
			lastSelected = source;
			setOpen(false);
			// execute
			if (internal != null)
				internal.execute(source);
		}
	}

	protected class HighlightOnCommand implements Command<Button> {
		@Override
		public void execute(Button source) {
			if (hooverField != null) {
				String helpString = (String) source.getUserData("tooltip");
				hooverField.setText(helpString);
			}
		}
	}

	protected class HighlightOffCommand implements Command<Button> {
		@Override
		public void execute(Button source) {
			if (hooverField != null) {
				hooverField.setText("");
			}
		}
	}

}
