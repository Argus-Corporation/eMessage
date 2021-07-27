package net.argus.emessage.client.room;

public class Room {
	
	private String name;
	private boolean privateRoom;
	
	public Room(String name, boolean privateRoom) {
		this.name = name;
		this.privateRoom = privateRoom;
	}
	
	public String getName() {return name;}
	
	public boolean isPrivate() {return privateRoom;}
	
	@Override
	public String toString() {
		return name;
	}

}
