package com.thekarura.bukkit.plugin.noticesystem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.thekarura.bukkit.plugin.noticesystem.listener.PlayerListener;

public class NoticeSystem extends JavaPlugin {
	
	// ====== Logger ======
	public final static Logger log = Logger.getLogger("Notice");
	public final static String logPrefix = "[Notice]";
	public final static String msgPrefix = "§D[Notice]§R";
	
	// ===== Listener ======
	private final PlayerListener playerListener = new PlayerListener(this);
	
	// ====== Configrationから読み込み ======
	private ConfigurationManager config;
	public static File folder = ConfigurationManager.folder;
	public static File listfile = ConfigurationManager.listfile;
	
	// ====== instance化 ======
	private static NoticeSystem instance;
	
	// ====== 起動処理時 ======
	public void onEnable(){
		
		instance = this;
		PluginManager pm = getServer().getPluginManager();
		config = new ConfigurationManager(this);
		config.load();
		
		pm.registerEvents(playerListener, this);
		
		if (!(listfile.exists())){
			log.info(logPrefix + "NoticeListファイルを生成します...");
			try {
				folder.mkdir();
				listfile.createNewFile();
			} catch (IOException e) {
				log.info(logPrefix + "ファイル作成が失敗しました。pluginを停止させます。");
				pm.disablePlugin(this);
				e.printStackTrace();
				return;
			}
			log.info(logPrefix + "ファイル作成が正常に完了しました。");
		}
		log.info(logPrefix + "onEnable has been invoked!");
		
	}
	
	// ====== 動作停止時 ======
	public void onDisable(){
		log.info(logPrefix + "onDisable has been invoked!");
	}
	
	public boolean onCommand(CommandSender sender ,Command cmd ,String rabel ,String[] args){
		if (cmd.getName().equalsIgnoreCase("Notice")) {
			
			String senderName = ChatColor.stripColor(sender.getName());
			if (sender instanceof ConsoleCommandSender) {
				senderName = "Console";
			}
			
			if (args.length == 0) {
				
				sender.sendMessage(msgPrefix + "HelpList => /Notice help or list /Notice [notice]");
				return true;
				
			} else if (args[0].equalsIgnoreCase("Help")) {
					
				sender.sendMessage(msgPrefix + "§Dplugin helps.");
				sender.sendMessage("§D=======================================");
				sender.sendMessage("§D     NoticeSystem");
				sender.sendMessage("§D     version : " + config.version);
				sender.sendMessage("§D     Auther :" + config.auther);
				sender.sendMessage("§D=======================================");
					
			} else if (args[0].equalsIgnoreCase("List")){
				
				if (checkfile(listfile)) {
					try {
						BufferedReader br = new BufferedReader(new FileReader(listfile));
						String str = br.readLine();
						int strint = 0;
						int read = 0;
						sender.sendMessage(msgPrefix + "§DNotices 10 list.");
						sender.sendMessage("§D=======================================");
						
						/* 
						 * ここを最新10件を見せるようにしたいけど
						 * 製作者の知識不足によりあえなく断念しました。
						 * 解決策があれば教えて下さい、、、
						 */
						
						while(str != null) {
							if (!(read == 50)) {
								sender.sendMessage(strint+" : "+str);
								read++;
							}
							str = br.readLine();
							strint++;
						}
						br.close();
						sender.sendMessage("§D=======================================");
						
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch(IOException e) {
						  System.out.println(e);
					}
				} else {
					log.info(logPrefix + "§4 Error : FileNotFound.");
					sender.sendMessage(msgPrefix + "§4 Error : FileNotFound");
					return true;
				}
				
			} else {
				if (args[0].length() < 20) {
					
					StringBuilder commitmessage = new StringBuilder(args[0]);
					for (int i = 1; i < args.length ; i++){
					commitmessage.append(" ").append(args[i]);
					}
					
					if (checkfile(listfile)) {
						
						String time;
						time = TimeDate();
						
						try {
							PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(listfile, true)));
							pw.println(logPrefix + " " + senderName + " : " + commitmessage);
							pw.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						
						Bukkit.broadcastMessage(msgPrefix + " §D" + senderName + " : " + commitmessage + " : " + time + "§R");
						
					} else {
						log.info(logPrefix + "§4 Error : FileNotFound.");
						sender.sendMessage(msgPrefix + "§4 Error : FileNotFound");
						return true;
					}
				} else {
					sender.sendMessage(msgPrefix + " §D50文字に抑えてください。§R");
				}
			}
			
		return true;
		}
		
	return false;
	}
	
	public ConfigurationManager getConfigs() {
		return config;
	}
	
	// ** 時刻を確認します。 ** //
	public String TimeDate(){
		
		Date date1 = new Date();
		String gettime;
		
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");
		gettime = sdf1.format(date1);
		
	return gettime;
	}
	
	// ** ファイルの有無を確認します。 ** //
	public boolean checkfile(File file){
		if (file.exists()){
			if (file.isFile() && file.canRead()){
			return true;
			}
		}
	return false;
	}
}
