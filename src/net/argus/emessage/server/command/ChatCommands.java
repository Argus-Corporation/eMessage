package net.argus.emessage.server.command;

import net.argus.net.server.command.Command;

public class ChatCommands {
	
	public static final Command MP = new MPCommand();
	public static final Command SAY = new SayCommand();
	
	public static void init() {}

}
