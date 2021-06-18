package net.argus.emessage.client.event;

import net.argus.net.pack.Package;

public class ChatEvent {
	
	private Package pack;
	private String message;
	private String pseudo;
	
	private boolean error;
	
	public ChatEvent(Package pack, boolean error) {
		this.pack = pack;
		this.error = error;
	}
	
	public ChatEvent(Package pack) {
		this(pack, false);
	}
	
	public ChatEvent(String message, boolean error) {
		this(message, null, error);
	}
	
	public ChatEvent(String message, String pseudo) {
		this(message, pseudo, false);
	}
	
	public ChatEvent(String message, String pseudo, boolean error) {
		this.message = message;
		this.pseudo = pseudo;
		this.error = error;
	}

	public Package getPackage() {return pack;}
	public String getMessage() {return message;}
	public String getPseudo() {return pseudo;}
	
	public boolean isError() {return error;}
	
}
