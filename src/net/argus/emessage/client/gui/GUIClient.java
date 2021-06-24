package net.argus.emessage.client.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.io.IOException;

import net.argus.emessage.ChatDefault;
import net.argus.emessage.client.ClientResources;
import net.argus.emessage.client.gui.about.AboutDialog;
import net.argus.emessage.client.gui.config.Config;
import net.argus.event.gui.frame.FrameListener;
import net.argus.gui.bubble.Bubble;

public class GUIClient {
		
	private static ClientFrame frame;
	
	private static Config configWindow;
	
	public static final MenuBarClient menuBar = new MenuBarClient();
	public static final PanelChatClient panChat = new PanelChatClient();
	
	private static AboutDialog aboutDialog;
	
	public static void init() {
		frame = getFrame();

		try {
			frame.add(BorderLayout.NORTH, menuBar.getMenuBar());
			frame.add(BorderLayout.CENTER, panChat.getChatPanel());
		}catch(IOException e) {}
		
		Bubble.setMaxWidth(frame.getWidth() / 2 - 20);
		
	}
	
	public static void connect() {
		menuBar.getFast().setEnabled(false);
		menuBar.getJoin().setEnabled(false);
		menuBar.getLeave().setEnabled(true);
	}
	
	public static void leave() {
		menuBar.getFast().setEnabled(true);
		menuBar.getJoin().setEnabled(true);
		menuBar.getLeave().setEnabled(false);
	}
	
	public static ClientFrame getFrame() {
		if(frame == null)
			frame = new ClientFrame(ClientResources.config);
		
		return frame;
	}
	
	public static AboutDialog getAboutDialog() {
		if(aboutDialog == null)
			aboutDialog = new AboutDialog();
		
		return aboutDialog;
	}
	
	public static Config getConfigWindow() {
		if(configWindow == null)
			configWindow = new Config(ClientResources.treeConfig);
			
		return configWindow;
	}
	
	public static void clearMessage() {panChat.clearMessage();}
		
	public static void addFrameListener(FrameListener listener) {frame.addFrameListener(listener);}
	
	public static void addFastAction(ActionListener actionListener) {menuBar.getFast().addActionListener(actionListener);}
	public static void addJoinAction(ActionListener actionListener) {menuBar.getJoin().addActionListener(actionListener);}
	public static void addLeaveAction(ActionListener actionListener) {menuBar.getLeave().addActionListener(actionListener);}
	
	public static void addPreferenceAction(ActionListener actionListener) {menuBar.getPreference().addActionListener(actionListener);}
	
	public static void addAboutAction(ActionListener actionListener) {menuBar.getAbout().addActionListener(actionListener);}
	
	public static void addSendAction(ActionListener actionListener) {panChat.getSendButton().addActionListener(actionListener);}
	
	public static void addMessage(int pos, String pseudo, String message) {panChat.addMessage(pos, pseudo, message);}
	public static void addMessage(String pseudo, String message) {panChat.addMessage(PanelChatClient.YOU, pseudo, message);}
	public static void addMessage(String message) {panChat.addMessage(PanelChatClient.ME, "", message);}
	public static void addSystemMessage(String message) {panChat.addSystemMessage(message);}
	
	public static void addArrayMessage(Object[] messages) {
		panChat.addArrayMessage(PanelChatClient.YOU, ChatDefault.DEFAULT_SYSTEM_NAME, messages);
	}
	
	public static void setVisible(boolean v) {frame.setVisible(v);}
	
}
