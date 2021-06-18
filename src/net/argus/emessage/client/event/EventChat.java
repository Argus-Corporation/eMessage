package net.argus.emessage.client.event;

import net.argus.event.Event;

public class EventChat extends Event<ChatListener> {
	
	public static final int CONNECT = 0;
	public static final int DISCONNECT = 1;
	public static final int ADD_MESSAGE = 2;
	public static final int SEND_MESSAGE = 3;
	public static final int RECEIVE_MESSAGE = 4;
	
	@Override
	public void event(ChatListener listener, int event, Object ... objs) {
		switch(event) {
			case CONNECT:
				listener.connect((ChatEvent) objs[0]);
				break;
			
			case DISCONNECT:
				listener.disconnect((ChatEvent) objs[0]);
				break;
				
			case ADD_MESSAGE:
				listener.addMessage((ChatEvent) objs[0]);
				break;
				
			case SEND_MESSAGE:
				listener.sendMessage((ChatEvent) objs[0]);
				break;
				
			case RECEIVE_MESSAGE:
				listener.receiveMessage((ChatEvent) objs[0]);
				break;
				
		}
	}

}
