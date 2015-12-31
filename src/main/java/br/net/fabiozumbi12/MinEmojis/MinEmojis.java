package br.net.fabiozumbi12.MinEmojis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import br.net.fabiozumbi12.MinEmojis.Fanciful.FancyMessage;
import de.inventivegames.rpapi.IPacketPlayResourcePackStatus;
import de.inventivegames.rpapi.ResourcePackAPI;
import de.inventivegames.rpapi.ResourcePackStatusEvent;
import de.inventivegames.rpapi.Status;

public class MinEmojis extends JavaPlugin implements Listener {

	public static Server serv;
	public static PluginDescriptionFile pdf;
	public static PluginManager pm;
	public static MinEmojis plugin;
	static HashMap<List<String>, String> emojis = new HashMap<List<String>, String>();
	static List<String> DeclinedPlayers = new ArrayList<String>();
	static        IPacketPlayResourcePackStatus packet;
	public static MEConfig config;

	public void onEnable() {
		try {
			plugin = this;
			serv = getServer();
	        pdf = getDescription();
	        pm = serv.getPluginManager();
	        pm.registerEvents(new MEListener(), this); 
	        config = new MEConfig(plugin);
	        AddEmojis();
	        MELogger.sucess(pdf.getFullName()  + " enabled.");

			Bukkit.getPluginManager().registerEvents(this, this);
		} catch (Exception e){
			e.printStackTrace();
			MELogger.severe("Error enabling " + pdf.getFullName() + ", plugin will shut down.");
            super.setEnabled(false);
		}		
	}
	
	private void AddEmojis() {
		emojis.put(new ArrayList<>(Arrays.asList(":)", ":blush:")), "褀");		
		emojis.put(new ArrayList<>(Arrays.asList(":D", ":smile:")), "褁");
		emojis.put(new ArrayList<>(Arrays.asList(">:", ":rage:")), "褂");
		emojis.put(new ArrayList<>(Arrays.asList("D:", ":worried:")), "褃");
		emojis.put(new ArrayList<>(Arrays.asList("<3", ":heart:")), "褄");
		emojis.put(new ArrayList<>(Arrays.asList("</3", ":broken_heart:")), "褅");
		emojis.put(new ArrayList<>(Arrays.asList("s2", ":heart_eyes:")), "褆");
		emojis.put(new ArrayList<>(Arrays.asList(":*", ":kissing_heart:")), "複");
		emojis.put(new ArrayList<>(Arrays.asList(";-;", "T.T", ":sob:")), "褈");
		emojis.put(new ArrayList<>(Arrays.asList("B)", "8)", ":sunglasses:")), "褉");
		emojis.put(new ArrayList<>(Arrays.asList("¬¬", ":unamused:")), "褊");
		emojis.put(new ArrayList<>(Arrays.asList("zzz", ":zzz:")), "褋");
		emojis.put(new ArrayList<>(Arrays.asList("x.x", ":dizzy_face:")), "褌");
		emojis.put(new ArrayList<>(Arrays.asList("-_-", ":expressionless:")), "褍");
		emojis.put(new ArrayList<>(Arrays.asList(";)", ":wink:")), "褎");	
		emojis.put(new ArrayList<>(Arrays.asList(":(", ":worried:")), "褏");		
		emojis.put(new ArrayList<>(Arrays.asList("><", ":stuck_out_tongue_closed_eyes:")), "褐");
		emojis.put(new ArrayList<>(Arrays.asList(":p", ":P", ":yum:")), "褑");
		emojis.put(new ArrayList<>(Arrays.asList(":o", ":open_mouth:")), "褒");
	}


