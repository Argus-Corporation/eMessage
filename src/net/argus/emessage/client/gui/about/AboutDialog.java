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
import javax.swing.border.Border;

import net.argus.emessage.EMessage;
import net.argus.emessage.client.ClientResources;
import net.argus.emessage.client.MainClient;
import net.argus.emessage.client.gui.GUIClient;
import net.argus.gui.Button;
import net.argus.gui.GUI;
import net.argus.gui.Label;
import net.argus.gui.LoadingLabel;
import net.argus.gui.Panel;
import net.argus.gui.component.DialogComponent;
import net.argus.instance.Instance;
import net.argus.lang.Lang;
import net.argus.lang.LangRegister;
import net.argus.system.UpdateInfo;
import net.argus.system.UserSystem;

public class AboutDialog extends DialogComponent implements GUI {
	
	private Label version;
	private Label dbgVersion;
	private LoadingLabel checkLab;

	public AboutDialog() {
		super();
		
		LangRegister.addElementLanguage(this);
		
		setIcon(ClientResources.ICON.getImage());
		setDialogIcon(ClientResources.ICON.getImage());
		
		setAlwaysOnTop(true);
		
		setSize(565, 270);
		setResizable(false);
		setLocationRelativeTo(GUIClient.FRAME);
		
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
		westPan.add(new JLabel(ClientResources.ICON));
		
		Panel eastPan = new Panel();
		eastPan.setLayout(new BoxLayout(eastPan, BoxLayout.Y_AXIS));
		
		eastPan.add(new JLabel(ClientResources.BANNER));
		
		Button check = new Button("checkupdate");
		check.addActionListener(getCheckActionListener());
		
		version = new Label("", false);
		dbgVersion = new Label("", false);
		
		checkLab = new LoadingLabel("", false);
		
		version.setBorder(getVersionBorder());

		checkLab.setBorder(getVersionBorder());
		
		eastPan.add(check);
		eastPan.add(checkLab);
		eastPan.add(version);
		eastPan.add(dbgVersion);
		
		Panel southPan = new Panel();
		southPan.add(new JLabel(EMessage.COPYRIGHT));
		
		northPan.add(BorderLayout.WEST, westPan);
		northPan.add(BorderLayout.EAST, eastPan);
		
		pan.add(BorderLayout.NORTH, northPan);
		pan.add(BorderLayout.SOUTH, southPan);
		
		
		return pan;
	}
	
	private Border getVersionBorder() {
		return BorderFactory.createEmptyBorder(5, 0, 0, 0);
	}
	
	private ActionListener getCheckActionListener() {
		return (e) -> {
			if(UserSystem.getUpdate() == null) {
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
			checkLab.start();
			
			UpdateInfo info = UserSystem.getUpdate().isLatestVersion();
			
			checkLab.stop();
			if(!info.isLatest()) {
				int result = UserSystem.getUpdate().showDialog(info);
				
				if(result == JOptionPane.YES_OPTION)
					UserSystem.getUpdate().downloadNewVersion();
			}else {
				UserSystem.getUpdate().showDialog(Lang.get("text.nonewupdate.name"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
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
		version.setText(Lang.get("text.version.name") + ": " + EMessage.VERSION);
		
		if(UserSystem.getManifest() != null)
			dbgVersion.setText(Lang.get("text.dbgversion.name") + ": " + UserSystem.getCurrentStringVersion());
	}

	@Override
	public String getElementName() {return null;}

}
