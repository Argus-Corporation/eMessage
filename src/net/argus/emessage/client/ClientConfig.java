package net.argus.emessage.client;

import net.argus.emessage.client.gui.GUIClient;

public class ClientConfig {
	
	public static int getPort() {return GUIClient.config.getInt("port");}

}
