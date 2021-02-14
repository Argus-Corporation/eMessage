package net.argus.chat.client.event;

public class ChatEvent {
	
	private String message;
	
	private boolean error;
	
	public ChatEvent(String message) {
		this(message, false);
	}
	
	public ChatEvent(String message, boolean error) {
		this.message = message;
		this.error = error;
	}

	public String getMessage() {return message;}
	
	public boolean isError() {return error;}
	
}
