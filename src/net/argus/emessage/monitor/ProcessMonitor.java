package net.argus.emessage.monitor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

import net.argus.instance.Instance;
import net.argus.util.debug.Debug;

public class ProcessMonitor {
	
	private BufferedReader in;
	private PrintStream out;
		
	private boolean running = true;
	
	public ProcessMonitor(Socket socket) throws IOException {
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintStream(socket.getOutputStream());
	}
	
	public void start() {
		Instance.startThread(new Thread(getScanRunnable()));
		Instance.startThread(new Thread(getMonitorRunnable()));
	}
	
	@SuppressWarnings("resource")
	private Runnable getScanRunnable() {
		return () -> {			
			Scanner scan = new Scanner(System.in);
			while(running) {
				String line = scan.nextLine();
				if(line.startsWith("!"))
					stop();
					
				out.println(line);
				out.flush();
			}
		};
	}
	
	@SuppressWarnings("resource")
	private Runnable getMonitorRunnable() {
		return () -> {
			Thread.currentThread().setName("monitor");
			Scanner scan = new Scanner(in);
			try {
				while(running)
					System.out.println(scan.nextLine());
			}catch(Exception e) {
				Debug.log("Connection was close");
				stop();
			}
		};
	}
	
	public void stop() {
		running = false;
		MainMonitor.stop();
	}

}
