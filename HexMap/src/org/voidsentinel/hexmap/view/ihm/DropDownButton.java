/**
 * 
 */
package org.voidsentinel.hexmap.view.ihm;

import java.util.Iterator;
import java.util.List;

import org.voidsentinel.hexmap.HexTuto;
import org.voidsentinel.hexmap.utils.ColorParser;

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
import com.simsilica.lemur.component.IconComponent;
import com.simsilica.lemur.component.SpringGridLayout;
import com.simsilica.lemur.component.TbtQuadBackgroundComponent;
import com.simsilica.lemur.style.ElementId;

/**
 * @author guipatry
 *
 */
public class DropDownButton extends Button {

	public static float	ICONSIZE			= 24f;

	private boolean		opened			= false;
	private Container		contents			= null;
	private Button			lastSelected	= null;
	private TextField		hooverField		= null;
	private String			toolTip			= "";
	private boolean		showSelected	= true;

	final ColorRGBA		BROWN				= ColorParser.parse("#854C30");

	public DropDownButton(String text, IconComponent icon, ElementId elementId) {
		this(text, icon, elementId, true);
	}

	public DropDownButton(String text, IconComponent icon, ElementId elementId, boolean showLastSelected) {
		super(text, true, elementId, GuiGlobals.getInstance().getStyles().getDefaultStyle());
		if (icon != null)
			icon.setIconSize(new Vector2f(ICONSIZE, ICONSIZE));
		this.setIcon(icon);
		this.setTextVAlignment(VAlignment.Center);

		TbtQuadBackgroundComponent btTexture = TbtQuadBackgroundComponent.create(
				ImageRepository.datas.getData("buttonSelectedBackground").getFilename(), 1f, 5, 5, 40, 44, .1f, false);
		this.setBackground(btTexture);

		this.showSelected = showLastSelected;

		this.addClickCommands(new ToggleOpenCommand());

		contents = new Container(elementId.child("content"));
		contents.setLayout(new SpringGridLayout(Axis.Y, Axis.X, FillMode.First, FillMode.Even));
		// contents.setBackground(new QuadBackgroundComponent(new ColorRGBA(1, 0.56f,
		// 0f, 0.5f)));// chrome yellow

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

		TbtQuadBackgroundComponent btTexture = TbtQuadBackgroundComponent
				.create(ImageRepository.datas.getData("buttonBackground").getFilename(), 1f, 5, 5, 40, 44, .1f, false);
		bt.setBackground(btTexture);
		// bt.setBackground(new QuadBackgroundComponent(new ColorRGBA(0.36f, 0.54f,
		// 0.66f, 0.5f)));
		// bt.setBackground(new QuadBackgroundComponent(new
		// IconComponent(ImageRepository.datas.getData("buttonBackground").getFilename()).getImageTexture()));
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
				contents.setLocalTranslation(this.getWorldTranslation().x,
						this.getWorldTranslation().y - this.getSize().y - 2, 0);
			} else {
				HexTuto.getInstance().getGuiNode().detachChild(contents);
			}
		}
	}

	/**
	 * change the representation of the dropdonw button. does not act on the
	 * selection (ie does not perform the click command)
	 * 
	 * @param bt
	 */
	public void setSelected(int position) {
		Button bt = (Button) contents.getChild(position);
		setSelected(bt);
	}

	/**
	 * change the representation of the dropdonw button. does not act on the
	 * selection (ie does not perform the click command)
	 * 
	 * @param bt
	 */
	public void setSelected(Button bt) {
		if (showSelected) {
			// clear the currently selected
			if (lastSelected != null && bt != lastSelected) {
				TbtQuadBackgroundComponent btTexture = TbtQuadBackgroundComponent.create(
						ImageRepository.datas.getData("buttonBackground").getFilename(), 1f, 5, 5, 40, 44, .1f, false);
				lastSelected.setBackground(btTexture);
			}

			// copy source info into ddb button, if needed
			if (bt.getIcon() != null) {
				setIcon(bt.getIcon().clone());
			} else {
				setIcon(null);
			}
			setText(bt.getText());

			// change the newly selected
			TbtQuadBackgroundComponent btTexture = TbtQuadBackgroundComponent.create(
					ImageRepository.datas.getData("buttonSelectedBackground").getFilename(), 1f, 5, 5, 40, 44, .1f, false);
			bt.setBackground(btTexture);

			// remember the currenlty selected
			lastSelected = bt;
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
	 * @param hooverField
	 *           the hooverField to set
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
	 * @param toolTip
	 *           the toolTip to set
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
			System.out.println(source.getElementId().getId() + " cliqué");
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
			// change the look
			setSelected(source);
			// close the menu
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
