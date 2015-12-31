package br.net.fabiozumbi12.MinEmojis;

import org.bukkit.Bukkit;

public class MELogger {		   
	public static void sucess(String s) {
    	Bukkit.getConsoleSender().sendMessage("MinEmojis: [§a§l"+s+"§r]");
    }
	
    public static void info(String s) {
    	Bukkit.getConsoleSender().sendMessage("MinEmojis: ["+s+"]");
    }
    
    public static void warning(String s) {
    	Bukkit.getConsoleSender().sendMessage("MinEmojis: [§6"+s+"§r]");
    }
    
    public static void severe(String s) {
    	Bukkit.getConsoleSender().sendMessage("MinEmojis: [§c§l"+s+"§r]");
    }
    
    public static void log(String s) {
    	Bukkit.getConsoleSender().sendMessage("MinEmojis: ["+s+"]");
    }
}

