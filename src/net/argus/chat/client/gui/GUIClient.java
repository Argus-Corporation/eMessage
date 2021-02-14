package net.argus.chat.client.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.io.IOException;

import net.argus.chat.client.gui.config.Config;
import net.argus.file.FileManager;
import net.argus.file.Properties;
import net.argus.gui.Frame;
import net.argus.gui.FrameListener;
import net.argus.image.gif.GIF;
import net.argus.image.gif.GIFLoader;
import net.argus.lang.Lang;
import net.argus.system.Temp;

public class GUIClient {
	
	public static Properties config;
	
	public static ClientFrame frame;
	
	public static Config configFrame;
	
	public static MenuBarClient menuBar = new MenuBarClient();
	public static PanelChatClient panChat = new PanelChatClient();
	
	public static String icon16 = FileManager.getPath("res/favicon16x16.png");
	public static String icon32 = FileManager.getPath("res/favicon32x32.png");
	
	public static GIF load = GIFLoader.load(Temp.getTempDir() + "/res/gif/load.gif");
	public static GIF valid = GIFLoader.load(Temp.getTempDir() + "/res/gif/valid.gif");
	public static GIF invalid = GIFLoader.load(Temp.getTempDir() + "/res/gif/invalid.gif");
	
	static {
		config = new Properties("config", "bin");
		
		Lang.setLang(config);

		frame = new ClientFrame(config);
		
		configFrame = new Config();
		
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
	
	public static Frame getFrame() {return frame;}
	
	public static void addFrameListener(FrameListener listener) {frame.addFrameListener(listener);}
	
	public static void addFastAction(ActionListener actionListener) {menuBar.getFast().addActionListener(actionListener);}
	public static void addJoinAction(ActionListener actionListener) {menuBar.getJoin().addActionListener(actionListener);}
	public static void addLeaveAction(ActionListener actionListener) {menuBar.getLeave().addActionListener(actionListener);}
	
	public static void addPreferenceAction(ActionListener actionListener) {menuBar.getPreference().addActionListener(actionListener);}
	
	public static void addSendAction(ActionListener actionListener) {panChat.getSendButton().addActionListener(actionListener);}
	
	public static void addMessage(String[] value) {panChat.addMessage(value);}
	public static void setVisible(boolean v) {frame.setVisible(v);}
	
}
