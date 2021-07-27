package net.argus.emessage.client.gui.config;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.BoxLayout;

import net.argus.emessage.EMessageProperty;
import net.argus.emessage.client.ClientResources;
import net.argus.emessage.client.MainClient;
import net.argus.emessage.client.gui.TextFieldPort;
import net.argus.gui.Panel;

public class PortConfig extends ConfigManager {
	
	private TextFieldPort port;
		
	public static final int ID = 1;

	public PortConfig() {super(ID, MainClient.getClientInstance());}

	@Override
	public Panel getConfigPanel() {
		Panel pan = new Panel();
		pan.setLayout(new BorderLayout());
		
		pan.add(BorderLayout.CENTER, getCenterPanel());
		pan.add(BorderLayout.SOUTH, getSouthPanel());
		
		return pan;
		
	}
	
	public Panel getCenterPanel() {
		Panel pan = new Panel();
		pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));
		
		Panel panPort = new Panel();
		
		port = new TextFieldPort(10, "port");
		port.setText(ClientResources.CONFIG.getString("port"));
		port.addKeyListener(getChangerKeyListener());
		
		panPort.add(port);
		
		pan.add(panPort);
		
		return pan;
	}
	
	public Panel getSouthPanel() {
		Panel pan = new Panel();
		
		apply.setEnabled(false);
		
		pan.add(def);
		pan.add(apply);
		return pan;
	}
	
	private KeyListener getChangerKeyListener() {
		return new KeyListener() {
			public void keyTyped(KeyEvent e) {}
			public void keyReleased(KeyEvent e) {}
			public void keyPressed(KeyEvent e) {
				apply.setEnabled(true);
			}
		};
	}

	@Override
	public int apply() {
		if(!port.isError())
			try {
				ClientResources.CONFIG.setKey("port", port.getText());
				
				apply.setEnabled(false);
				return VALID_APPLY;
			}catch(IOException e) {e.printStackTrace();}
		
		return ERROR_APPLY;
		
	}

	@Override
	public void setDefault() {
		port.setText(Integer.toString((int) EMessageProperty.get("DefaultPort")));
		
		getChangerKeyListener().keyPressed(null);
		
	}
}
