package net.argus.emessage.client.gui.about;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import net.argus.emessage.Chat;
import net.argus.emessage.client.MainClient;
import net.argus.emessage.client.gui.GUIClient;
import net.argus.gui.Button;
import net.argus.gui.GUI;
import net.argus.gui.Label;
import net.argus.gui.Panel;
import net.argus.gui.component.DialogComponent;
import net.argus.instance.Instance;
import net.argus.lang.Lang;
import net.argus.lang.LangRegister;
import net.argus.system.UserSystem;

public class AboutDialog extends DialogComponent implements GUI {
	
	private Label version = new Label("", false);
	private Label checkLab = new Label("", false);

	public AboutDialog() {
		super(null);
		
		LangRegister.addElementLanguage(this);
		
		setAlwaysOnTop(true);
		setIcon(GUIClient.icon.getImage());
		setSize(565, 270);
		setResizable(false);
		
		setText();
	}

	@Override
	public Panel getComponent() {
		Panel pan = new Panel();
		pan.setLayout(new BorderLayout());
		pan.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		Panel northPan = new Panel();
		northPan.setLayout(new BorderLayout());
		
		Panel westPan = new Panel();
		westPan.add(new JLabel(GUIClient.icon));
		
		Panel eastPan = new Panel();
		eastPan.setLayout(new BoxLayout(eastPan, BoxLayout.Y_AXIS));
		
		eastPan.add(new JLabel(GUIClient.banner));
		
		Button check = new Button("checkupdate");
		check.addActionListener(getCheckActionListener());
		
		eastPan.add(check);
		eastPan.add(checkLab);
		eastPan.add(version);
		
		Panel southPan = new Panel();
		southPan.add(new JLabel(Chat.COPYRIGHT));
		
		northPan.add(BorderLayout.WEST, westPan);
		northPan.add(BorderLayout.EAST, eastPan);
		
		pan.add(BorderLayout.NORTH, northPan);
		pan.add(BorderLayout.SOUTH, southPan);
		
		
		return pan;
	}
	
	private ActionListener getCheckActionListener() {
		return (e) -> {
			if(UserSystem.update == null) {
				JOptionPane.showMessageDialog(getDialog(), Lang.get("info.noupdate.name"), (String) UIManager.get("Frame.titleErrorText"), JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			getCheckUpdateThread().start();
			
		};
	}	
	
	private Thread getCheckUpdateThread() {
		Thread th = new Thread(() -> {
			Instance.setThreadInstance(MainClient.getClientInstance());
			checkLab.setText(Lang.get("text.checkupdate.name"));
			
			if(!UserSystem.update.isLatestVersion()) {
				int result = UserSystem.update.showDialog(UIManager.get("Text.newVersion"));
				
				if(result == JOptionPane.YES_OPTION)
					UserSystem.update.downloadNewVersion();
			}else {
				UserSystem.update.showDialog(Lang.get("text.nonewupdate.name"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
			}
			SwingUtilities.invokeLater(() -> checkLab.setText(""));

		});
		
		th.setName("check-update");
		return th;
	}

	@Override
	public void setForeground(Color fore) {}

	@Override
	public void setBackground(Color bg) {}

	@Override
	public void setFont(Font font) {}

	@Override
	public void setText() {
		setTitle(Lang.get("menuitem.about.name"));
		version.setText(Lang.get("text.version.name") + ": " + Chat.VERSION);
		
		//getDialog().pack();
	}

	@Override
	public String getElementName() {return null;}

}