	public void onDisable() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			packet.removeChannelForPlayer(p);
		}
		config.saveAll();
		MELogger.severe(pdf.getFullName() + " disabled.");
	}	
		
	@Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args){
		List<String> tab = new ArrayList<String>();
		if (cmd.getName().equalsIgnoreCase("MinEmojis") && args.length == 1){			
	    	for (List<String> emojis:MinEmojis.emojis.keySet()){
	    		for (String emoji:emojis){
	    			if (emoji.startsWith(":") && emoji.endsWith(":")){
		    			tab.add(emoji);
		    		}
	    		}	    		
	    	}
		}    	
		return tab;     	
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (args.length == 0){
			sender.sendMessage(ChatColor.AQUA + MinEmojis.pdf.getFullName() + ", developed by FabioZumbi12!");
		}
		if (args.length == 1){
			if ((args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("enable")) && sender.hasPermission("minemojis.enable")){
				if (DeclinedPlayers.contains(sender.getName())){
					DeclinedPlayers.remove(sender.getName());					
				}
				sender.sendMessage(config.getLangString("plugin-tag")+config.getLangString("emojis.enabled"));
			}
			if ((args[0].equalsIgnoreCase("off") || args[0].equalsIgnoreCase("disable")) && sender.hasPermission("minemojis.enable")){
				if (!DeclinedPlayers.contains(sender.getName())){
					DeclinedPlayers.add(sender.getName());					
				}        		
				sender.sendMessage(config.getLangString("plugin-tag")+config.getLangString("emojis.disabled"));
			}
			if (args[0].equalsIgnoreCase("reload") && sender.hasPermission("minemojis.reload")){
				config.reload();
        		sender.sendMessage(ChatColor.AQUA + MinEmojis.pdf.getFullName() + " reloaded!");
			}						
			if (args[0].equalsIgnoreCase("install") && sender instanceof Player && sender.hasPermission("minemojis.install")){
				Player p = (Player) sender;
				ResourcePackAPI.setResourcepack(p, "https://dl.dropboxusercontent.com/s/mggt0usjvrrvgj5/MinEmojis.zip", "emojis_resourcepack");						
			}
			if (args[0].equalsIgnoreCase("download") && sender instanceof Player && sender.hasPermission("minemojis.download")){
				Player p = (Player) sender;
				if (config.getList("download-help-lines") != null && config.getList("download-help-lines").size() > 0){
					for (String line:config.getList("download-help-lines")){
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
					}
				}					
			}
			if (args[0].equalsIgnoreCase("list") && sender instanceof Player && sender.hasPermission("minemojis.list")){
				Iterator<List<String>> emojits = MinEmojis.emojis.keySet().iterator();
				sender.sendMessage(config.getLangString("plugin-tag")+config.getLangString("default-color")+" Emojis:");
				try {
                  	 Class.forName("com.google.gson.JsonParser");
                  	FancyMessage message = new FancyMessage();
    				
    				while (emojits.hasNext()){	
    					List<String> shortcuts = emojits.next();
    					String[] emoji = shortcuts.toArray(new String[shortcuts.size()]);
    					String shortcut = "";
    					for (String shortc:emoji){
    						shortcut = shortcut+" "+config.getLangString("default-color")+"|§r "+shortc;
    					}
    					shortcut = shortcut.substring(4);
    					message.text(config.getLangString("default-color")+"|§r"+MinEmojis.emojis.get(shortcuts)+config.getLangString("default-color")+"|§r")
    					.tooltip(MinEmojis.config.getLangString("shortcut")+shortcut)
    					.then(" ");
    				}    				
    				message.send(sender);
                  	} catch( ClassNotFoundException e ) {
                  		String send = "";
        				while (emojits.hasNext()){	
        					List<String> shortcuts = emojits.next();
        					String[] emoji = shortcuts.toArray(new String[shortcuts.size()]);
        					send = send+"|"+MinEmojis.emojis.get(shortcuts)+" = "+emoji+"|";
        				}
        				sender.sendMessage(send);
                  	}				
			}
			for (List<String> emojis:MinEmojis.emojis.keySet()){
				if (emojis.contains(args[0]) && (sender.hasPermission("minemojis."+args[0]) || sender.hasPermission("minemojis.all"))){
					sender.sendMessage(config.getLangString("plugin-tag")+" "+args[0]+"§r = "+MinEmojis.emojis.get(emojis));
				}
			}
		}		
		return true;
	}	
	
	@Override
	public void onLoad() {
		Class<?> packet_class = null;
		try {
			packet_class = Class.forName("de.inventivegames.rpapi.packets.PacketPlayResourcePackStatus_" + getVersion());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (packet_class == null) {
			Bukkit.getLogger().severe("[RPApi] Can't find compatible Packet class! (Version: " + getVersion() + ")");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}

		try {
			packet = (IPacketPlayResourcePackStatus) packet_class.newInstance();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		if (packet == null) {
			Bukkit.getLogger().severe("[RPApi] Error while loading Packet!");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}

		try {
			packet.inject();
		} catch (Exception e) {
			e.printStackTrace();
			Bukkit.getLogger().severe("[RPApi] Error while injecting Packet into Classpath!");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}

	}
	
	static String getVersion() {
		String name = Bukkit.getServer().getClass().getPackage().getName();
		String version = name.substring(name.lastIndexOf('.') + 1);
		return version;
	}
	
	public static void onResourcePackResult(Status status, Player p, String hash) {
		Bukkit.getPluginManager().callEvent(new ResourcePackStatusEvent(status, p, hash));
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		packet.addChannelForPlayer(e.getPlayer());
	}

}
