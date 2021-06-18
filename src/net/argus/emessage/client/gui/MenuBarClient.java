package net.argus.emessage.client.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import net.argus.gui.Menu;
import net.argus.gui.MenuItem;
import net.argus.lang.Lang;
import net.argus.lang.LangType;

public class MenuBarClient {

	private Menu connection, configuration, lang, help;
	
	private MenuItem fast, join, leave, preference, about;
		
	public JMenuBar getMenuBar() {
		JMenuBar bar = new JMenuBar();
		
		connection = new Menu("connection");
		configuration = new Menu("configuration");
		lang = new Menu("lang");
		help = new Menu("help");
		
		fast = new MenuItem("fast");
		join = new MenuItem("join");
		leave = new MenuItem("leave");
		
		preference = new MenuItem("preference");
		
		about = new MenuItem("about");
		
		GUIClient.leave();
		
		Menu change = new Menu("change");

		for(String langName : Lang.getAllRealName()) {
			JMenuItem item = new JMenuItem(langName);
			change.add(item);
			
			item.addActionListener(getChangeLangListener(langName));
		}
		
		connection.add(fast);
		connection.add(join);
		connection.add(leave);
				
		configuration.add(preference);
		
		lang.add(change);
		
		help.add(about);
		
		bar.add(connection);
		bar.add(configuration);
		bar.add(lang);
		bar.add(help);
		
		return bar;
	}
	
	private ActionListener getChangeLangListener(String langName) {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LangType lang = LangType.getLangType(langName, 0);
				Lang.updateLang(lang);
				try {
					GUIClient.config.setKey("lang", lang.getName());
				}catch(IOException e1) {}
			}
		};
	}
	
	public Menu getConnection() {return connection;}
	public Menu getLang() {return lang;}
	
	public Menu getHelp() {return help;}
	
	public MenuItem getFast() {return fast;}
	public MenuItem getJoin() {return join;}
	public MenuItem getLeave() {return leave;}
	
	public MenuItem getPreference() {return preference;}
	
	public MenuItem getAbout() {return about;}
	
}
