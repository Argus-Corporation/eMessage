package net.argus.chat.client.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.io.IOException;

import net.argus.chat.client.gui.config.Config;
import net.argus.event.frame.FrameListener;
import net.argus.file.FileManager;
import net.argus.file.Properties;
import net.argus.file.cjson.CJSON;
import net.argus.file.cjson.CJSONFile;
import net.argus.file.cjson.CJSONPareser;
import net.argus.image.gif.GIF;
import net.argus.image.gif.GIFLoader;
import net.argus.lang.Lang;
import net.argus.system.Temp;

public class GUIClient {
	
	public static Properties config = new Properties("config", "bin");;
	
	public static ClientFrame frame;
	
	private static Config configWindow;
	
	public static MenuBarClient menuBar = new MenuBarClient();
	public static PanelChatClient panChat = new PanelChatClient();
	
	public static final String icon16 = FileManager.getPath("res/favicon16x16.png");
	public static final String icon32 = FileManager.getPath("res/favicon32x32.png");
	
	public static final GIF load = GIFLoader.load(Temp.getTempDir() + "/res/gif/load.gif");
	public static final GIF valid = GIFLoader.load(Temp.getTempDir() + "/res/gif/valid.gif");
	public static final GIF invalid = GIFLoader.load(Temp.getTempDir() + "/res/gif/invalid.gif");
	
	private static CJSON treeConfig = CJSONPareser.parse(new CJSONFile("config", "bin"));
	
	public static void init() {
		Lang.setLang(config);
		
		frame = getFrame();

		try {
			frame.add(BorderLayout.NORTH, menuBar.getMenuBar());
			frame.add(BorderLayout.CENTER, panChat.getChatPanel());
		}catch(IOException e) {}
		
	}
	
	public static void connect() {
		menuBar.getFast().setEnabled(false);
		menuBar.getJoin().setEnabled(false);
		menuBar.getLeave().setEnabled(true);
		
		menuBar.getEncrypt().setEnabled(false);
	}
	
	public static void leave() {
		menuBar.getFast().setEnabled(true);
		menuBar.getJoin().setEnabled(true);
		menuBar.getLeave().setEnabled(false);
		
		menuBar.getEncrypt().setEnabled(true);
	}
	
	public static ClientFrame getFrame() {
		if(frame == null)
			frame = new ClientFrame(config);
		
		return frame;
	}
	
	public static Config getConfigWindow() {
		if(configWindow == null)
			configWindow = new Config(treeConfig);
			
		return configWindow;
	}
	
	public static CJSON getTreeConfig() {return treeConfig;}
	
	public static void addFrameListener(FrameListener listener) {frame.addFrameListener(listener);}
	
	public static void addFastAction(ActionListener actionListener) {menuBar.getFast().addActionListener(actionListener);}
	public static void addJoinAction(ActionListener actionListener) {menuBar.getJoin().addActionListener(actionListener);}
	public static void addLeaveAction(ActionListener actionListener) {menuBar.getLeave().addActionListener(actionListener);}
	
	public static void addPreferenceAction(ActionListener actionListener) {menuBar.getPreference().addActionListener(actionListener);}
	
	public static void addSendAction(ActionListener actionListener) {panChat.getSendButton().addActionListener(actionListener);}
	
	public static void addMessage(String[] value) {panChat.addMessage(value);}
	
	public static void setVisible(boolean v) {frame.setVisible(v);}
	
}
