package com.thekarura.bukkit.plugin.noticesystem.listener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.thekarura.bukkit.plugin.noticesystem.ConfigurationManager;
import com.thekarura.bukkit.plugin.noticesystem.NoticeSystem;

public class PlayerListener implements Listener {
	
	// ====== Logger ======
	public final static Logger log = NoticeSystem.log;
	public final static String logPrefix = NoticeSystem.logPrefix;
	public final static String msgPrefix = NoticeSystem.msgPrefix;
	
	public final static File listfile = ConfigurationManager.listfile;
	
	public PlayerListener(NoticeSystem noticeSystem) {
		
	}
	
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event){
		Player player = event.getPlayer();
		if(!(player.hasPermission("NoticeSystem.loginmessage"))){ return; }
		String nameid = player.getName();
			if (checkfile(listfile)) {
				
				int read = 0;
				BufferedReader br;
				
				try {
					
					br = new BufferedReader(new FileReader(listfile));
					String str = br.readLine();
					int strint = 0;
					
					if (str != null) {
						
						player.sendMessage(msgPrefix + "以下の通知があります。");
						player.sendMessage("§D=======================================");
						while(str != null) {
							if (!(strint + read == 10)) {
								player.sendMessage(str);
								read++;
							}
							str = br.readLine();
						}
						br.close();
						player.sendMessage("§D=======================================");
						return;
						
					} else { return; }
					
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch(IOException e) {
					  System.out.println(e);
				}
				
			} else { return; }
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
