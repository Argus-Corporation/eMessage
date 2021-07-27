package net.argus.emessage.client.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import net.argus.emessage.client.ClientResources;
import net.argus.gui.Menu;
import net.argus.gui.MenuItem;
import net.argus.lang.Lang;
import net.argus.lang.LangType;

public class MenuBarClient {
	
	private JMenuBar bar;

	private Menu connection, configuration, lang, help;
	
	private MenuItem fast, join, leave, preference, utility, about;
	
	
	public MenuBarClient() {
		init();
	}
		
	private JMenuBar init() {
		bar = new JMenuBar();
		
		connection = new Menu("connection");
		configuration = new Menu("configuration");
		lang = new Menu("lang");
		help = new Menu("help");
		
		fast = new MenuItem("fast");
		join = new MenuItem("join");
		leave = new MenuItem("leave");
		
		preference = new MenuItem("preference");
		utility = new MenuItem("utility");
		
		about = new MenuItem("about");
		
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
		configuration.add(utility);
		
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
					ClientResources.CONFIG.setKey("lang", lang.getName());
				}catch(IOException e1) {}
			}
		};
	}
	
	public JMenuBar getMenuBar() {return bar;}
	
	public Menu getConnection() {return connection;}
	public Menu getLang() {return lang;}
	
	public Menu getHelp() {return help;}
	
	public MenuItem getFast() {return fast;}
	public MenuItem getJoin() {return join;}
	public MenuItem getLeave() {return leave;}
	
	public MenuItem getPreference() {return preference;}
	public MenuItem getUtility() {return utility;}
	
	public MenuItem getAbout() {return about;}
	
}
