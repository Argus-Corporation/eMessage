package net.argus.emessage.client.gui.utility;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;

import net.argus.emessage.EMessageDefault;
import net.argus.emessage.client.MainClient;
import net.argus.emessage.client.room.Room;
import net.argus.emessage.client.room.RoomRegister;
import net.argus.gui.Button;
import net.argus.gui.CList;
import net.argus.gui.OptionPane;
import net.argus.gui.Panel;
import net.argus.gui.TextArea;
import net.argus.lang.Lang;
import net.argus.util.debug.Debug;

public class RoomUtility extends UtilityTab {
	
	private Panel mainPan;
	private TextArea descriptionArea;
	
	private Button join;
	
	private DefaultListModel<Room> model;
	private CList<Room> roomList;
	
	public RoomUtility() {
		mainPan = new Panel();
		
		model = new DefaultListModel<Room>();
		roomList = new CList<Room>(model);
		
		init();
	}
	
	private void init() {
		mainPan = new Panel();
		mainPan.setLayout(new BorderLayout());
				
		Panel westPan = new Panel();
		westPan.setLayout(new BorderLayout());
		westPan.setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 20));
		
		roomList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		roomList.addListSelectionListener(getListSelectionListener());
		roomList.addMouseListener(getMouseListener());
		
		JScrollPane listScrollPan = new JScrollPane(roomList);
		
		listScrollPan.setPreferredSize(new Dimension(100, 50));
		
		Panel centerPan = new Panel();
		centerPan.setLayout(new BoxLayout(centerPan, BoxLayout.Y_AXIS));
		centerPan.setBorder(BorderFactory.createEmptyBorder(15, 0, 20, 20));
		
		centerPan.add(getInfoPanel());
		
		join = new Button("join");
		join.addActionListener(getJoinActionListener());
		join.setEnabled(false);
		
		centerPan.add(join);
		
		westPan.add(BorderLayout.CENTER, listScrollPan);
		
		mainPan.add(BorderLayout.WEST, westPan);
		mainPan.add(BorderLayout.CENTER, centerPan);
	}
	
	
	private Panel getInfoPanel() {
		Panel infoPan = new Panel();
		infoPan.setLayout(new BorderLayout());
		infoPan.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
		
		descriptionArea = new TextArea();
		
		descriptionArea.setEditable(false);
		descriptionArea.setLineWrap(true);
		
		JScrollPane infoScrollPan = new JScrollPane(descriptionArea);
		infoScrollPan.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		infoPan.add(BorderLayout.CENTER, infoScrollPan);
		
		return infoPan;
	}

	@Override
	public JComponent getComponent() {
		return mainPan;
	}
	
	@Override
	public UtilityTab create() {
		model.removeAllElements();
		for(Room room : RoomRegister.getRooms())
			model.addElement(room);
		
		descriptionArea.setText("");
		return this;
	}
	
	private MouseListener getMouseListener() {
		return new MouseListener() {
			public void mouseReleased(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {
				int index = roomList.getSelectedIndex();
							
				if(index >= model.getSize())
					index = model.getSize() - 1;
				
				if(index == -1)
					return;
				
				Room room = model.get(index);
				
				if(e.getClickCount() != 2)
					return;
				
				join(room);
			}
			public void mouseExited(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseClicked(MouseEvent e) {}
		};
	}
	
	private ListSelectionListener getListSelectionListener() {
		return (e) -> {
			int index = roomList.getSelectedIndex();
			
			if(index >= model.getSize())
				index = model.getSize() - 1;
			
			if(index == -1) {
				join.setEnabled(false);
				return;
			}
			
			join.setEnabled(true);
			
			Room room = model.get(index);
			
			descriptionArea.setText("Name: " + room.getName());
			descriptionArea.setText(descriptionArea.getText() + (room.isPrivate()?"\n\n" + Lang.get("text.privateroom.name"):""));
			
		};
	}
	
	private ActionListener getJoinActionListener() {
		return (e) -> {
			Room room = roomList.getSelectedValue();
			
			if(room == null)
				return;
			
			join(room);
		};
	}
	
	public void join(Room room) {
		String password = null;
		if(room.isPrivate()) {
			password = OptionPane.showPasswordDialog(null, Lang.get("text.roompass.name"), Lang.get("text.password.name"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			if(password == null || password.equals(""))
				return;
		}else if(EMessageDefault.confirmConnectRoom()) {
			int result = OptionPane.showDialog(null, Lang.get("text.consentjoinroom.name"), Lang.get("text.room.name"), JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
			
			if(result != JOptionPane.YES_OPTION)
				return;
		}
		
		try {
			MainClient.joinRoom(room.getName(), password);
		}catch(IOException e) {
			Debug.log("Error to join room \"" + room.getName() + "\"");
		}
	}
	
	public void addRoom(Room room) {
		model.addElement(room);
	}

	public void removeRoom(Room room) {
		model.removeElement(room);
	}

	@Override
	public String getName() {
		return "room";
	}

}
