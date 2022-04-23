package net.argus.emessage.client.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.io.IOException;

import net.argus.emessage.EMessageDefault;
import net.argus.emessage.EMessageProperty;
import net.argus.emessage.client.ClientResources;
import net.argus.emessage.client.gui.about.AboutDialog;
import net.argus.emessage.client.gui.config.Config;
import net.argus.emessage.client.gui.utility.UserUtility;
import net.argus.emessage.client.room.RoomRegister;
import net.argus.event.gui.frame.FrameListener;
import net.argus.gui.bubble.Bubble;

public class GUIClient {
		
	public static final ClientFrame FRAME = new ClientFrame();
	
	private static Config configWindow;
	
	public static final MenuBarClient MENU_BAR = new MenuBarClient();
	public static final EMessagePanelClient CHAT_PANEL = new EMessagePanelClient();
	
	public static final AboutDialog ABOUT = new AboutDialog();
	
	public static final UserUtility UTILITY = new UserUtility();
	
	public static void init() {
		try {
			FRAME.add(BorderLayout.NORTH, MENU_BAR.getMenuBar());
			FRAME.add(BorderLayout.CENTER, CHAT_PANEL.getChatPanel());
		}catch(IOException e) {}
		
		GUIClient.leave();
		
		Bubble.setMaxWidth(FRAME.getWidth() / 2 - 20);
				
	}
	
	public static void connect() {
		MENU_BAR.getFast().setEnabled(false);
		MENU_BAR.getJoin().setEnabled(false);
		MENU_BAR.getLeave().setEnabled(true);
		
		MENU_BAR.getUtility().setEnabled(true);
		
		if(EMessageDefault.openUtilityOnConnection())
			UTILITY.show();
	}
	
	public static void leave() {
		MENU_BAR.getFast().setEnabled(true);
		MENU_BAR.getJoin().setEnabled(true);
		MENU_BAR.getLeave().setEnabled(false);
		
		MENU_BAR.getUtility().setEnabled(false);
		
		UTILITY.hide();
		
		RoomRegister.removeAll();

	}
	
	public static Config getConfigWindow() {
		if(configWindow == null)
			configWindow = new Config(ClientResources.TREE_CONFIG);
			
		return configWindow;
	}
	
	public static void clearMessage() {CHAT_PANEL.clearMessage();}
		
	public static void addFrameListener(FrameListener listener) {FRAME.addFrameListener(listener);}
	
	public static void addFastAction(ActionListener actionListener) {MENU_BAR.getFast().addActionListener(actionListener);}
	public static void addJoinAction(ActionListener actionListener) {MENU_BAR.getJoin().addActionListener(actionListener);}
	public static void addLeaveAction(ActionListener actionListener) {MENU_BAR.getLeave().addActionListener(actionListener);}
	
	public static void addPreferenceAction(ActionListener actionListener) {MENU_BAR.getPreference().addActionListener(actionListener);}
	public static void addUtilityAction(ActionListener actionListener) {MENU_BAR.getUtility().addActionListener(actionListener);}
	
	public static void addAboutAction(ActionListener actionListener) {MENU_BAR.getAbout().addActionListener(actionListener);}
	
	public static void addSendAction(ActionListener actionListener) {CHAT_PANEL.getSendButton().addActionListener(actionListener);}
	
	public static void addMessage(int pos, String pseudo, String message) {CHAT_PANEL.addMessage(pos, pseudo, message);}
	public static void addMessage(String pseudo, String message) {CHAT_PANEL.addMessage(EMessagePanelClient.YOU, pseudo, message);}
	public static void addMessage(String message) {CHAT_PANEL.addMessage(EMessagePanelClient.ME, "", message);}
	public static void addSystemMessage(String message) {CHAT_PANEL.addSystemMessage(message);}
	
	public static void addArrayMessage(Object[] messages) {
		CHAT_PANEL.addArrayMessage(EMessagePanelClient.YOU, (String) EMessageProperty.get("DefaultSystemName"), messages);
	}
	
	public static void setVisible(boolean v) {FRAME.setVisible(v);}
	
}
