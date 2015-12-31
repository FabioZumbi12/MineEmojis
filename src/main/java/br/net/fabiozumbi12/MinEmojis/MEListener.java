package br.net.fabiozumbi12.MinEmojis;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import de.inventivegames.rpapi.ResourcePackStatusEvent;
import de.inventivegames.rpapi.Status;

public class MEListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerChat(AsyncPlayerChatEvent e){
		Player p = e.getPlayer();
		String chat = e.getMessage();
		
		if (MinEmojis.DeclinedPlayers.contains(p.getName())){
			return;
		}
		
		for (List<String> emojis:MinEmojis.emojis.keySet()){
			for (String emoji:emojis){
				if (chat.contains(emoji) && (p.hasPermission("minemojis.emoji."+emoji.replace(":", "")) || p.hasPermission("minemojis.emoji.all"))){
					chat = chat.replace(emoji, MinEmojis.emojis.get(emojis));				
				}
			}			
		}	
		e.setMessage(chat);
	}
	
	@EventHandler
    public void onResourcePackStatus(ResourcePackStatusEvent e) {
		if (e.getHash().equals("emojis_resourcepack")){
			if (e.getStatus().equals(Status.ACCEPTED)){
				e.getPlayer().sendMessage(MinEmojis.config.getLangString("plugin-tag")+MinEmojis.config.getLangString("installing"));
				if (MinEmojis.DeclinedPlayers.contains(e.getPlayer().getName())){
					MinEmojis.DeclinedPlayers.remove(e.getPlayer().getName());
				}
			}
			if (e.getStatus().equals(Status.SUCCESSFULLY_LOADED)){
				e.getPlayer().sendMessage(MinEmojis.config.getLangString("plugin-tag")+MinEmojis.config.getLangString("installed"));
				if (MinEmojis.DeclinedPlayers.contains(e.getPlayer().getName())){
					MinEmojis.DeclinedPlayers.remove(e.getPlayer().getName());
				}
			}
			if (e.getStatus().equals(Status.DECLINED)){
				e.getPlayer().sendMessage(MinEmojis.config.getLangString("plugin-tag")+MinEmojis.config.getLangString("disabling"));
				MinEmojis.DeclinedPlayers.add(e.getPlayer().getName());
			}
			if (e.getStatus().equals(Status.FAILED_DOWNLOAD)){
				e.getPlayer().sendMessage(MinEmojis.config.getLangString("plugin-tag")+MinEmojis.config.getLangString("error"));
				MinEmojis.DeclinedPlayers.add(e.getPlayer().getName());
			}
		} 
    }
}
