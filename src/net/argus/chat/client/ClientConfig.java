package net.argus.chat.client;

import net.argus.chat.client.gui.GUIClient;
import net.argus.system.UserSystem;

public class ClientConfig {
	
	public static int getPort() {return GUIClient.config.getInt("port");}
	public static int getPortCrypt() {return GUIClient.config.getInt("port.crypt");}
	
	public static native String getDefaultKey();
	
	public static native int getDefaultPort();
	public static native int getDefaultPortCrypt();
	
	static {
		UserSystem.loadLibrary("config");
	}

}
