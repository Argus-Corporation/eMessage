package net.argus.emessage.client;

import javax.swing.ImageIcon;

import net.argus.cjson.CJSON;
import net.argus.cjson.CJSONParser;
import net.argus.file.CJSONFile;
import net.argus.file.FileManager;
import net.argus.file.Properties;
import net.argus.image.gif.GIF;
import net.argus.image.gif.GIFLoader;

public class ClientResources {
	
	public static final Properties config = new Properties("config", "bin");

	public static final String iconPath = FileManager.getPath("res/eMessage.png");
	
	public static final ImageIcon icon = new ImageIcon(iconPath);
	
	public static final ImageIcon banner = new ImageIcon(FileManager.getPath("res/banner.png"));
	
	public static final GIF load = GIFLoader.load(FileManager.getMainPath() + "/res/gif/load.gif");
	public static final GIF valid = GIFLoader.load(FileManager.getMainPath() + "/res/gif/valid.gif");
	public static final GIF invalid = GIFLoader.load(FileManager.getMainPath() + "/res/gif/invalid.gif");
	
	public static final CJSON treeConfig = CJSONParser.getCJSON(new CJSONFile("config", "bin"));

	public static void init() {}
	
}
