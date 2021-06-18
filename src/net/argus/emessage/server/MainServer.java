package net.argus.emessage.server;

import java.io.IOException;

import net.argus.emessage.Chat;
import net.argus.emessage.ChatDefault;
import net.argus.emessage.pack.ChatPackageType;
import net.argus.emessage.server.command.ChatCommands;
import net.argus.event.net.server.ServerEvent;
import net.argus.event.net.server.ServerListener;
import net.argus.exception.InstanceException;
import net.argus.instance.CardinalProgram;
import net.argus.instance.Program;
import net.argus.monitoring.event.MonitoringEvent;
import net.argus.monitoring.event.MonitoringListener;
import net.argus.monitoring.server.MonitoringClient;
import net.argus.monitoring.server.MonitoringServer;
import net.argus.net.server.Server;
import net.argus.net.server.ServerProcessEvent;
import net.argus.net.server.role.Role;
import net.argus.net.socket.CryptoSocket;
import net.argus.plugin.InitializationPlugin;
import net.argus.plugin.PluginEvent;
import net.argus.plugin.PluginRegister;
import net.argus.system.InitializationSystem;
import net.argus.system.UserSystem;
import net.argus.util.debug.Debug;
import net.argus.util.debug.Info;

@Program(instanceName = "server")
public class MainServer extends CardinalProgram {
	
	private static CardinalProgram program;
	
	private static MonitoringServer monitoring;
	
	private static Server serv;
	
	private static int PORT = ChatDefault.DEFAULT_PORT; 
	private static int SIZE = 100;
	
	private static String PASSWORD = "rt";
	
	public static void init() {
		info();
		
		ChatPackageType.init();
		ChatCommands.init();
		
		try {
			monitoring = new MonitoringServer(8579);
			monitoring.addMonitoringListener(getMonitoringListener());
			monitoring.open();
		}catch(IOException e) {Debug.log("Error: port 8579 for the monitoring server is not available", Info.ERROR);}
		
		serv = new Server(SIZE, PORT);
		serv.addServerListener(getServerListener());
		
		ServerProcessEvent.addProcessListener(new ServerChatProcess());
		
		PluginRegister.init(new PluginEvent(MainServer.class));
		
		new Role("administrator", PASSWORD, 10);
		
		serv.open(new CryptoSocket(true));
				
		PluginRegister.postInit(new PluginEvent(MainServer.class));
	}
	
	private static void info() {
		int port = UserSystem.getIntegerProperty("port");
		int size = UserSystem.getIntegerProperty("size");
		
		String password = UserSystem.getProperty("password");
		
		PORT = port!=-1?port:PORT;
		SIZE = size!=-1?size:SIZE;
		
		PASSWORD = password!=null?password:PASSWORD;
	}
	
	private static ServerListener getServerListener() {
		return new ServerListener() {
			@Override
			public void stop(ServerEvent e) {wakeUp();}
			
			@Override
			public void open(ServerEvent e) {}

			@Override
			public void error(ServerEvent e) {stop(e);}
		};
	}
	
	private static MonitoringListener getMonitoringListener() {
		return new MonitoringListener() {
			@Override
			public void newMonitor(MonitoringEvent e) {
				try {
					MonitoringClient client = e.getMonitoringClient();
					serv.setMonitoring(client.getInputStream(), client.getPrintStream());
				}catch(IOException e1) {e1.printStackTrace();}
			}
		};
	}
	
	public static void wakeUp() {notify(program);}
	
	public int main(String[] args) throws InstanceException {
		InitializationSystem.initSystem(args, false);
		InitializationPlugin.register();
		
		Debug.log("Program version: " + Chat.VERSION);
		
		program = this;
		
		PluginRegister.preInit(new PluginEvent(MainServer.class));
		
		MainServer.init();
		
		wait(this);
		return 0;
	}

}
