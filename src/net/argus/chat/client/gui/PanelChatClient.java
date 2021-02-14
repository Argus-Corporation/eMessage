package net.argus.chat.client.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;

import net.argus.gui.Button;
import net.argus.gui.FrameListener;
import net.argus.gui.Panel;
import net.argus.gui.TextField;
import net.argus.gui.animation.FrameAnimation;

public class PanelChatClient {
	
	private JEditorPane discussion;
	private TextField msg;
	private Button send;
	
	public Panel getChatPanel() throws UnknownHostException, IOException {
		Panel main = new Panel();
		main.setLayout(new BorderLayout());
		
		Panel south = new Panel();
		
		discussion = new JEditorPane();
		JScrollPane scrollPan = new JScrollPane(discussion);
		discussion.setEditable(false);
	
		scrollPan.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		msg = new TextField(0, true);
		send = new Button("send");
		
		msg.setPreferredSize(new Dimension(GUIClient.getFrame().getSize().width - 200, 25));
		
		GUIClient.addFrameListener(new FrameListener() {
			@Override
			public void frameResizing() {
				discussion.setBounds(0, 0, GUIClient.getFrame().getSize().width, 25);
				
				msg.setPreferredSize(new Dimension(GUIClient.getFrame().getSize().width - 200, 25));
			}
			@Override
			public void frameClosing() {
				new FrameAnimation(GUIClient.getFrame()).play();
			}
			public void frameMinimalized() {}
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
		if(value[0] != null && !value[0].equals(""))
			pseudo = value[0] + ": ";
		discussion.setText(discussion.getText() + pseudo + value[1] + "\n");
	}
	
	public TextField getTextField() {return msg;}
	public Button getSendButton() {return send;}

}
