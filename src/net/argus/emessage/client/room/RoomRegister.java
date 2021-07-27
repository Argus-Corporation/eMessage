package net.argus.emessage.client.room;

import java.util.ArrayList;
import java.util.List;

import net.argus.emessage.client.gui.utility.UserUtility;

public class RoomRegister {
	
	private static List<Room> rooms = new ArrayList<Room>();
	
	public static void addRoom(Room r) {
		if(r == null)
			return;
		
		rooms.add(r);
		UserUtility.ROOM_TAB.addRoom(r);
	}
	
	public static void removeRoom(Room r) {
		if(r == null)
			return;
		
		rooms.remove(r);
		UserUtility.ROOM_TAB.removeRoom(r);

	}
	
	public static void removeRoom(String name) {
		if(name == null)
			return;
		
		Room r = getRoom(name);
		removeRoom(r);
	}
	
	public static Room getRoom(String name) {
		if(name == null)
			return null;
		
		for(Room r : rooms)
			if(r.getName().equals(name))
				return r;
		
		return null;
			
	}
	
	public static void removeAll() {
		rooms.clear();
	}
	
	public static List<Room> getRooms() {return rooms;}

}
