package net.argus.chat.client.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import net.argus.file.Properties;
import net.argus.gui.Button;
import net.argus.gui.CheckBox;
import net.argus.gui.Icon;
import net.argus.gui.OptionPane;
import net.argus.gui.Panel;
import net.argus.instance.Instance;
import net.argus.lang.Lang;

public class HostInfo {
	
	private static Properties profileConfig = new Properties("profile", "/", Instance.SYSTEM);
	
	private static String result;
	
	public static void openInfoDialog(Component parent) {
		List<String> hostName = getHostName();
		
		result = null;
		
		JDialog dial = new JDialog();
		dial.setSize(300, 150);
		dial.setLocationRelativeTo(parent);
		dial.setAlwaysOnTop(true);
		dial.setResizable(false);
		dial.setIconImage(Icon.getIcon(GUIClient.icon16).getImage());
		dial.setLayout(new BorderLayout());
			
		Panel north = new Panel();
		Panel south = new Panel();
		Panel option = new Panel();
		
		CheckBox saveCheck = new CheckBox("profile");
		north.add(saveCheck);
		
		CheckBox newCheck = new CheckBox("newhost");
		south.add(newCheck);
		
		JComboBox<String> list = new JComboBox<String>((String[]) hostName.toArray(new String[hostName.size()]));
		north.add(list);
		
		TextFieldHost host = new TextFieldHost(12);
		south.add(host);
		
		Button ok = new Button("   OK   ", false);
		option.add(ok);
		
		Button cancel = new Button(UIManager.get("OptionPane.cancelButtonText").toString(), false);
		option.add(cancel);
		
		saveCheck.setSelected(true);
		host.setEnabled(false);
		
		saveCheck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveCheck.setSelected(true);
				newCheck.setSelected(false);
				
				list.setEnabled(true);
				host.setEnabled(false);
			}
		});
		
		newCheck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newCheck.setSelected(true);
				saveCheck.setSelected(false);
				
				list.setEnabled(false);
				host.setEnabled(true);
			}
		});
		
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(saveCheck.isSelected()) {
					
					dial.setVisible(false);
					result = profileConfig.getString("profile" + hostName.indexOf(list.getSelectedItem()) + ".ip");
				}else {
					if(!host.isError()) {
						dial.setVisible(false);
						result = host.getText();
					}else {
						OptionPane.showMessageDialog(dial, Lang.get("option.hosterror.name"), UIManager.getString("Frame.titleErrorText"),
								JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dial.setVisible(false);
			}
		});
		
		dial.add(BorderLayout.NORTH, north);
		dial.add(BorderLayout.CENTER, south);
		dial.add(BorderLayout.SOUTH, option);
		
		dial.pack();
				
		dial.setVisible(true);
		
	}
	
	public static List<String> getHostName() {
		List<String> name = new ArrayList<String>();
		
		for(int i = 0; i < (profileConfig.countLine() / 2); i++)
			name.add(profileConfig.getString("profile" + i + ".name"));
			
		return name;
	}
	
	public static String getHost() {return result;}
	
	public static Properties getProfileConfig() {return profileConfig;}

}
