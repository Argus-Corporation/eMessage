package net.argus.emessage;

import java.io.IOException;

import net.argus.emessage.client.ClientResources;

public class ChatDefault{
	
	public static boolean openUtilityOnConnection() {
		return isTrue("open.utility", "true");
	}
	
	public static boolean confirmConnectRoom() {
		return isTrue("join.confirm", "true");
	}
	
	private static boolean isTrue(String key, String defaultValue) {
		if(!ClientResources.CONFIG.containsKey(key))
			try {ClientResources.CONFIG.setKey(key, defaultValue);}
			catch(IOException e) {}
		
		return ClientResources.getBooleanConfig(key);
	}

}
