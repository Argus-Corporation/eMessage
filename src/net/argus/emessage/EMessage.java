package net.argus.emessage;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import net.argus.Cardinal;
import net.argus.gui.Look;
import net.argus.net.client.Client;
import net.argus.net.server.Server;
import net.argus.util.Version;
import net.argus.util.debug.Debug;

public class EMessage {
	
	public static final Version VERSION = new Version("1.4.1a");
	public static final String NAME = "eMessage";
	public static final String COPYRIGHT = "© 2022 Argus";
	
	public static String[] getInfo() {
		return new String[] {
				"version=" + VERSION,
				"cardinalversion=" + Cardinal.VERSION,
				"clientversion=" + Client.VERSION,
				"serverversion=" + Server.VERSION
		};
	}
	
	public static void main(String[] args) {
		Look.chageLook(UIManager.getSystemLookAndFeelClassName());
		Debug.log("You must use the launcher to run this program");
		
		if(!Boolean.valueOf(System.getProperty("java.awt.headless")))
			JOptionPane.showMessageDialog(null, "You must use the launcher to run this program", "Error", JOptionPane.ERROR_MESSAGE);
	}

}
