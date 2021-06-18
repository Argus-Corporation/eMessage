package net.argus.emessage.client;

import net.argus.emessage.client.event.ChatEvent;
import net.argus.emessage.client.event.EventChat;
import net.argus.emessage.client.gui.GUIClient;
import net.argus.emessage.client.gui.InfoDialog;
import net.argus.emessage.pack.ChatPackageType;
import net.argus.event.net.process.ProcessEvent;
import net.argus.event.net.process.ProcessListener;
import net.argus.net.pack.Package;
import net.argus.net.pack.PackageType;

public class ClientChatProcess implements ProcessListener {

	@Override
	public void nextPackage(ProcessEvent e) {
		Package pack = e.getPackage();
		
		MainClient.getEvent().startEvent(EventChat.RECEIVE_MESSAGE, new ChatEvent(pack));
		if(pack.getType().equals(ChatPackageType.MESSAGE)) {
			GUIClient.addMessage(Integer.valueOf(pack.getValue("Position")), pack.getValue("Pseudo"), pack.getValue("Message"));	
		}else if(pack.getType().equals(PackageType.INFO)) {
			InfoDialog info = new InfoDialog();
			info.addInfo(pack);
			info.setVisible(true);
		}else if(pack.getType().equals(PackageType.SYSTEM)) {
			GUIClient.addSystemMessage(pack.getValue("System-Info"));
		}
	}

}
