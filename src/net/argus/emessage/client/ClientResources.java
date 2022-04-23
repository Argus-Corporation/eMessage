package net.argus.emessage.client;

import javax.swing.ImageIcon;

import net.argus.cjson.CJSON;
import net.argus.cjson.CJSONParser;
import net.argus.file.CJSONFile;
import net.argus.file.FileManager;
import net.argus.file.Properties;
import net.argus.image.gif.GIF;
import net.argus.image.gif.GIFLoader;
import net.argus.sound.Audio;

public class ClientResources {
	
	public static final Properties CONFIG = new Properties("config", "bin");

	public static final String ICON_PATH = FileManager.getPath("res/eMessage.png");
	
	public static final ImageIcon ICON = new ImageIcon(ICON_PATH);
	
	public static final ImageIcon BANNER = new ImageIcon(FileManager.getPath("res/banner.png"));
	
	public static final GIF LOAD_GIF = GIFLoader.load(FileManager.getMainPath() + "/res/gif/load.gif");
	public static final GIF VALID_GIF = GIFLoader.load(FileManager.getMainPath() + "/res/gif/valid.gif");
	public static final GIF INVALID_GIF = GIFLoader.load(FileManager.getMainPath() + "/res/gif/invalid.gif");
	
	public static final Audio VALID = new Audio(FileManager.getMainPath() + "/res/sound/valid.wav");
	public static final Audio ERROR =  new Audio(FileManager.getMainPath() + "/res/sound/error.wav");
	
	public static final Audio SEND_MESSAGE =  new Audio(FileManager.getMainPath() + "/res/sound/send_message.wav");
	public static final Audio NEW_MESSAGE =  new Audio(FileManager.getMainPath() + "/res/sound/new_message.wav");
	
	public static final CJSON TREE_CONFIG = CJSONParser.getCJSON(new CJSONFile("config", "bin"));
	
	public static void playAudio(String path) {
		new Audio(path).play();
	}
	
	public static String getStringConfig(String key) {
		return CONFIG.getString(key);
	}
	
	public static int getIntConfig(String key) {
		return CONFIG.getInt(key);
	}
	
	public static boolean getBooleanConfig(String key) {
		return CONFIG.getBoolean(key);
	}

	public static void init() {}
	
}
