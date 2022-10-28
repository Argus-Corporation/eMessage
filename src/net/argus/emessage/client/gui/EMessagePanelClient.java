package net.argus.emessage.client.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.JScrollPane;

import net.argus.emessage.EMessageProperty;
import net.argus.emessage.client.MainClient;
import net.argus.emessage.client.event.ChatEvent;
import net.argus.emessage.client.event.EventChat;
import net.argus.event.gui.frame.FrameEvent;
import net.argus.event.gui.frame.FrameListener;
import net.argus.gui.Button;
import net.argus.gui.Panel;
import net.argus.gui.TextField;
import net.argus.emessage.api.ui.bubble.BubbleScrollPane;
import net.argus.emessage.api.ui.bubble.Type;

public class EMessagePanelClient {
	
	public static final int ME = Type.USER;
	public static final int YOU = Type.FRIEND;
	
	private BubbleScrollPane discussion;
	private TextField msg;
	private Button send;

	
	public Panel getChatPanel() throws UnknownHostException, IOException {
		Panel main = new Panel();
		main.setLayout(new BorderLayout());
		
		Panel south = new Panel();
		
		discussion = new BubbleScrollPane(Type.DARK);
		
		discussion.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		msg = new TextField(0);
		msg.addKeyListener(msg.getDefaultKeyListener());
		send = new Button("send");
		
		msg.setPreferredSize(new Dimension(GUIClient.FRAME.getSize().width - 200, 25));
		
		GUIClient.addFrameListener(new FrameListener() {
			@Override
			public void frameResizing(FrameEvent e) {
				discussion.setBounds(0, 0, GUIClient.FRAME.getSize().width, 25);
				
				msg.setPreferredSize(new Dimension(GUIClient.FRAME.getSize().width - 200, 25));
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
		discussion.addBubble(message, pseudo, pos);
		
		MainClient.getEvent().startEvent(EventChat.ADD_MESSAGE, new ChatEvent(message, pseudo, pos));
	}
	
	public void addSystemMessage(String message) {
		discussion.addBubble(message, (String) EMessageProperty.get("DefaultSystemName"), Type.CENTER);
		
		MainClient.getEvent().startEvent(EventChat.ADD_MESSAGE, new ChatEvent(message, (String) EMessageProperty.get("DefaultSystemName")));
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
