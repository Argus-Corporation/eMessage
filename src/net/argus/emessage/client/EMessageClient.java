package net.argus.emessage.client;

import java.io.IOException;
import java.net.UnknownHostException;

import net.argus.emessage.api.client.CemClient;

public class EMessageClient {
		
	private CemClient client;
	
	public EMessageClient(String host, int port) throws IOException {
		client = new CemClient(host, port);
	}
	
	public void logOut(String arg) {
		client.quit(arg);
	}
	
	public void connect(String userName, String password) throws UnknownHostException, IOException {client.connect(userName, password);}
	public void send(String message) {client.send(message);}
	
	/*public void addSocketListener(SocketListener listener) {client.addSocketListener(listener);}
	public void addProcessListener(ProcessListener listener) {client.addProcessListener(listener);}
	*/
	public CemClient getClient() {return client;}
	/*public ClientProcess getClientProcess() {return client.getProcess();}
	public CardinalSocket getSocket() {return client.getCardinalSocket();}
	public Profile getProfile() {return client.getProfile();}*/
	public boolean isConnected() {return client.isConnected();}

}
