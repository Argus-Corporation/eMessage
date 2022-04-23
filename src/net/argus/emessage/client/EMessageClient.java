package net.argus.emessage.client;

import java.io.IOException;
import java.net.UnknownHostException;

import net.argus.event.net.process.ProcessListener;
import net.argus.event.net.socket.SocketListener;
import net.argus.net.Profile;
import net.argus.net.client.Client;
import net.argus.net.client.ClientProcess;
import net.argus.net.pack.Package;
import net.argus.net.socket.CardinalSocket;
import net.argus.net.socket.CryptoSocket;

public class EMessageClient {
		
	private Client client;
	
	public EMessageClient(String host, int port, String pseudo) {
		client = new Client(host, port, new CryptoSocket(false));
		
		client.getProfile().setName(pseudo);
	}
	
	public void logOut() {
		try {client.logOut("leave");}
		catch(IOException e) {e.printStackTrace();}
	}
	
	public void connect(String password) throws UnknownHostException, IOException {client.connect(password);}
	public void send(Package pack) {client.send(pack);}
	
	public void addSocketListener(SocketListener listener) {client.addSocketListener(listener);}
	public void addProcessListener(ProcessListener listener) {client.addProcessListener(listener);}
	
	public Client getClient() {return client;}
	public ClientProcess getClientProcess() {return client.getProcess();}
	public CardinalSocket getSocket() {return client.getCardinalSocket();}
	public Profile getProfile() {return client.getProfile();}
	public boolean isConnected() {return client.isConnected();}

}
