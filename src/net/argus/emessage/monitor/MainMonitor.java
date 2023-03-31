package net.argus.emessage.monitor;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import net.argus.exception.InstanceException;
import net.argus.instance.CardinalProgram;
import net.argus.instance.Program;
import net.argus.system.InitializationSystem;
import net.argus.system.UserSystem;
import net.argus.util.Error;
import net.argus.util.debug.Debug;
import net.argus.util.debug.Info;

@Program(instanceName = "monitor")
public class MainMonitor extends CardinalProgram {
	
	private static Socket socket;
	
	public static final int PORT = 8495;
	
	public static void init(InetAddress server, int port) {
		if(port <= 0)
			port = PORT;
		
		try {
			socket = new Socket(server, port);
			Debug.log("You are connected");
			
			ProcessMonitor process = new ProcessMonitor(socket);
			process.start();
		}catch(IOException e) {Debug.log("Error: Monitor server is not avaliable", Info.ERROR); stop();}
	}
	
	public static void stop() {UserSystem.exit(0);}

	@Override
	public void main(String[] args) throws InstanceException {
		InitializationSystem.initSystem(args);
		
		
		InetAddress address = null;
		String serv = UserSystem.getProperty("server");
		
		try {
			if(serv != null && !serv.equals(""))
				address = InetAddress.getByName(serv);
			else
				address = InetAddress.getByName("localhost");
		}catch(UnknownHostException e) {e.printStackTrace(); Error.createErrorFileLog(e);}
		
		int port = UserSystem.getIntegerProperty("port");
		
		if(address == null) {
			Debug.log("Argument \"server\" is null", Info.ERROR);
			return;
		}
		
		MainMonitor.init(address, port);
	}

}
