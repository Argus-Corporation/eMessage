package net.argus.chat.client;

import java.awt.TrayIcon.MessageType;

import net.argus.chat.client.gui.GUIClient;
import net.argus.client.ProcessListener;
import net.argus.util.Notification;

public class ProcessListenerChat implements ProcessListener {

	@Override
	public void addMessage(String[] value) {
		GUIClient.addMessage(value);
		
	}
	
	@Override
	public void addSystemMessage(String[] value) {
		addMessage(value);
		if(!GUIClient.getFrame().isActive())
			Notification.showNotification("Vous avez un nouveau message de " + value[0], value[1], "Argus", MessageType.NONE, GUIClient.icon16);

	}

}
