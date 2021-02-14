package net.argus.chat.server;

import java.io.IOException;

import net.argus.chat.client.ClientConfig;
import net.argus.plugin.InitializationPlugin;
import net.argus.plugin.PluginEvent;
import net.argus.plugin.PluginRegister;
import net.argus.security.Key;
import net.argus.server.Server;
import net.argus.server.role.Role;
import net.argus.system.InitializationSystem;
import net.argus.system.UserSystem;
import net.argus.util.CloseListener;

public class MainServer {
	
	private static Server serv, servCrypt;
	
	public static int PORT = 11066; 
	public static int MAX = 100;
	
	private static String PASSWORD = "rt";
	
	public static void init() throws IOException {
		info();
		
		serv = new Server(MAX, PORT);
		servCrypt = new Server(MAX, PORT + 1, new Key(ClientConfig.getDefaultKey()));
		
		serv.addClostListener(getServerCloseListener());
		servCrypt.addClostListener(getServerCryptCloseListener());
		
		PluginRegister.init(new PluginEvent(MainServer.class));

		new Role("admin").setPassword(PASSWORD).setRank(10).registry();
		
		serv.start();
		servCrypt.start();
		
		PluginRegister.postInit(new PluginEvent(MainServer.class));
	}
	
	private static void info() {
		int port = UserSystem.getIntegerProperty("port");
		int max = UserSystem.getIntegerProperty("max");
		
		String password = UserSystem.getProperty("password");
		
		PORT = port!=-1?port:PORT;
		MAX = max!=-1?max:MAX;
		
		PASSWORD = password!=null?password:PASSWORD;
	}
	
	private static CloseListener getServerCloseListener() {
		return new CloseListener() {
			public void close() {
				servCrypt.stop(0);
			}
		};
	}
	
	private static CloseListener getServerCryptCloseListener() {
		return new CloseListener() {
			public void close() {
				serv.stop(0);
			}
		};
	}
	
	public static void main(String[] args) throws IOException, InterruptedException {
		InitializationSystem.initSystem(args, false);
		InitializationPlugin.register();
		
		Thread.currentThread().setName("SERVER");
		
		PluginRegister.preInit(new PluginEvent(MainServer.class));
		
		MainServer.init();
	}

}
