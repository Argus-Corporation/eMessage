package net.argus.chat.client.gui.config;

import java.util.ArrayList;
import java.util.List;

import net.argus.chat.client.gui.config.profile.ProfileConfig;
import net.argus.gui.Panel;

public abstract class ConfigManager {
	
	private static List<ConfigManager> configs = new ArrayList<ConfigManager>();
	
	public static final ConfigManager PORT = new PortConfig();
	public static final ConfigManager PROFIL = new ProfileConfig();
	
	protected int id;
	
	public ConfigManager(int id) {
		this.id = id;
		configs.add(this);
	}
	
	public static ConfigManager getConfigManager(int id) {
		for(ConfigManager config : configs)
			if(config.id == id)
				return config;
		
		return null;
	}
	
	public abstract Panel getConfigPanel();
	
	public abstract int apply();
	
}
