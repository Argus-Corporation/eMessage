package net.argus.chat.server;

import java.io.IOException;

import net.argus.chat.pack.ChatPackagePrefab;
import net.argus.chat.pack.ChatPackageType;
import net.argus.event.net.process.ProcessEvent;
import net.argus.event.net.process.ProcessListener;
import net.argus.net.pack.Package;
import net.argus.net.pack.PackageType;
import net.argus.net.server.ServerProcess;

public class ServerChatProcess implements ProcessListener {

	@Override
	public void nextPackage(ProcessEvent e) {
		Package pack = e.getPackage();
		
		PackageType type = pack.getType();
		if(type.equals(ChatPackageType.MESSAGE)) {
			message(pack, (ServerProcess) e.getProcess());
		}
	}
	
	private void message(Package pack, ServerProcess process) {
		try {
			process.sendToAll(ChatPackagePrefab.genServerMessagePackage(pack.getValue("Message"), process.getCardinalSocket().getProfile().getName()));
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

}
