package net.argus.chat.client.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.JScrollPane;

import net.argus.chat.ChatDefault;
import net.argus.chat.client.MainClient;
import net.argus.chat.client.event.ChatEvent;
import net.argus.chat.client.event.EventChat;
import net.argus.event.frame.FrameEvent;
import net.argus.event.frame.FrameListener;
import net.argus.gui.Button;
import net.argus.gui.Panel;
import net.argus.gui.TextField;
import net.argus.gui.bubble.BubblePanel;
import net.argus.gui.bubble.BubbleScrollPane;

public class PanelChatClient {
	
	public static final int ME = BubblePanel.RIGHT;
	public static final int YOU = BubblePanel.LEFT;
	
	private BubbleScrollPane discussion;
	private TextField msg;
	private Button send;
	
	private String lastPseudo;
	
	public Panel getChatPanel() throws UnknownHostException, IOException {
		Panel main = new Panel();
		main.setLayout(new BorderLayout());
		
		Panel south = new Panel();
		
		discussion = new BubbleScrollPane();
		discussion.setIndexColorRight(0);
		discussion.setIndexColorLeft(1);
		
		discussion.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		msg = new TextField(0, true);
		send = new Button("send");
		
		msg.setPreferredSize(new Dimension(GUIClient.getFrame().getSize().width - 200, 25));
		
		GUIClient.addFrameListener(new FrameListener() {
			@Override
			public void frameResizing(FrameEvent e) {
				discussion.setBounds(0, 0, GUIClient.getFrame().getSize().width, 25);
				
				msg.setPreferredSize(new Dimension(GUIClient.getFrame().getSize().width - 200, 25));
			}
			@Override
			public void frameClosing(FrameEvent e) {}
			public void frameMinimalized(FrameEvent e) {}
		});
		
		msg.addActionListener((ActionEvent e) -> send.getActionListeners()[0].actionPerformed(e));	
		
		south.add(msg);
		south.add(send);
		main.add(BorderLayout.CENTER, discussion);
		main.add(BorderLayout.SOUTH, south);
		
		return main;
	}
	
	public void addMessage(int pos, String pseudo, String message) {
		if(lastPseudo != null && lastPseudo.equals(pseudo) && pos == YOU)
			discussion.addBubble(pos, message);
		else
			discussion.addBubble(pos, message, pseudo);
			
		
		if(pseudo != null && !pseudo.equals(""))
			lastPseudo = pseudo;
		else
			lastPseudo = null;
		
		MainClient.getEvent().startEvent(EventChat.ADD_MESSAGE, new ChatEvent(message, pseudo));
	}
	
	public void addSystemMessage(String message) {
		discussion.addInfoBubble(message);
		
		MainClient.getEvent().startEvent(EventChat.ADD_MESSAGE, new ChatEvent(message, ChatDefault.DEFAULT_SYSTEM_NAME));
	}
	
	public void addArrayMessage(int pos, String pseudo, Object[] messages) {
		String message = "";
		for(Object mes : messages)
			message += mes + "\n";
		
		addMessage(pos, pseudo, message);
	}
	
	public void clearMessage() {discussion.clear();}
	
	public TextField getTextField() {return msg;}
	public Button getSendButton() {return send;}

}
