package net.argus.emessage.client.gui;

import java.awt.BorderLayout;

import net.argus.emessage.Chat;
import net.argus.emessage.client.ClientResources;
import net.argus.file.Properties;
import net.argus.gui.Frame;
import net.argus.gui.animation.FrameAnimation;

public class ClientFrame extends Frame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8126430449963545516L;
		
	public ClientFrame(Properties config) {
		super(Chat.NAME, ClientResources.iconPath, config);
		setMainLayout(new BorderLayout());
		setLocationRelativeTo(null);
		setIcon(ClientResources.iconPath);
		setAnimation(new FrameAnimation(this));
	}

}
