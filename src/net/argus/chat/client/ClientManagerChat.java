package net.argus.chat.client;

import net.argus.chat.client.event.ChatEvent;
import net.argus.chat.client.event.EventChat;
import net.argus.chat.client.gui.GUIClient;
import net.argus.client.Client;
import net.argus.client.ClientManager;
import net.argus.client.ProcessClient;
import net.argus.exception.SecurityException;
import net.argus.util.pack.Package;

public class ClientManagerChat implements ClientManager {
	
	public Client client;
	
	public ClientManagerChat(ChatClient client) {
		this.client = client.getClient();
	}
	
	@Override
	public void receivePackage(Package pack, ProcessClient thisObj) throws SecurityException {
		MainClient.getEvent().startEvent(EventChat.RECEIVE_MESSAGE, new ChatEvent(pack));
	}
	
	@Override
	public void disconnected(String msg) throws SecurityException {
		String[] value = new String[2];
		value[0] = "SYSTEM-ALERT";
		value[1] = msg;
		client.getProcessListeners().get(0).addSystemMessage(value);
		
		GUIClient.leave();
	}

}
