package net.argus.emessage.client.gui;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import net.argus.emessage.client.ClientConfig;
import net.argus.emessage.client.MainClient;
import net.argus.emessage.client.gui.config.PortConfig;
import net.argus.emessage.client.gui.config.profile.ProfileConfig;
import net.argus.emessage.client.gui.connect.SplashConnect;
import net.argus.gui.OptionPane;
import net.argus.lang.Lang;
import net.argus.util.ThreadManager;

public class Connect extends Thread {
	
	private SplashConnect splashConnect;
	
	private boolean fast;
	
	public Connect(boolean fast) {
		this.fast = fast;
		
		splashConnect = new SplashConnect("Connection");
	}
	
	@Override
	public void run() {
		Thread.currentThread().setName("Client-Connector");
		if(ClientConfig.getPort() != -1) {
			String host = null;
			String pseudo = null;
			String password = null;
			
			host = fast?getDefaultHost():getHost();
			
			if(host != null) {
				pseudo = JOptionPane.showInputDialog(GUIClient.getFrame(), Lang.get("text.pseudo.name"), Lang.get("text.connection.name"), JOptionPane.DEFAULT_OPTION);
				if(pseudo != null) {
					password = JOptionPane.showInputDialog(GUIClient.getFrame(), Lang.get("text.password.name"), Lang.get("text.connection.name"), JOptionPane.DEFAULT_OPTION);
					if(password != null) {
						splashConnect.setVisible(true);
						MainClient.connect(host, pseudo, password);
					}
						
				}
			}
			
		}else {
			int result = OptionPane.showMessageDialog(GUIClient.getFrame(), Lang.get("option.porterror.name"), UIManager.getString("Frame.titleErrorText"), JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
			if(result == JOptionPane.YES_OPTION) {
				GUIClient.getConfigWindow().show();
				GUIClient.getConfigWindow().setSelectedTree(PortConfig.ID);
			}
				
		}
		
	}
	
	public String getDefaultHost() {
		int index = GUIClient.config.getInt("profile.main");
		if(index == -1) {
			int result = OptionPane.showMessageDialog(GUIClient.getFrame(), Lang.get("option.nodefaulthost.name"),
					UIManager.getString("Frame.titleErrorText"), JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
			
			if(result == JOptionPane.YES_OPTION) {
				GUIClient.getConfigWindow().show();
				GUIClient.getConfigWindow().setSelectedTree(ProfileConfig.ID);
			}
			
			return null;
		}
		
		return HostInfo.getProfileConfig().getString("profile" + index + ".ip");
	}
	
	public String getHost() {
		HostInfo.openInfoDialog(GUIClient.getFrame());
		
		String host = null;
		while(host == null) {
			host = HostInfo.getHost();
			ThreadManager.sleep(1);
		}
		
		return host;
	}

}
