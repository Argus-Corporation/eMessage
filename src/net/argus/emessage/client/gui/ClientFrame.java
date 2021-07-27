package net.argus.emessage.client.gui;

import java.awt.BorderLayout;

import net.argus.emessage.Chat;
import net.argus.emessage.client.ClientResources;
import net.argus.gui.animation.FrameAnimation;
import net.argus.gui.frame.Frame;

public class ClientFrame extends Frame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8126430449963545516L;
		
	public ClientFrame() {
		super(Chat.NAME);
		setDefaultCloseOperation(Frame.EXIT_ON_CLOSE);		
		
		setContentPaneLayout(new BorderLayout());
		setSize(700, 700);
		setLocationRelativeTo(null);
		
		setIconImage(ClientResources.ICON.getImage());
		setFrameIconImage(ClientResources.ICON);
		
		setFrameAnimation(new FrameAnimation(this));
	}

}
