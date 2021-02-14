package net.argus.chat.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.SocketException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

import net.argus.annotation.Cardinal;
import net.argus.chat.client.event.ChatEvent;
import net.argus.chat.client.event.ChatListener;
import net.argus.chat.client.event.EventChat;
import net.argus.chat.client.gui.Connect;
import net.argus.chat.client.gui.GUIClient;
import net.argus.event.socket.SocketEvent;
import net.argus.event.socket.SocketListener;
import net.argus.file.css.CSSEngine;
import net.argus.gui.FrameListener;
import net.argus.gui.TextField;
import net.argus.plugin.InitializationPlugin;
import net.argus.plugin.PluginEvent;
import net.argus.plugin.PluginRegister;
import net.argus.security.Key;
import net.argus.system.CopyTemp;
import net.argus.system.InitializationSplash;
import net.argus.system.InitializationSystem;
import net.argus.util.ArrayManager;
import net.argus.util.Display;
import net.argus.util.ThreadManager;
import net.argus.util.debug.Debug;
import net.argus.util.pack.Package;
import net.argus.util.pack.PackageBuilder;
import net.argus.util.pack.PackageType;

@Cardinal
public class MainClient {
	
	private static ChatClient client;
	
	private static EventChat event = new EventChat();
	
	public static void init() {
		GUIClient.addFastAction(getFastActionListener());
		GUIClient.addJoinAction(getJoinActionListener());
		GUIClient.addLeaveAction(getLeaveActionListener());
		
		GUIClient.addPreferenceAction(getPreferenceActionListener());
		
		GUIClient.addSendAction(getSendActionListener());
		
		GUIClient.addFrameListener(getFrameListener());
		
		PluginRegister.init(new PluginEvent(MainClient.class));
		
		InitializationSplash.getSplash().exit();
		while(!InitializationSplash.getSplash().isFinnish())
			GUIClient.setVisible(false);
			
		GUIClient.setVisible(true);
		
		PluginRegister.postInit(new PluginEvent(MainClient.class));
		
	}
	
	public static ActionListener getFastActionListener() {
		return (ActionEvent e) -> new Connect(true).start();
	}
	
	public static ActionListener getJoinActionListener() {
		return (ActionEvent e) -> new Connect(false).start();
	}
	
	public static ActionListener getLeaveActionListener() {
		return (ActionEvent e) -> client.logOut();
	}
	
	public static ActionListener getPreferenceActionListener() {
		return (ActionEvent e) -> GUIClient.configFrame.show();
	}
	
	public static ActionListener getSendActionListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TextField field = GUIClient.panChat.getTextField();
				String msg = field.getText();
				
				if(client != null && client.isConnected() && ArrayManager.isExist(msg.toCharArray(), 0)) {
					if(msg.toCharArray()[0] == '/')
						client.sendPackage(new Package(new PackageBuilder(PackageType.COMMANDE.getId()).addValue("command", msg)));
					else
						client.sendPackage(new Package(new PackageBuilder(PackageType.MESSAGE.getId()).addValue("message", msg)));
				
					GUIClient.addMessage(new String[] {"Vous", msg});
				
					field.copyData();
					field.setText("");
				}
			}
		};
	
	}
	
	public static FrameListener getFrameListener() {
		return new FrameListener() {
			public void frameResizing() {}
			@Override
			@SuppressWarnings("deprecation")
			public void frameClosing() {
				if(client != null && client.isConnected()) {
					client.sendPackage(new Package(new PackageBuilder(PackageType.LOG_OUT.getId()).addValue("message", "Frame Closing")));
					
					client.getProcessClient().stop();
				}
			}	
			public void frameMinimalized() {}
		};
	}
	
	public static SocketListener getSocketListener() {
		return new SocketListener() {
			public void errorConnection(SocketEvent e) {
				GUIClient.leave();
				
				String errmsg = "";
				if(e.getObject() instanceof SocketException) errmsg = "timeout";
				else if(e.getObject() instanceof UnknownHostException) errmsg = "unknownhost";
				
				event.startEvent(EventChat.DISCONNECT, new ChatEvent(errmsg.equals("")?e.getObject().toString():errmsg, true));
			}
			public void disconnect(SocketEvent e) {
				event.startEvent(EventChat.DISCONNECT, new ChatEvent("disconnected"));
			}
			public void connect(SocketEvent e) {
				event.startEvent(EventChat.CONNECT, new ChatEvent("connected"));
				
				GUIClient.connect();
			}
		};
	}
	
	
	/**--EVENT--**/
	public static void addChatListener(ChatListener listener) {event.addListener(listener);}
	
	public static EventChat getEvent() {return event;}
	
	
	/**----**/
	
	public static void connect(String host, String pseudo, String password) {
		if(GUIClient.menuBar.isEncrypt())
			client = new ChatClient(host, ClientConfig.getPortCrypt(), pseudo, password, new Key(ClientConfig.getDefaultKey()));
		else
			client = new ChatClient(host, ClientConfig.getPort(), pseudo, password);
		
		client.addSocketListener(getSocketListener());
		client.addClientManager(new ClientManagerChat(client));
		client.addProcessListener(new ProcessListenerChat());
		
		try {client.start();}
		catch(IOException e) {}
		
	}
	
	public static void main(String[] args) throws IOException, URISyntaxException {
		InitializationSystem.initSystem(args, true, new InitializationSplash("res/logo.png", Display.getWidhtDisplay() - 50, 0));
		InitializationPlugin.register();
		
		Thread.currentThread().setName("client");
		CSSEngine.run("client", "bin/css");
		
		CopyTemp copy = new CopyTemp();
		copy.copy("*.gif");
		
		Debug.addBlackList(ThreadManager.THREAD_MANAGER);

		PluginRegister.preInit(new PluginEvent(MainClient.class));

		MainClient.init();
		
	}
	
}
