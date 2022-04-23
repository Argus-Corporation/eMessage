package net.argus.emessage.server;

import java.io.IOException;

import net.argus.emessage.pack.EMessagePackagePrefab;
import net.argus.emessage.pack.EMessagePackageType;
import net.argus.event.net.process.ProcessEvent;
import net.argus.event.net.process.ProcessListener;
import net.argus.net.pack.Package;
import net.argus.net.pack.PackageType;
import net.argus.net.server.ServerProcess;

public class EMessageServerProcess implements ProcessListener {

	@Override
	public void nextPackage(ProcessEvent e) {
		Package pack = e.getPackage();
		
		PackageType type = pack.getType();
		if(type.equals(EMessagePackageType.MESSAGE)) {
			message(pack, (ServerProcess) e.getProcess());
		}
	}
	
	private void message(Package pack, ServerProcess process) {
		try {
			process.sendToAll(EMessagePackagePrefab.genServerMessagePackage(pack.getValue("Message"), process.getCardinalSocket().getProfile().getName()));
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

}
