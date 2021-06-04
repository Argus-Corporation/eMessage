package net.argus.chat.monitor;

import java.io.IOException;
import java.net.Socket;

import net.argus.exception.InstanceException;
import net.argus.instance.CardinalProgram;
import net.argus.instance.Program;
import net.argus.util.debug.Debug;
import net.argus.util.debug.Info;

@Program(instanceName = "monitor")
public class MainMonitor extends CardinalProgram {
	
	private static CardinalProgram program;
	
	private static Socket socket;
	
	public static void init() {
		try {
			socket = new Socket("127.0.0.1", 8579);
			Debug.log("You are connected");
			
			ProcessMonitor process = new ProcessMonitor(socket);
			process.start();
		}catch(IOException e) {Debug.log("Error: Monitor server is not avaliable", Info.ERROR); wakeUp();}
	}
	
	public static void wakeUp() {notify(program);}

	@Override
	public int main(String[] args) throws InstanceException {
		program = this;
		
		MainMonitor.init();
		wait(this);
		return 0;
	}

}
