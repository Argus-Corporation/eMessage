package net.argus.chat.client.gui.config.profile;

public class Profile {
	
	private String name, ip;
	
	public Profile(String name, String ip) {
		this.name = name;
		this.ip = ip;
	}
	
	public String getName() {return name;}
	public String getIp() {return ip;}
	
	public void setName(String name) {this.name = name;}
	public void setIp(String ip) {this.ip = ip;}
	
	@Override
	public String toString() {
		return name;
	}

}
