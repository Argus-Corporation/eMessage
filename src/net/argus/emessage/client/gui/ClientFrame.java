package net.argus.emessage.client.gui;

import java.awt.BorderLayout;

import net.argus.emessage.Chat;
import net.argus.file.Properties;
import net.argus.gui.Frame;
import net.argus.gui.animation.FrameAnimation;

public class ClientFrame extends Frame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8126430449963545516L;
		
	public ClientFrame(Properties config) {
		super(Chat.NAME, GUIClient.iconPath, config);
		setMainLayout(new BorderLayout());
		setLocationRelativeTo(null);
		setIcon(GUIClient.iconPath);
		setAnimation(new FrameAnimation(this));
	}

}
