package net.argus.emessage.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import net.argus.emessage.EMessage;
import net.argus.emessage.client.event.ChatEvent;
import net.argus.emessage.client.event.ChatListener;
import net.argus.emessage.client.event.EventChat;
import net.argus.emessage.client.gui.Connect;
import net.argus.emessage.client.gui.EMessagePanelClient;
import net.argus.emessage.client.gui.GUIClient;
import net.argus.emessage.pack.EMessagePackageType;
import net.argus.event.gui.frame.FrameEvent;
import net.argus.event.gui.frame.FrameListener;
import net.argus.event.net.socket.SocketEvent;
import net.argus.event.net.socket.SocketListener;
import net.argus.exception.InstanceException;
import net.argus.file.FileManager;
import net.argus.file.css.CSSEngine;
import net.argus.gui.OptionPane;
import net.argus.gui.TextField;
import net.argus.instance.CardinalProgram;
import net.argus.instance.Instance;
import net.argus.instance.Program;
import net.argus.lang.Lang;
import net.argus.lang.LangRegister;
import net.argus.net.client.Client;
import net.argus.plugin.InitializationPlugin;
import net.argus.plugin.PluginEvent;
import net.argus.plugin.PluginRegister;
import net.argus.system.InitializationSplash;
import net.argus.system.InitializationSystem;
import net.argus.system.UserSystem;
import net.argus.util.ArrayManager;
import net.argus.util.Display;
import net.argus.util.Error;
import net.argus.util.FontManager;
import net.argus.util.ThreadManager;
import net.argus.util.debug.Debug;

@Program(instanceName = "client")
public class MainClient extends CardinalProgram {
	
	private static EMessageClient client;
	
	private static EventChat event = new EventChat();
	
	private static Instance instanceClient;
	
	public static void init() {
		EMessagePackageType.init();
		ClientResources.init();
		GUIClient.init();

		GUIClient.addFastAction(getFastActionListener());
		GUIClient.addJoinAction(getJoinActionListener());
		GUIClient.addLeaveAction(getLeaveActionListener());
		
		GUIClient.addPreferenceAction(getPreferenceActionListener());
		GUIClient.addUtilityAction(getUtilityActionListener());
		
		GUIClient.addAboutAction(getAboutActionListener());
		
		GUIClient.addSendAction(getSendActionListener());
		
		GUIClient.addFrameListener(getFrameListener());
		
		addChatListener(getChatListener());
		
		PluginRegister.init(new PluginEvent(MainClient.class));
				
		InitializationSplash.getSplash().exit();

		while(!InitializationSplash.getSplash().isFinnish())
			GUIClient.setVisible(false);
			
		LangRegister.update();

		GUIClient.setVisible(true);
		PluginRegister.postInit(new PluginEvent(MainClient.class));
		
	}
	
	public static ActionListener getFastActionListener() {
		return (ActionEvent e) -> Instance.startThread(new Connect(true), instanceClient);
	}
	
	public static ActionListener getJoinActionListener() {
		return (ActionEvent e) -> Instance.startThread(new Connect(false), instanceClient);
	}
	
	public static ActionListener getLeaveActionListener() {
		return (ActionEvent e) -> {Instance.setThreadInstance(instanceClient); client.logOut("leave");};
	}
	
	public static ActionListener getPreferenceActionListener() {
		return (ActionEvent e) -> {Instance.setThreadInstance(instanceClient); GUIClient.getConfigWindow().show();};
	}
	
	public static ActionListener getUtilityActionListener() {
		return (ActionEvent e) -> {
			Instance.setThreadInstance(instanceClient);
			GUIClient.UTILITY.show();
		};
	}
	
	public static ActionListener getAboutActionListener() {
		return (ActionEvent e) -> {Instance.setThreadInstance(instanceClient); GUIClient.ABOUT.show();};
	}
	
	public static ActionListener getSendActionListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TextField field = GUIClient.CHAT_PANEL.getTextField();
				String msg = field.getText();
				
