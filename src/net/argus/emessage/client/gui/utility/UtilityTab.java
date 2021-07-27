package net.argus.emessage.client.gui.utility;

import javax.swing.Icon;
import javax.swing.JComponent;

public abstract class UtilityTab {
	
	public UtilityTab() {}
	
	public abstract JComponent getComponent();
	
	public abstract String getName();
	
	public abstract UtilityTab create();
	
	public Icon getIcon() {
		return null;
	}
	
	public String getTip() {
		return null;
	}

}
