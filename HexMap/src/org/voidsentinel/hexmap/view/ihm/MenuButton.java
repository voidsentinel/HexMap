/**
 * 
 */
package org.voidsentinel.hexmap.view.ihm;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.voidsentinel.hexmap.HexTuto;
import org.voidsentinel.hexmap.repositories.ImageRepository;
import org.voidsentinel.hexmap.utils.ColorParser;
import org.voidsentinel.hexmap.view.ihm.IHMEventController.IHMAction;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.simsilica.lemur.Axis;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.FillMode;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Panel;
import com.simsilica.lemur.TextField;
import com.simsilica.lemur.VAlignment;
import com.simsilica.lemur.component.IconComponent;
import com.simsilica.lemur.component.SpringGridLayout;
import com.simsilica.lemur.component.TbtQuadBackgroundComponent;
import com.simsilica.lemur.style.ElementId;

/**
 * A Button that open a menu underIt
 * @author VoidSentinel
 *
 */
public class MenuButton extends Button implements IIHMEventListener {
	private static final Logger			LOG		= Logger.getLogger(MenuButton.class.toString());

	public static float	ICONSIZE			= 24f;

	private boolean		opened			= false;
	private Container		contents			= null;
	private Button			lastSelected	= null;
	private TextField		hooverField		= null;
	private String			toolTip			= "";
	private boolean		showSelected	= true;
	private boolean		firstLevel		= true;

	final ColorRGBA		BROWN				= ColorParser.parse("#854C30");

	/**
	 * Contructor for the Menu
	 * 
	 * @param text             the text of the menu
	 * @param icon             the icon of the menu
	 * @param elementId        the id of the menu
	 * @param showLastSelected does the selection of a subelement change the content
	 *                         of the menu (text, icon)
	 * @param firstlevel       is the menu at firstlevel(open under) or not (open at
	 *                         riht of the button)
	 */
	public MenuButton(String text, IconComponent icon, ElementId elementId, boolean showLastSelected,
	      boolean firstlevel) {
		super(text, true, elementId, GuiGlobals.getInstance().getStyles().getDefaultStyle());
		if (icon != null)
			icon.setIconSize(new Vector2f(ICONSIZE, ICONSIZE));
		this.setIcon(icon);
		this.setTextVAlignment(VAlignment.Center);

		TbtQuadBackgroundComponent btTexture = TbtQuadBackgroundComponent.create(
		      ImageRepository.datas.getData("buttonBackground").getFilename(), 1f, 5, 5, 40, 44, .1f, false);
		this.setBackground(btTexture);

		this.showSelected = showLastSelected;
		this.firstLevel = firstlevel;

		this.addClickCommands(new ToggleOpenCommand());

		contents = new Container(elementId.child("content"));
		contents.setLayout(new SpringGridLayout(Axis.Y, Axis.X, FillMode.First, FillMode.Even));
		IHMEventController.addListener(this);
	}

	/**
	 * create a menu that open a right of the menu button
	 * 
	 * @param text
	 * @param icon
	 */
	public MenuButton(String text, IconComponent icon) {
		this(text, icon, new ElementId("Menu"), true, false);
	}

	/**
	 * create a menu that open a right of the menu button
	 * 
	 * @param text
	 * @param icon
	 */
	public MenuButton(String text, String iconFile) {
		this(text, new IconComponent(iconFile), new ElementId("Menu"), true, false);
	}

	/**
	 * create a menu that open a right of the menu button
	 * 
	 * @param text
	 * @param icon
	 */
	public MenuButton(String text, IconComponent icon, ElementId elementId) {
		this(text, icon, elementId, true, false);
	}

	/**
	 * create a menu that open a right of the menu button
	 * 
	 * @param text
	 * @param icon
	 */
	public MenuButton(String text, String iconFile, ElementId elementId) {
		this(text, new IconComponent(iconFile), elementId, true, false);
	}

	/**
	 * construct a button and add it to the menu
	 * 
	 * @param titleString the title of the button
	 * @param icon        the icon
	 * @param helpString  the tooltip
	 * @param command     the command to execute when the button is pressed
	 * @return the generated button
	 */
	public Button addButton(String titleString, IconComponent icon, String helpString, Command<Button> command) {
		Button bt = new Button(titleString, this.getElementId().child("menuButton" + contents.getChildren().size()),
		      GuiGlobals.getInstance().getStyles().getDefaultStyle());
		if (icon != null)
			icon.setIconSize(new Vector2f(ICONSIZE, ICONSIZE));
		bt.setIcon(icon);
		bt.setTextVAlignment(VAlignment.Center);

		TbtQuadBackgroundComponent btTexture = TbtQuadBackgroundComponent
		      .create(ImageRepository.datas.getData("buttonBackground").getFilename(), 1f, 5, 5, 40, 44, .1f, false);
		bt.setBackground(btTexture);
		// manage the tooltip
		if (helpString != null) {
			bt.setUserData("tooltip", helpString);
			bt.addCommands(ButtonAction.HighlightOn, new HighlightOnCommand());
			bt.addCommands(ButtonAction.HighlightOff, new HighlightOffCommand());
		}

		bt.setUserData("id", bt.getElementId().getId());
		// set the cloick command, and add one to cllose the menu
		bt.addClickCommands(command);
		bt.addClickCommands(new CloseCommand());
		contents.addChild(bt);

		return bt;
	}

