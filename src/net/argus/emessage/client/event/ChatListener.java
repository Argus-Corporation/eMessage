package net.argus.emessage.client.event;

import net.argus.util.Listener;

public interface ChatListener extends Listener {
	
	public void connect(ChatEvent e);
	
	public void disconnect(ChatEvent e);
	
	public void addMessage(ChatEvent e);
	
	public void sendMessage(ChatEvent e);
	
	public void receiveMessage(ChatEvent e);

}
