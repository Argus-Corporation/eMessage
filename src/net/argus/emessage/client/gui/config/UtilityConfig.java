package net.argus.emessage.client.gui.config;

import java.awt.BorderLayout;
import java.awt.event.ItemListener;
import java.io.IOException;

import javax.swing.BoxLayout;

import net.argus.emessage.ChatDefault;
import net.argus.emessage.client.ClientResources;
import net.argus.emessage.client.MainClient;
import net.argus.gui.CheckBox;
import net.argus.gui.Panel;

public class UtilityConfig extends ConfigManager {
	
	public static final int ID = 3;
	
	private CheckBox openOnConnect = new CheckBox("openutility");
	private CheckBox confirm = new CheckBox("confirmjoin");
	
	public UtilityConfig() {
		super(ID, MainClient.getClientInstance());
		apply.setEnabled(false);
	}

	@Override
	public Panel getConfigPanel() {
		Panel main = new Panel();
		main.setLayout(new BorderLayout());
		
		main.add(BorderLayout.CENTER, getCenterPanel());
		main.add(BorderLayout.SOUTH, getSouthPanel());
		
		return main;
	}
	
	private Panel getCenterPanel() {
		Panel center = new Panel();
		center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
		
		openOnConnect.setSelected(ChatDefault.openUtilityOnConnection());
		confirm.setSelected(ChatDefault.confirmConnectRoom());
		
		openOnConnect.addItemListener(getCheckBoxChangeListener());
		confirm.addItemListener(getCheckBoxChangeListener());
		
		center.add(openOnConnect);
		center.add(confirm);
		return center;
	}
	
	private Panel getSouthPanel() {
		Panel south = new Panel();
		
		south.add(apply);
		
		return south;
	}
	
	private ItemListener getCheckBoxChangeListener() {
		return (e) -> {
			apply.setEnabled(true);
		};
	}
	
	@Override
	public int apply() {
		try {
			ClientResources.CONFIG.setKey("open.utility", openOnConnect.isSelected() + "");
			ClientResources.CONFIG.setKey("join.confirm", confirm.isSelected() + "");
		}catch(IOException e) {return -1;}
		
		apply.setEnabled(false);
		return 0;
	}
	
	@Override
	public void setDefault() {
		
	}

}
