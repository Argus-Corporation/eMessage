package net.argus.emessage.client.gui.config;

import java.util.ArrayList;
import java.util.List;

import net.argus.emessage.client.gui.config.profile.ProfileConfig;
import net.argus.gui.Panel;
import net.argus.instance.Instance;
import net.argus.util.DoubleStock;

public abstract class ConfigManager {
		
	private static List<ConfigManager> configs = new ArrayList<ConfigManager>();
	
	public static final ConfigManager PORT = new PortConfig();
	public static final ConfigManager PROFIL = new ProfileConfig();
	
	public static final int VALID_APPLY = 0;
	public static final int ERROR_APPLY = -1;
	
	protected DoubleStock<Integer, String> id = new DoubleStock<Integer, String>();

	public ConfigManager(int id) {
		this(id, Instance.currentInstance());
	}
	
	public ConfigManager(int id, Instance instance) {
		this(id, instance.getName());
	}
	
	public ConfigManager(int id, String instanceName) {
		this.id.setFirst(id);
		this.id.setSecond(instanceName);
		configs.add(this);
	}
	
	public static ConfigManager getConfigManager(int id, Instance instance) {
		return getConfigManager(id, instance.getName());
	}
	
	public static ConfigManager getConfigManager(int id, String instanceName) {
		for(ConfigManager config : configs)
			if(config.id.getFirst() == id && config.id.getSecond().equals(instanceName))
				return config;
		
		return null;
	}
	
	public static ConfigManager getConfigManager(int id) {
		return getConfigManager(id, Instance.currentInstance());
	}
	
	public abstract Panel getConfigPanel();
	
	public abstract int apply();
	
	public static void init() {}
	
}
