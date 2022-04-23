package net.argus.emessage.server;

import java.io.IOException;

import net.argus.emessage.EMessage;
import net.argus.emessage.EMessageProperty;
import net.argus.emessage.pack.EMessagePackagePrefab;
import net.argus.emessage.pack.EMessagePackageType;
import net.argus.emessage.proxy.MainProxy;
import net.argus.emessage.server.command.EMessageCommands;
import net.argus.event.net.server.ServerEvent;
import net.argus.event.net.server.ServerListener;
import net.argus.exception.InstanceException;
import net.argus.instance.CardinalProgram;
import net.argus.instance.Program;
import net.argus.monitoring.event.MonitoringEvent;
import net.argus.monitoring.event.MonitoringListener;
import net.argus.monitoring.server.MonitorWhiteList;
import net.argus.monitoring.server.MonitoringClient;
import net.argus.monitoring.server.MonitoringServer;
import net.argus.net.server.Server;
import net.argus.net.server.ServerProcess;
import net.argus.net.server.ServerProcessEvent;
import net.argus.net.server.role.Role;
import net.argus.net.server.room.Room;
import net.argus.net.server.room.RoomRegister;
import net.argus.net.socket.CryptoSocket;
import net.argus.plugin.InitializationPlugin;
import net.argus.plugin.PluginEvent;
import net.argus.plugin.PluginRegister;
import net.argus.system.InitializationSystem;
import net.argus.system.Network;
import net.argus.system.UserSystem;
import net.argus.util.Error;
import net.argus.util.debug.Debug;
import net.argus.util.debug.Info;

@Program(instanceName = "server")
public class MainServer extends CardinalProgram {
		
	private static MonitoringServer monitoring;
	
	private static Server serv;
	
	private static int port = EMessageProperty.getInt("DefaultPort"); 
	private static int size = EMessageProperty.getInt("DefaultMainRoomSize");
	
	private static String password;
	
	public static void init() {
		info();
		
		if(UserSystem.getBooleanProperty("proxy.local")) {
			int proxyPort = UserSystem.getIntegerProperty("proxy.port");
			if(proxyPort <= 0)
				proxyPort = EMessageProperty.getInt("AlternativePort");
			
			try {MainProxy.init(Network.getByName("127.0.0.1"), port, proxyPort);}
			catch(IOException e) {Error.createErrorFileLog(e); Debug.log("Error when create proxy server", Info.ERROR);}
		}
		
		EMessagePackageType.init();
		EMessageCommands.init();
		
		try {
			monitoring = new MonitoringServer(8495);
			monitoring.addMonitoringListener(getMonitoringListener());
			monitoring.open();
		}catch(IOException e) {Debug.log("Error: port 8495 for the monitoring server is not available", Info.ERROR);}
		
		whitelistMonitor();
		
		serv = new Server(size, port);
		serv.addServerListener(getServerListener());
		
		ServerProcessEvent.addProcessListener(new EMessageServerProcess());
		
		PluginRegister.init(new PluginEvent(MainServer.class));
		
		if(password != null && !password.equals(""))
			new Role("administrator", password, 10);
		
		serv.open(new CryptoSocket(true));
				
		PluginRegister.postInit(new PluginEvent(MainServer.class));
	}
	
	private static void info() {
		int port = UserSystem.getIntegerProperty("port");
		int size = UserSystem.getIntegerProperty("size");
		
		String password = UserSystem.getProperty("password");
		
		MainServer.port = port!=-1?port:MainServer.port;
		MainServer.size = size!=-1?size:MainServer.size;
		
		MainServer.password = password!=null?password:MainServer.password;
		
	}
	
	private static void whitelistMonitor() {
		String list = UserSystem.getProperty("monitor");
		
		if(list == null || list.equals(""))
			return;
		
		String[] hosts = list.split(";");
		
		for(String host : hosts)
			MonitorWhiteList.addInetAddress(Network.getByName(host));
	}
	
	public static void sendNewRoom(Room newRoom) {
		for(Room r : RoomRegister.getRooms()) {
			for(ServerProcess p : r.getClients()) {
				try {p.send(EMessagePackagePrefab.genRoomPackage(newRoom));}
				catch(IOException e) {e.printStackTrace();}
			}
		}
	}
	
	public static void sendCloseRoom(Room newRoom) {
		for(Room r : RoomRegister.getRooms()) {
			for(ServerProcess p : r.getClients()) {
				try {p.send(EMessagePackagePrefab.genCloseRoomPackage(newRoom));}
				catch(IOException e) {e.printStackTrace();}
			}
		}
	}
	
	public static void sendRoom(ServerProcess p) {
		for(Room r : RoomRegister.getRooms())
			try {p.send(EMessagePackagePrefab.genRoomPackage(r));}
			catch(IOException e) {e.printStackTrace();}
	}

	
	private static ServerListener getServerListener() {
		return new ServerListener() {
			@Override
			public void stop(ServerEvent e) {MainServer.stop();}
			
			@Override
			public void open(ServerEvent e) {}

			@Override
			public void error(ServerEvent e) {stop(e);}

			@Override
			public void roomCreate(ServerEvent e) {
				Room room = (Room) e.getObject();
				sendNewRoom(room);
			}

			@Override
			public void roomRemove(ServerEvent e) {
				Room room = (Room) e.getObject();
				sendCloseRoom(room);
			}

			@Override
			public void userJoin(ServerEvent e) {
				ServerProcess p = (ServerProcess) e.getObject();
				
				sendRoom(p);
			}

			@Override
			public void stopAction(ServerEvent e) {
				
			}
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
	
	public static void addServerListener(ServerListener listener) {serv.addServerListener(listener);}
	
	public static Server getServer() {return serv;}
		
	public static void stop() {UserSystem.exit(0);}
	
	public void main(String[] args) throws InstanceException {
		InitializationSystem.initSystem(args, false);
		InitializationPlugin.register();
		
		Debug.log("Program version: " + EMessage.VERSION);
				
		PluginRegister.preInit(new PluginEvent(MainServer.class));
		MainServer.init();
	}

}
