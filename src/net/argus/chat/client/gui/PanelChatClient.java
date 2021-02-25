package net.argus.chat.client.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.JScrollPane;

import net.argus.chat.client.MainClient;
import net.argus.chat.client.event.ChatEvent;
import net.argus.chat.client.event.EventChat;
import net.argus.event.frame.FrameEvent;
import net.argus.event.frame.FrameListener;
import net.argus.gui.Button;
import net.argus.gui.EditorPane;
import net.argus.gui.Panel;
import net.argus.gui.TextField;

public class PanelChatClient {
	
	private EditorPane discussion;
	private TextField msg;
	private Button send;
	
	public Panel getChatPanel() throws UnknownHostException, IOException {
		Panel main = new Panel();
		main.setLayout(new BorderLayout());
		
		Panel south = new Panel();
		
		discussion = new EditorPane();
		JScrollPane scrollPan = new JScrollPane(discussion);
		discussion.setEditable(false);
	
		scrollPan.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
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
		main.add(BorderLayout.CENTER, scrollPan);
		main.add(BorderLayout.SOUTH, south);
		
		return main;
	}
	
	public void addMessage(String[] value) {
		String pseudo = "";
		String message = "";
		if(value[0] != null && !value[0].equals(""))
			pseudo = value[0] + ": ";
		
		message = pseudo + value[1];
		discussion.setText(discussion.getText() + message + "\n");
		
		MainClient.getEvent().startEvent(EventChat.ADD_MESSAGE, new ChatEvent(value[0], value[1]));
		
	}
	
	public TextField getTextField() {return msg;}
	public Button getSendButton() {return send;}

}
