package net.argus.emessage.client.event;

import net.argus.beta.com.pack.Package;

public class ChatEvent {
	
	private Package pack;
	private String message;
	private String pseudo;
	
	private int pos = -1;
	
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
	
	public ChatEvent(String message, String pseudo, int pos) {
		this(message, pseudo, pos, false);
	}
	
	public ChatEvent(String message, String pseudo, boolean error) {
		this(message, pseudo, -1, error);
	}
	
	public ChatEvent(String message, String pseudo, int pos, boolean error) {
		this.message = message;
		this.pseudo = pseudo;
		this.error = error;
		this.pos = pos;
	}

	public Package getPackage() {return pack;}
	public String getMessage() {return message;}
	public String getPseudo() {return pseudo;}
	
	public int getPos() {return pos;}
	
	public boolean isError() {return error;}
	
}
