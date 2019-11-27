package org.voidsentinel.hexmap.view.ihm;

public class ToggleButton {
//extends ButtonSimple 
	
//	private boolean value = true;
//	private IToggleCallBack  callback = null;
//	private String checkedFile = ThemeInstance.getIconFile("toggle.checked");
//	private String uncheckedFile = ThemeInstance.getIconFile("toggle.unchecked");
//	
//
//	public ToggleButton(ElementId id) {
//		this(id, true, "", null);
//	}
//
//	public ToggleButton(ElementId id, String text) {
//		this(id, true, text, null);
//	}
//
//	public ToggleButton(ElementId id, String text, IToggleCallBack  callback) {
//		this(id, true, text, callback);
//	}
//	
//	/**
//	 * 
//	 * @param value
//	 * @param text
//	 */
//	public ToggleButton(ElementId id, boolean value, String text, IToggleCallBack  callback) {
//		super("", id);
//		this.callback = callback;
//		this.value = value;
//		setText(text);
//		setTextVAlignment(VAlignment.Center);
//		setTextHAlignment(HAlignment.Left);
//		if (value) {
//			setIcon(checkedFile, 1, 1);
//		} else {
//			setIcon(uncheckedFile, 1, 1);
//		}
//
//		final ToggleButton button = this;
//		addClickCommands(new Command<Button>() {
//			@Override
//			public void execute(Button source) {
//				button.value = !button.value;
//				if (button.value) {
//					setIcon(checkedFile, 1, 1);
//				} else {
//					setIcon(uncheckedFile, 1, 1);
//				}
//				if (button.callback != null){
//					button.callback.toggleOccured(button.getElementId(), button.value);
//				}
//			}
//		});
//	}
//	
//	
//	public void setIcons(String selected, String unselected){
//		checkedFile = selected;
//		uncheckedFile = unselected; 
//		if (value) {
//			setIcon(checkedFile, 1, 1);
//		} else {
//			setIcon(uncheckedFile, 1, 1);
//		}		
//	}
//
//	/**
//	 * 
//	 * @return
//	 */
//	public boolean isChecked() {
//		return value;
//	}

}
