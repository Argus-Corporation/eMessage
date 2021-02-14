package net.argus.chat.client.gui;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import net.argus.gui.TextField;
import net.argus.util.ArrayManager;
import net.argus.util.ChangeListener;

public class TextFieldName extends TextField {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5283450673655876616L;
	
	public TextFieldName(int size) {
		super(size);
		addFocusListener(getDefaultFocusListener());
		addChaneListener(getDefaultChangeListener());
	}
	
	@Override
	public boolean isError() {
		getDefaultChangeListener().valueChanged(null);
		return super.isError();
	}
	
	public FocusListener getDefaultFocusListener() {
		return new FocusListener() {
			public void focusLost(FocusEvent e) {
				if(!ArrayManager.isExist(getText().toCharArray(), 0))
					setError();
				else
					unError();
			}
			public void focusGained(FocusEvent e) {unError();}
		};
	}
	
	public ChangeListener getDefaultChangeListener() {
		return (n) -> getDefaultFocusListener().focusLost(null);
	}

}
