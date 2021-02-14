package net.argus.chat.client.gui;

import java.awt.BorderLayout;

import net.argus.file.Properties;
import net.argus.gui.Frame;

public class ClientFrame extends Frame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8126430449963545516L;
	
	private static final boolean[] BUTTON = new boolean[] {true, true, true}; 
	
	public static final String ICON_PATH = GUIClient.icon32;
	public static final String TITLE = "[ARGUS] Client";

	public ClientFrame(Properties config) {
		super(TITLE, ICON_PATH, BUTTON, config);
		setMainLayout(new BorderLayout());
		setLocationRelativeTo(null);
		setIcon(GUIClient.icon16);
	}

}
