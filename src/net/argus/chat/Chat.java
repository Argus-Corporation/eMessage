package net.argus.chat;

import net.argus.Cardinal;
import net.argus.client.Client;
import net.argus.server.Server;

public class Chat {
	
	public static final String VERSION = "pre 1.3";
	
	public static String[] getInfo() {
		return new String[] {
				"version=" + VERSION,
				"cardinalversion=" + Cardinal.VERSION,
				"clientversion=" + Client.getVersion(),
				"serverversion=" + Server.getVersion()
		};
	}

}