				if(client != null && client.isConnected() && ArrayManager.isExist(msg.toCharArray(), 0)) {
					boolean com = msg.toCharArray()[0] == '/';
					if(com)
						client.send(msg);
					else
						client.send(msg);
					
					event.startEvent(EventChat.SEND_MESSAGE, new ChatEvent(msg, null));
					
					if(!com)
						GUIClient.addMessage(msg);
				
					field.copyData();
					field.setText("");
				}
				
				
			}
		};
	
	}
	
	public static FrameListener getFrameListener() {
		return new FrameListener() {
			public void frameResizing(FrameEvent e) {}

			@Override
			public void frameClosing(FrameEvent e) {
				if(client != null && client.isConnected()) {
					client.logOut("Frame Closing");
					
					//client.getClientProcess().close();
				}
			}	
			public void frameMinimalized(FrameEvent e) {}
		};
	}
	
	public static SocketListener getSocketListener() {
		return new SocketListener() {

			@Override
			public void connect(SocketEvent e) {
				event.startEvent(EventChat.CONNECT, new ChatEvent(e.getArgument(), null));
				
				GUIClient.connect();
			}

			@Override
			public void disconnect(SocketEvent e) {
				GUIClient.leave();
				
				event.startEvent(EventChat.DISCONNECT, new ChatEvent(e.getArgument(), null));
			}

			@Override
			public void connectionRefused(SocketEvent e) {
				GUIClient.leave();
				event.startEvent(EventChat.DISCONNECT, new ChatEvent(e.getArgument(), true));
			}
		};
	}
	
	public static ChatListener getChatListener() {
		return new ChatListener() {
			
			@Override
			public void sendMessage(ChatEvent e) {
				if(!e.getMessage().startsWith("/"))
					ClientResources.SEND_MESSAGE.play();				
			}
			
			@Override
			public void receiveMessage(ChatEvent e) {
			}
			
			@Override
			public void disconnect(ChatEvent e) {
				if(e.isError()) {
					ClientResources.ERROR.play();
					GUIClient.addSystemMessage(("You could not connect") + (e.getMessage()!=null?": " + e.getMessage():""));
				}else
					GUIClient.addSystemMessage(("You are disconnected") + (e.getMessage()!=null?": " + e.getMessage():""));
				
			}
			
			@Override
			public void connect(ChatEvent e) {
				ClientResources.VALID.play();
				
				GUIClient.clearMessage();
				GUIClient.addSystemMessage("You are connected");
			}
			
			@Override
			public void addMessage(ChatEvent e) {
				if(e.getPos() == EMessagePanelClient.YOU)
					ClientResources.NEW_MESSAGE.play();
			}
		};
	}
	
	public static Instance getClientInstance() {return instanceClient;}
	
	
	/**--EVENT--**/
	public static void addChatListener(ChatListener listener) {event.addListener(listener);}
	public static void removeChatListener(ChatListener listener) {event.removeListener(listener);}
	
	public static EventChat getEvent() {return event;}
	
	
	/**----**/
	public static void connect(String host, String pseudo, String password) {
		try {
			client = new EMessageClient(host, ClientResources.CONFIG.getInt("port"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		/*client.addSocketListener(getSocketListener());
		client.addProcessListener(new EMessageClientProcess());
*/
		try {client.connect(pseudo, password);}
		catch(IOException e) {}
	}
	
	public static void joinRoom(String roomName, String password) throws IOException {
		String command = "/joinroom \"" + roomName + "\"";
		if(password != null)
			command += " \"" + password + "\"";
		
		client.send(command);
	}
	
	public static void stop() {UserSystem.exit(0);}
	
	public void main(String[] args) throws InstanceException {
		try {
			InitializationSystem.initSystem(args, true, new InitializationSplash("res/logo.png", Display.getWidth() - 50, 0));
			InitializationPlugin.register();
			
			Debug.addBlackList(ThreadManager.THREAD_MANAGER);

			Debug.log("Program version: " + EMessage.VERSION);
			Debug.log("Client version: " + Client.VERSION);

			instanceClient = getInstance();
			
			Lang.setLang(ClientResources.CONFIG);
			
			FontManager.registerFont(FontManager.loadFont(new File(FileManager.getMainPath() + "/res/font/Roboto.ttf")));
			Lang.updateCSS();
			
			CSSEngine.run("client", "bin/css");
			
			PluginRegister.preInit(new PluginEvent(EMessage.getInfo()));

			MainClient.init();
		}catch(Throwable e) {Error.createErrorFileLog(e); e.printStackTrace(); OptionPane.showErrorDialog(null, EMessage.NAME, e); stop();};
	}
	
}
