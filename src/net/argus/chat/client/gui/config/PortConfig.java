package net.argus.chat.client.gui.config;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.BoxLayout;

import net.argus.chat.client.ClientConfig;
import net.argus.chat.client.gui.GUIClient;
import net.argus.chat.client.gui.TextFieldPort;
import net.argus.gui.Button;
import net.argus.gui.Label;
import net.argus.gui.Panel;

public class PortConfig extends ConfigManager {
	
	private TextFieldPort port;
	private TextFieldPort portCrypt;
	
	private Button apply;
	
	public static final int ID = 1;

	public PortConfig() {super(ID);}

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
		Label text = new Label("Port", false);
		
		port = new TextFieldPort(10);
		port.setText(GUIClient.config.getString("port"));
		port.addKeyListener(getChangerKeyListener());
		panPort.add(text);
		panPort.add(port);
		
		//--------------------Crypt------------------------\\
		Panel panPortCrypt = new Panel();
		Label textCrypt = new Label("Port crypter", false);

		
		portCrypt = new TextFieldPort(10);
		portCrypt.setText(GUIClient.config.getString("port.crypt"));
		portCrypt.addKeyListener(getChangerKeyListener());
		panPortCrypt.add(textCrypt);
		panPortCrypt.add(portCrypt);
		
		pan.add(panPort);
		pan.add(panPortCrypt);
		
		return pan;
	}
	
	public Panel getSouthPanel() {
		Panel pan = new Panel();
		
		Button butDefault = new Button("default");
		apply = new Button("apply");
		
		butDefault.addActionListener(getDefaultActionListener());
		apply.addActionListener(getApplyActionListener());
		
		apply.setEnabled(false);
		
		pan.add(butDefault);
		pan.add(apply);
		return pan;
	}
	
	private ActionListener getApplyActionListener() {return (e) -> apply();}
	
	private ActionListener getDefaultActionListener() {
		return (e) -> {
			port.setText(Integer.toString(ClientConfig.getDefaultPort()));
			portCrypt.setText(Integer.toString(ClientConfig.getDefaultPortCrypt()));
			
			getChangerKeyListener().keyPressed(null);
		};
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
		if(!port.isError() && !portCrypt.isError())
			try {
				GUIClient.config.setKey("port", port.getText());
				GUIClient.config.setKey("port.crypt", portCrypt.getText());
				
				apply.setEnabled(false);
				return VALID_APPLY;
			}catch(IOException e) {e.printStackTrace();}
		
		return ERROR_APPLY;
		
	}
	
}
