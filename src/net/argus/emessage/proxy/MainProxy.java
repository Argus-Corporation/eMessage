package net.argus.emessage.proxy;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import net.argus.exception.InstanceException;
import net.argus.instance.CardinalProgram;
import net.argus.instance.Program;
import net.argus.net.proxy.Proxy;
import net.argus.system.InitializationSystem;
import net.argus.system.UserSystem;
import net.argus.util.Error;
import net.argus.util.debug.Debug;
import net.argus.util.debug.Info;

@Program(instanceName = "proxy")
public class MainProxy extends CardinalProgram {
	
	private static Proxy proxy;
	
	public static void init(InetAddress serverAddress, int serverPort, int localPort) throws IOException {
		proxy = new Proxy(serverAddress, serverPort, localPort);
		proxy.open();
	}

	@Override
	public void main(String[] args) throws InstanceException {
		InitializationSystem.initSystem(args);
		
		InetAddress serverAddress = null;
		
		String address = UserSystem.getProperty("server");
		int serverPort = UserSystem.getIntegerProperty("server.port");
		int localPort = UserSystem.getIntegerProperty("local.port");
		
		if(address != null)
			try {serverAddress = InetAddress.getByName(address);}
			catch(UnknownHostException e) {e.printStackTrace(); Error.createErrorFileLog(e);}
		
		if(serverAddress == null) {
			Debug.log("Argument \"server\" is null", Info.ERROR);
			return;
		}
		
		if(serverPort <= 0) {
			Debug.log("Argument \"server.port\" is null", Info.ERROR);
			return;
		}
		
		if(localPort <= 0) {
			Debug.log("Argument \"local.port\" is null", Info.ERROR);
			return;
		}
		
		
		try {init(serverAddress, serverPort, localPort);}
		catch(IOException e) {e.printStackTrace();}
	}

}
