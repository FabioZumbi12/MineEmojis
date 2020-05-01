package br.net.fabiozumbi12.MinEmojis;

import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MEConfig {

    private static MinEmojis plugin;
    private MEYaml config;

    public MEConfig(MinEmojis pl) {
        plugin = pl;
        config = new MEYaml();
        LoadConfigs();
        loadDisabled();
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

    public void reload() {
        LoadConfigs();
        loadDisabled();
        saveAll();
    }

    private void saveDisabled() {
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

    private void loadConfigMojis() {
        MEYaml disFile = new MEYaml();
        File file = new File(plugin.getDataFolder(), "emojis.yml");
        if (!file.exists()) {
            MELogger.info("Creating new emojis.yml ...");
            try {
                plugin.saveResource("emojis.yml", false);
                disFile = inputLoader(plugin.getResource("emojis.yml"));
            } catch (Exception e) {
                MELogger.severe("Error on creating emojis.yml file!");
                e.printStackTrace();
            }
        } else {
            try {
                disFile.load(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        HashMap<List<String>, String> NewEmojis = new HashMap<>();

        for (List<String> aliases : MinEmojis.emojis.keySet()) {
            List<String> newAliases = new ArrayList<>();
            for (String alias : aliases) {
                if (alias.startsWith(":") && alias.endsWith(":")) {
                    if (!disFile.contains("emoji-aliases." + alias.replace(":", ""))) {
                        disFile.set("emoji-aliases." + alias.replace(":", ""), "");
                    } else {
                        newAliases.add(alias);
                        for (String nAlias : disFile.getString("emoji-aliases." + alias.replace(":", "")).split(",")) {
                            if (!nAlias.isEmpty()) {
                                newAliases.add(nAlias.replace(" ", ""));
                            }
                        }
                    }
                }
            }
            if (!newAliases.isEmpty()) {
                String Symbol = MinEmojis.emojis.get(aliases);
                NewEmojis.put(newAliases, Symbol);
            }
        }

        if (!NewEmojis.isEmpty()) {
            MinEmojis.emojis.clear();
            MinEmojis.emojis.putAll(NewEmojis);
        }

        try {
            disFile.options().header("--------------------- Emojis aliases configuration ---------------------\n"
                    + "Remember to always add the ' ' around your aliases.\n"
                    + "The plugin may remove the ' ' or add quotation marks.\n"
                    + "If you want to add more than one alias use commas, like the emoji 'sob'.\n"
                    + "-------------------------------------------------------------------------\n");
            disFile.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadDisabled() {
        MEYaml disFile = new MEYaml();
        File file = new File(plugin.getDataFolder(), "disabled.yml");
        if (!file.exists()) {
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
                if (disFile.getStringList("disabled-for").size() > 0) {
                    MinEmojis.DeclinedPlayers.addAll(disFile.getStringList("disabled-for"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void saveAll() {
        saveDisabled();
        try {
            config.save(new File(plugin.getDataFolder(), "config.yml"));
            MELogger.info("Player file for disabled players saved!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void LoadConfigs() {
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
        for (String key : config.getKeys(true)) {
            config.set(key, plugin.getConfig().get(key));
        }
        for (String key : config.getKeys(false)) {
            plugin.getConfig().set(key, config.get(key));
        }

        //set aliases from configuration
        loadConfigMojis();

        plugin.saveConfig();
        //--------------------------------------------------------------------------//
    }

    public boolean getBool(String key) {
        return config.getBoolean(key);
    }

    public List<String> getList(String key) {
        return config.getStringList(key);
    }

    public String getLangString(String key) {
        return ChatColor.translateAlternateColorCodes('&',
                config.getString("strings." + key, "&cNot found string on config.yml for &4" + key + "&r"));

    }
}
