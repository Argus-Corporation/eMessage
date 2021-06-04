package net.argus.chat.client.gui.config.profile;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;

import net.argus.chat.client.gui.GUIClient;
import net.argus.chat.client.gui.HostInfo;
import net.argus.chat.client.gui.TextFieldHost;
import net.argus.chat.client.gui.TextFieldName;
import net.argus.chat.client.gui.config.ConfigManager;
import net.argus.file.Properties;
import net.argus.gui.Button;
import net.argus.gui.CheckBox;
import net.argus.gui.ComboBox;
import net.argus.gui.Panel;

public class ProfileConfig extends ConfigManager {
	
	private DefaultComboBoxModel<Profile> listModel;
	private ComboBox<Profile> list;
	
	private TextFieldName name;
	private TextFieldHost host;
	
	private Button remove, create, apply;
	
	private CheckBox main;
	
	private boolean old = false;
	
	public static final int ID = 2;

	public ProfileConfig() {
		super(ID);
		listModel = new DefaultComboBoxModel<Profile>();
	}

	@Override
	public Panel getConfigPanel() {
		Panel pan = new Panel();
		pan.setLayout(new BorderLayout());
		
		Panel listPan = getListPanel();
		Panel centerPanel = getCenterPanel();
		Panel buttonPan = getButtonPanel();
		
		load();
		
		pan.add(BorderLayout.NORTH, listPan);
		pan.add(BorderLayout.CENTER, centerPanel);
		pan.add(BorderLayout.SOUTH, buttonPan);
		
		list.setSelectedIndex(list.getSelectedIndex());
		
		return pan;
	}
	
	public Panel getListPanel() {
		Panel pan = new Panel();
		
		list = new ComboBox<Profile>(listModel);
		list.addActionListener(getListActionListener());
		
		pan.add(list);
		
		return pan;
	}
	
	public Panel getCenterPanel() {
		Panel pan = new Panel();
		pan.setLayout(new BorderLayout());
		
		Panel north = new Panel();
		Panel center = new Panel();
		
		name = new TextFieldName(10);
		host = new TextFieldHost(10);
		
		main = new CheckBox("main");
		
		name.addKeyListener(getNameKeyListener());
		host.addKeyListener(getIpKeyListener());
		
		main.addActionListener(getMainActionListener());
		
		north.add(name);
		north.add(host);
		
		center.add(main);
		
		pan.add(BorderLayout.NORTH, north);
		pan.add(BorderLayout.CENTER, center);
		
		return pan;
	}
	
	public Panel getButtonPanel() {
		Panel pan = new Panel();
		
		remove = new Button("remove");
		create = new Button("create");
		apply = new Button("apply");
		
		remove.addActionListener(getRemoveActionListener());
		create.addActionListener(getCreateActionListener());
		apply.addActionListener(getApplyActionListener());
		
		apply.setEnabled(false);
		
		pan.add(remove);
		pan.add(create);
		pan.add(apply);
				
		return pan;
	}
	
	public void load() {
		Properties profile = HostInfo.getProfileConfig();
		
		String name;
		String ip;
		
		listModel.removeAllElements();
		for(int i = 0; i < profile.countLine() / 2; i++) {
			name = profile.getString("profile" + i + ".name"); 
			ip = profile.getString("profile" + i + ".ip"); 
			
			listModel.addElement(new Profile(name, ip));
		}
		
	}
	
	private ActionListener getRemoveActionListener() {
		return e -> {
			int index = list.getSelectedIndex();
			
			if(index > -1) {
				listModel.removeElementAt(index);
				updateFile();
			}
		};
	}
	
	private ActionListener getCreateActionListener() {
		return e -> {
			if(list.getSelectedIndex() < 0 || (!host.isError() && !name.isError())) {
				listModel.addElement(new Profile("", ""));
				list.setSelectedIndex(listModel.getSize() - 1);
			}
		};
	}
	
	private ActionListener getApplyActionListener() {
		return e -> {
			int result = apply();
			if(result == -1) {
				
			}
		};
	}
	
	private ActionListener getListActionListener() {
		return e -> {
			old = true;
			apply();
			
			int index = list.getSelectedIndex();
			Profile nameText = (Profile) list.getSelectedItem();
			
			name.setText(nameText!=null?nameText.toString():"");
			host.setText(HostInfo.getProfileConfig().getString("profile" + index + ".ip"));
			
			int mainProfile = GUIClient.config.getInt("profile.main");
			main.setSelected(list.getSelectedIndex() == mainProfile);
		};
	}
	
	private ActionListener getMainActionListener() {
		return e -> {
			int defIndex = GUIClient.config.getInt("profile.main");
			
			if(list.getSelectedIndex() == defIndex)
				main.setSelected(true);
			
			apply.setEnabled(true);
		};
	}
	
	private KeyListener getNameKeyListener() {
		return new KeyListener() {
			public void keyTyped(KeyEvent e) {}
			public void keyReleased(KeyEvent e) {keyPressed(e);}
			public void keyPressed(KeyEvent e) {
				Profile nameText = null;
				do {
					nameText = (Profile) list.getSelectedItem();
					
					if(nameText == null)
						getCreateActionListener().actionPerformed(null);
				}while(nameText == null);
				
				nameText.setName(name.getText());
				list.updateUI();
				apply.setEnabled(true);
				
			}
		};
	}
	
	private KeyListener getIpKeyListener() {
		return new KeyListener() {
			public void keyTyped(KeyEvent e) {}
			public void keyReleased(KeyEvent e) {}
			public void keyPressed(KeyEvent e) {
				apply.setEnabled(true);
			}
		};
	}
	
	public void add(int index) {
		Properties profileConfig = HostInfo.getProfileConfig();
		try {
			profileConfig.addKey("profile" + index + ".name", name.getText());
			profileConfig.addKey("profile" + index + ".ip", host.getText());
		}catch(IOException e) {e.printStackTrace();}
	}
	
	public void change(int index) {
		Properties profileConfig = HostInfo.getProfileConfig();
		try {
			profileConfig.setKey("profile" + index + ".name", name.getText());
			profileConfig.setKey("profile" + index + ".ip", host.getText());
		}catch(IOException e) {e.printStackTrace();}
	}
	
	public void updateFile() {
		Properties profileConfig = HostInfo.getProfileConfig();
		
		List<String> keys = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		for(int i = 0; i < listModel.getSize(); i++) {
			keys.add("profile" + i + ".name");
			keys.add("profile" + i + ".ip");
			
			values.add(listModel.getElementAt(i).getName());
			values.add(listModel.getElementAt(i).getIp());
		}
		try {
			profileConfig.clear();
			profileConfig.addKeys(keys, values);
		}catch(IOException e) {}
	}

	@Override
	public int apply() {
		if(!host.isError() && !name.isError()) {
			int index = old?list.getOldSelectedIndex():list.getSelectedIndex();
			
			if(index < 0)
				index = 0;
				
			if(index > -1 && index < (HostInfo.getProfileConfig().countLine() / 2))
				change(index);
			else if(index > -1)
				add(index);
			
			if(main.isSelected())
				try {GUIClient.config.setKey("profile.main", index);}
				catch (IOException e) {e.printStackTrace();}
			
			apply.setEnabled(false);
		}else
			return ERROR_APPLY;
		
		old = false;
		
		return VALID_APPLY;
	}
	
}