	/**
	 * add a button to the menu. the button will have some command added to manage
	 * closing the menu and displaying the tooltip Note that the id of the button
	 * should be a child of the menu so that it know when / if it should close it.
	 * 
	 * @param bt         the button to add
	 * @param helpString the tooltip to be displayed (null if none)
	 */
	public void addButton(Button bt, String helpString) {
		// manage tooltip
		if (helpString != null) {
			bt.setUserData("tooltip", helpString);
			bt.addCommands(ButtonAction.HighlightOn, new HighlightOnCommand());
			bt.addCommands(ButtonAction.HighlightOff, new HighlightOffCommand());
		}
		// add the close command (if the is not a menu...
		if (!MenuButton.class.isInstance(bt)) {			
			bt.addClickCommands(new CloseCommand());
		}
		contents.addChild(bt);
	}

	/**
	 * open/close the menu
	 * 
	 * @param open true if the menu is to be opened
	 */
	public void setOpen(boolean open) {
		if (open != opened) {
			LOG.info("> Setting Open "+this.getElementId().getId());
			opened = open;
			if (open) {
				HexTuto.getInstance().getGuiNode().attachChild(contents);
				if (firstLevel) {
					contents.setLocalTranslation(this.getWorldTranslation().x,
					      this.getWorldTranslation().y - this.getSize().y - 2, 0);
				} else {
					contents.setLocalTranslation(this.getWorldTranslation().x + this.getPreferredSize().x,
					      this.getWorldTranslation().y, 0);
				}

			} else {
				LOG.info("> Setting Close"+this.getElementId().getId());
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
	 * @param bt the button to select
	 */
	public void setSelected(Button bt) {
		setSelected(bt, false);
	}

	/**
	 * change the representation of the dropdonw button. does not act on the
	 * selection
	 * 
	 * @param bt the button to select
	 */
	public void setSelected(Button bt, boolean action) {
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

			// execute the action
			if (action) {
				List<Command<? super Button>> list = bt.getClickCommands();
				for (Iterator<Command<? super Button>> iterator = list.iterator(); iterator.hasNext();) {
					Command<? super Button> command = (Command<? super Button>) iterator.next();
					command.execute(bt);
				}
			}
		}
	}

	/**
	 * indicate that a even occured on another IHM object (for exemple another menu
	 * was opened). If this is not a child of this object, then close the menu
	 * 
	 * @param source the source oif the event
	 */
	@Override
	public void signalAction(Panel source, IHMAction action) {
//		LOG.info("> Receiving IHM Event "+action.toString()+ " from "+source.getElementId().getId()
//				+" to "+this.getElementId().getId());

		switch (action) {
		case MENU_OPEN:
			if (!source.getElementId().getId().startsWith(this.getElementId().getId())) {
//				LOG.info("> Closing the menu "+this.getElementId().getId());
				this.setOpen(false);
			}
			break;
		case MENU_CLOSE:
			if (source.getElementId().getId().startsWith(this.getElementId().getId())) {
//				LOG.info("> Closing the menu "+this.getElementId().getId());
				this.setOpen(false);
			}
			break;
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
		if (toolTip != null) {
			this.setUserData("tooltip", toolTip);
		} else {
			this.setUserData("tooltip", "");
		}
		this.addCommands(ButtonAction.HighlightOn, new HighlightOnCommand());
		this.addCommands(ButtonAction.HighlightOff, new HighlightOffCommand());
	}

	/**
	 * internal command class to open/cole the menu
	 * 
	 * @author voidSentinel
	 *
	 */
	protected class ToggleOpenCommand implements Command<Button> {
		@Override
		public void execute(Button source) {
			LOG.info("> Toggling "+source.getElementId().getId());
			// and act
			setOpen(!isOpen());
			// if needed signal that this menu will open
			// (no need to signal for close)
			if (isOpen()) {
				IHMEventController.signalEvent(source, IHMEventController.IHMAction.MENU_OPEN);
			} else {
				IHMEventController.signalEvent(source, IHMEventController.IHMAction.MENU_CLOSE);
			}

		}
	}

	/**
	 * This command is to be added to a button when clicking ton the button will
	 * close the menu in which it is inserted
	 * 
	 * @author guipatry
	 *
	 */
	public class CloseCommand implements Command<Button> {
		@Override
		public void execute(Button source) {
			IHMEventController.signalEvent(source, IHMEventController.IHMAction.MENU_CLOSE);
			setSelected(source);
			setOpen(false);
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
