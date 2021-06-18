package net.argus.emessage.client.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;

import net.argus.gui.Button;
import net.argus.gui.Label;
import net.argus.gui.Panel;
import net.argus.net.pack.Package;
import net.argus.net.pack.PackageType;
import net.argus.util.debug.Debug;
import net.argus.util.debug.Info;

public class InfoDialog extends JDialog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9159273386391123276L;

	public InfoDialog(JFrame parent) {
		super(parent);
		setLocationRelativeTo(parent);
		setLayout(new BorderLayout());
		
		addButton();
	}
	
	public InfoDialog() {
		this(GUIClient.getFrame());
	}
	
	private void addButton() {
		Panel pan = new Panel();
		
		Button ok = new Button("OK", false);
		
		ok.addActionListener(getOKActionListener());
		
		pan.add(ok);
		add(BorderLayout.SOUTH, pan);
	}
	
	private ActionListener getOKActionListener() {
		return (e) -> setVisible(false);
	}
	
	public void addInfo(Package packageInfo) {
		if(packageInfo.getType() != PackageType.INFO) {
			Debug.log("Error: this package was not an info package", Info.ERROR);
			return;
		}
		
		Object[] objs = packageInfo.getArray("Infos");
		
		if(objs != null)
			setObjects(objs);
	}
	
	public void setObjects(Object[] objs) {
		Panel pan = new Panel();
		pan.setBorder(BorderFactory.createTitledBorder("Info"));
		pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));
		
		for(Object obj : objs)
			if(obj != null)
				addObject(obj, pan);
		
		add(BorderLayout.CENTER, pan);
	}
	
	private void addObject(Object obj, JComponent comp) {
		comp.add(new Label(obj.toString(), false));
	}
	
	@Override
	public void setVisible(boolean b) {
		pack();
		super.setVisible(b);
	}

}
