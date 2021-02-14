package net.argus.chat.client;

import java.io.IOException;
import java.net.UnknownHostException;

import net.argus.client.Client;
import net.argus.client.ClientManager;
import net.argus.client.ProcessClient;
import net.argus.client.ProcessListener;
import net.argus.event.socket.SocketListener;
import net.argus.security.Key;
import net.argus.util.pack.Package;
import net.argus.util.pack.PackageBuilder;
import net.argus.util.pack.PackageType;

public class ChatClient {
	
	public static final int PORT = 11066;
	
	private Client client;
	
	public ChatClient(String host, int port, String pseudo, String password, Key key) {
		client = new Client(host, port, key);
		
		client.setPseudo(pseudo);
		client.setPassword(password);
	}
	
	public ChatClient(String host, int port, String pseudo, String password) {
		this(host, port,pseudo, password, null);
	}
	
	public void logOut() {
		client.sendPackage(new Package(new PackageBuilder(PackageType.LOG_OUT).addValue("message", "Leave")));
	}
	
	public void start() throws UnknownHostException, IOException {client.start();}
	public void sendPackage(Package pack) {client.sendPackage(pack);}
	public void stop() {client.stop();}
	
	public void addSocketListener(SocketListener listener) {client.addSocketListener(listener);}
	public void addClientManager(ClientManager manager) {client.addClientManager(manager);}
	public void addProcessListener(ProcessListener listener) {client.addProcessListener(listener);}
	
	public Client getClient() {return client;}
	public ProcessClient getProcessClient() {return client.getProcessClient();}
	public boolean isConnected() {return client.isConnected();}

}
