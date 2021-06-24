package net.argus.emessage.client.gui;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import net.argus.event.change.ChangeListener;
import net.argus.gui.TextField;
import net.argus.system.Network;

public class TextFieldHost extends TextField {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8837992809426360794L;
	
	private Thread checker;

	public TextFieldHost(int size) {
		this(size, null);
	}
	
	public TextFieldHost(int size, String name) {
		super(size, name);
		addFocusListener(getDefaultFocusListener());
		addKeyListener(getKeyListener());
		addChangeListener(getDefaultChangeListener());
	}
	
	@Override
	public boolean isError() {
		getDefaultFocusListener().focusLost(null);
		return super.isError();
	}
	
	public FocusListener getDefaultFocusListener() {
		return new FocusListener() {
			@SuppressWarnings("deprecation")
			public void focusLost(FocusEvent e) {
				if(checker == null) {
					checker = new Thread(getCheckRunnable());
					checker.setName("host-checker");

					checker.start();
				}else {
					checker.stop();
					checker = null;
					focusLost(e);
				}
			}
			public void focusGained(FocusEvent e) {}
		};
	}
	
	public ChangeListener getDefaultChangeListener() {
		return (n) -> getDefaultFocusListener().focusLost(null);
	}
	
	private KeyListener getKeyListener() {
		return new KeyListener() {
			public void keyTyped(KeyEvent e) {}
			public void keyReleased(KeyEvent e) {getDefaultFocusListener().focusLost(null);}
			public void keyPressed(KeyEvent e) {}
		};
	}
	
	private Runnable getCheckRunnable() {
		return () -> {
			if(getText() == null || getText().equals("") || !Network.isIp(getText()))
				setError();
			else
				unError();
		};
	}

}
