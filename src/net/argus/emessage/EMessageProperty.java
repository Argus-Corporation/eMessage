package net.argus.emessage;

import java.util.HashMap;
import java.util.Map;

public class EMessageProperty {
	
	private static Map<String, Object> property = new HashMap<String, Object>();
	
	public static void put(String key, Object value) {property.put(key, value);}
	public static Object get(String key) {return property.get(key);}
	public static int getInt(String key) {return (int) property.get(key);}
	
	static {
		put("DefaultPort", 11066);
		put("AlternativePort", 8375);
		put("DefaultDebugPort", 11036);
		put("DefaultMainRoomSize", 100);
		put("DefaultSystemName", "System");
	}

}
