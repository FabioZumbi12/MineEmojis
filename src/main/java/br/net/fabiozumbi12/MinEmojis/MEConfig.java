package br.net.fabiozumbi12.MinEmojis;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

public class MEConfig {

	private static MinEmojis plugin;
	private MEYaml config;

	public MEConfig(MinEmojis pl) {
		plugin = pl;
		config = new MEYaml();
		LoadConfigs();
		loadDisabled();
	}
	
	public void reload(){
		LoadConfigs();
		loadDisabled();
		saveAll();
	}
	
	private void saveDisabled(){
		MEYaml disFile = new MEYaml();
		File file = new File(plugin.getDataFolder(), "disabled.yml");
		try {
			disFile.set("disabled-for", MinEmojis.DeclinedPlayers);
			disFile.save(file);
		} catch (IOException e) {
			MELogger.severe("Error on save disabled.yml file!");
			e.printStackTrace();
		}
	}
	
	private void loadDisabled(){
		MEYaml disFile = new MEYaml();
		File file = new File(plugin.getDataFolder(), "disabled.yml");
		if(!file.exists()) {
			MELogger.info("Creating new disabled.yml ...");
			try {
				disFile.set("disabled-for", new ArrayList<String>());
				disFile.save(file);
			} catch (IOException e) {
				MELogger.severe("Error on creating disabled.yml file!");
				e.printStackTrace();
			}
		} else {
			try {
				disFile.load(file);
				if (disFile.getStringList("disabled-for") != null && disFile.getStringList("disabled-for").size() >0){
					for (String player:disFile.getStringList("disabled-for")){
						MinEmojis.DeclinedPlayers.add(player);
					}
				}				
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void saveAll(){
		saveDisabled();
		try {
			config.save(new File(plugin.getDataFolder(), "config.yml"));
			MELogger.info("Player file for disabled players saved!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	private void LoadConfigs(){
		//------------------------------ Add default Values ----------------------------//
		File configs = new File(plugin.getDataFolder(), "config.yml");
		if (!configs.exists()) {
        	plugin.saveResource("config.yml", false);//create config file
        } 
		
        FileConfiguration temp = new MEYaml();
        try {
        	temp.load(configs);
		} catch (Exception e) {
			e.printStackTrace();
		} 
        
        try {
			plugin.getConfig().load(configs);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
        
        config = inputLoader(plugin.getResource("config.yml"));  
        for (String key:config.getKeys(true)){                        	
        	config.set(key, plugin.getConfig().get(key));    	            	   	            	
        }                        
        for (String key:config.getKeys(false)){    
        	plugin.getConfig().set(key, config.get(key));
        }  
        
        plugin.saveConfig();
        //--------------------------------------------------------------------------//
	}
	
	private static MEYaml inputLoader(InputStream inp) {
		MEYaml file = new MEYaml();
		try {
			file.load(new InputStreamReader(inp, StandardCharsets.UTF_8));
			inp.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 		
		return file;
	}
		
	public String getString(String key){
		return config.getString(key);
	}
	
	public List<String> getList(String key){
		return config.getStringList(key);
	}
	
	public Boolean getBool(String key){
		return config.getBoolean(key);
	}
	
	public String getLangString(String key){
		if (config.getString("strings."+key) != null){
			return ChatColor.translateAlternateColorCodes('&', config.getString("strings."+key));
		} else {
			return "§cNot found string on config.yml for §4"+key+"§r";
		}
		
	}
}
