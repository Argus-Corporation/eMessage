package net.argus.emessage.pack;

import net.argus.net.pack.PackageType;

public class EMessagePackageType {
	
	public static final PackageType MESSAGE = new PackageType("message", 2);
	public static final PackageType ROOM = new PackageType("room", 3);
	public static final PackageType ROOM_REMOVE = new PackageType("room_rm", 4);

	
	public static void init() {}
}
