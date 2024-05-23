package net.argus.emessage.client.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import net.argus.emessage.EMessage;
import net.argus.emessage.client.ClientResources;
import net.argus.gui.CFrame;

public class ClientFrame extends CFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8126430449963545516L;
		
	public ClientFrame() {
		super(EMessage.NAME);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		
		getContentPane().setLayout(new BorderLayout());
		setSize(700, 700);
		setLocationRelativeTo(null);
		
		setIconImage(ClientResources.ICON.getImage());
		
		//setFrameIconImage(ClientResources.ICON);
		
		//setFrameAnimation(new FrameAnimation(this));
	}

}
