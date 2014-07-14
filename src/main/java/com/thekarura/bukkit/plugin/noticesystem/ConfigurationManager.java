package com.thekarura.bukkit.plugin.noticesystem;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

public class ConfigurationManager {
	
	public final static Logger log = NoticeSystem.log;
	public final static String logPrefix = NoticeSystem.logPrefix;
	public final static String msgPrefix = NoticeSystem.msgPrefix;
	
	// ====== 作者とバージョンを指定 =======
	public String auther = "the_karura";
	public String version = "1.7.2_1-Pre-Alpha";
	
	private JavaPlugin plugin;
	
	// ** 階層 ** //
	public static File folder = new File("plugins/NoticeSystem");
	public static File listfile = new File("plugins/NoticeSystem/NoticeList.txt");
	
	public ConfigurationManager(final JavaPlugin plugin) {
		this.plugin = plugin;
	}
	
	public void save(){
		plugin.saveConfig();
	}
	
	public void load(){
		plugin.reloadConfig();
	}
}