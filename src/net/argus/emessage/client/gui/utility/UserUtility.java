package net.argus.emessage.client.gui.utility;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;

import net.argus.emessage.client.ClientResources;
import net.argus.emessage.client.gui.GUIClient;
import net.argus.gui.Panel;
import net.argus.gui.TabbedPane;
import net.argus.gui.component.DialogComponent;

public class UserUtility extends DialogComponent {
	
	public static final RoomUtility ROOM_TAB = new RoomUtility();
	
	private List<UtilityTab> tabs = new ArrayList<UtilityTab>();
	
	private TabbedPane tabbedPane;
	
	private boolean visible;

	public UserUtility() {
		super();
		
		GUIClient.FRAME.addWindowListener(getFrameWindowListener());
		
		setTitleLang(true);
		setTitle("utility");
		setSize(500, 500);
		setLocationRelativeTo(GUIClient.FRAME);
		setAlwaysOnTop(true);
		
		setIcon(ClientResources.ICON.getImage());
	//	setDialogIcon(ClientResources.ICON.getImage());
		
		addTab(ROOM_TAB);
	}

	@Override
	public Panel getComponent() {
		Panel pan = new Panel();
		pan.setLayout(new BorderLayout());
		pan.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
		
		tabbedPane = new TabbedPane();
		
		pan.add(BorderLayout.CENTER, tabbedPane);
		
		return pan;
	}
	
	private WindowListener getFrameWindowListener() {
		return new WindowListener() {
			public void windowOpened(WindowEvent e) {}
			public void windowClosing(WindowEvent e) {}
			public void windowClosed(WindowEvent e) {}
			public void windowIconified(WindowEvent e) {
				visible = isVisible();
				
				setVisible(false);
			}
			public void windowDeiconified(WindowEvent e) {if(visible) setVisible(true);}
			public void windowActivated(WindowEvent e) {}
			public void windowDeactivated(WindowEvent e) {}
			
		};
	}
	
	@Override
	public void show() {
		for(int i = 0; i < tabs.size(); i++) {
			tabs.set(i, tabs.get(i).create());
			
			tabbedPane.setTitleAt(i, tabs.get(i).getName());
			tabbedPane.setIconAt(i, tabs.get(i).getIcon());
			tabbedPane.setComponentAt(i, tabs.get(i).getComponent());
			tabbedPane.setToolTipTextAt(i, tabs.get(i).getTip());
		}
		super.show();
	}
	
	public void addTab(UtilityTab tab) {
		tabs.add(tab);
		tabbedPane.addTab(tab.getName(), tab.getIcon(), tab.getComponent(), tab.getTip());
	}
	
}
