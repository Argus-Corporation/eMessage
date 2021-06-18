package net.argus.emessage.client.gui.about;

import java.awt.Dimension;

import net.argus.emessage.client.gui.GUIClient;
import net.argus.gui.Panel;
import net.argus.gui.component.DialogComponent;
import net.argus.gui.gif.GIFComponent;

public class LoadingDialog extends DialogComponent {
	
	private GIFComponent gif;
	
	public LoadingDialog() {
		super(null);
		setAlwaysOnTop(true);
		setSize(400, 150);
		gif = new GIFComponent(GUIClient.load);
	}
	
	public void stop() {
		gif.stop();
	}

	@Override
	public Panel getComponent() {
		Panel pan = new Panel();
		gif.setPreferredSize(new Dimension(50, 50));
		gif.setLoop(true);
		pan.add(gif);
		
		return pan;
	}
	
	@Override
	public void show() {
		gif.start();
		super.show();
	}

}
