package net.argus.emessage.server.command;

import java.io.IOException;

import net.argus.emessage.pack.ChatPackagePrefab;
import net.argus.net.pack.PackagePrefab;
import net.argus.net.server.ServerProcess;
import net.argus.net.server.command.Command;
import net.argus.net.server.command.structure.KeyType;
import net.argus.net.server.command.structure.Structure;
import net.argus.net.server.command.structure.StructuredCommand;
import net.argus.util.debug.Debug;
import net.argus.util.debug.Info;

public class MPCommand extends Command {

	public MPCommand() {
		super("mp", new Structure()
				.add("user name", KeyType.STRING)
				.add("message", KeyType.STRING));
	}

	@Override
	protected void run(StructuredCommand com, ServerProcess process) throws IOException {
		String userName = com.get(0).toString();
		String message = com.get(1).toString();
		
		ServerProcess client = process.getRoom().getClientByName(userName);
		if(client == null) {
			Debug.log("User \"" + userName + "\" is not connected", Info.ERROR);
			process.send(PackagePrefab.genInfoPackage("User \"" + userName + "\" is not connected"));
			return;
		}
		
		process.send(ChatPackagePrefab.genServerMessagePackage(message, "-> " + client.getCardinalSocket().getProfile().getName(), ChatPackagePrefab.RIGHT));
		client.send(ChatPackagePrefab.genServerMessagePackage(message, "-> " + process.getCardinalSocket().getProfile().getName()));
		
	}

}
