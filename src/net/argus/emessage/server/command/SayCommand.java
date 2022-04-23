package net.argus.emessage.server.command;

import java.io.IOException;

import net.argus.emessage.pack.EMessagePackagePrefab;
import net.argus.net.server.ServerProcess;
import net.argus.net.server.command.Command;
import net.argus.net.server.command.structure.Structure;
import net.argus.net.server.command.structure.StructuredCommand;

public class SayCommand extends Command {

	public SayCommand() {
		super("say", new Structure().add("message"), 5);
	}

	@Override
	protected void run(StructuredCommand com, ServerProcess process) throws IOException {
		process.sendToAll(EMessagePackagePrefab.genServerMessagePackage((String) com.get(0), process.getCardinalSocket().getProfile().getName()));
		process.send(EMessagePackagePrefab.genServerMessagePackage((String) com.get(0), "-> all", EMessagePackagePrefab.RIGHT));
	}

}
