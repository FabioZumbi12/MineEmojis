package br.net.fabiozumbi12.MinEmojis;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.inventivetalent.rpapi.ResourcePackStatusEvent;
import org.inventivetalent.rpapi.Status;

public class MEListenerRPA implements Listener {

	@EventHandler
    public void onResourcePackStatus(ResourcePackStatusEvent e) {
		Player p = e.getPlayer();
		if (MinEmojis.isInstalling(p)){
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
				MinEmojis.delInstalling(p);
			}
			if (e.getStatus().equals(Status.DECLINED)){
				e.getPlayer().sendMessage(MinEmojis.config.getLangString("plugin-tag")+MinEmojis.config.getLangString("disabling"));
				MinEmojis.DeclinedPlayers.add(e.getPlayer().getName());
				MinEmojis.delInstalling(p);
			}
			if (e.getStatus().equals(Status.FAILED_DOWNLOAD)){
				e.getPlayer().sendMessage(MinEmojis.config.getLangString("plugin-tag")+MinEmojis.config.getLangString("error"));
				MinEmojis.DeclinedPlayers.add(e.getPlayer().getName());
				MinEmojis.delInstalling(p);
			}
		} 
    }	
}
