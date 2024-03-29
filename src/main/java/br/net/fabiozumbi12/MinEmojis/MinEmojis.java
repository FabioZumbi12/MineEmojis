package br.net.fabiozumbi12.MinEmojis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.inventivetalent.rpapi.ResourcePackAPI;

import br.net.fabiozumbi12.MinEmojis.Fanciful.FancyMessage;

public class MinEmojis extends JavaPlugin implements Listener {

    public static Server serv;
    public static PluginDescriptionFile pdf;
    public static PluginManager pm;
    public static MinEmojis plugin;
    public static MEConfig config;
    static HashMap<List<String>, String> emojis = new HashMap<>();
    static List<String> DeclinedPlayers = new ArrayList<>();
    private static final List<String> installing = new ArrayList<>();
    private final HashMap<String, HashMap<Integer, String>> signPlayers = new HashMap<>();

    public static boolean isInstalling(Player p) {
        return installing.contains(p.getName());
    }

    public static void delInstalling(Player p) {
        installing.remove(p.getName());
    }

    public void onEnable() {
        try {

            Plugin p = Bukkit.getPluginManager().getPlugin("ResourcePackApi");
            boolean RPAPI = p != null && p.isEnabled();
            plugin = this;
            serv = getServer();
            pdf = getDescription();
            pm = serv.getPluginManager();
            AddEmojis();
            config = new MEConfig(plugin);

            if (getBukkitVersion() >= 188) {
                if (RPAPI) {
                    MELogger.warning("ResourcePackApi detected but after version 1.8.8 is not necessary. You can remove securely!");
                }
                pm.registerEvents(new MEListener188(), this);
            } else {
                if (!RPAPI) {
                    MELogger.severe("Not found the dependency ResourcePackAPI required for version < 1.8.8!");
                    Bukkit.getPluginManager().disablePlugin(this);
                    return;
                }
                pm.registerEvents(new MEListenerRPA(), this);
            }

            MELogger.sucess(pdf.getFullName() + " enabled. (" + getBukkitVersion() + ")");

            Bukkit.getPluginManager().registerEvents(this, this);
        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    private void AddEmojis() {
        emojis.put(new ArrayList<>(Collections.singletonList(":blush:")), "䰀");
        emojis.put(new ArrayList<>(Collections.singletonList(":smile:")), "䰁");
        emojis.put(new ArrayList<>(Collections.singletonList(":rage:")), "䰂");
        emojis.put(new ArrayList<>(Collections.singletonList(":anguished:")), "䰃");
        emojis.put(new ArrayList<>(Collections.singletonList(":heart:")), "䰄");
        emojis.put(new ArrayList<>(Collections.singletonList(":broken_heart:")), "䰅");
        emojis.put(new ArrayList<>(Collections.singletonList(":heart_eyes:")), "䰆");
        emojis.put(new ArrayList<>(Collections.singletonList(":kissing_heart:")), "䰇");
        emojis.put(new ArrayList<>(Collections.singletonList(":sob:")), "䰈");
        emojis.put(new ArrayList<>(Collections.singletonList(":sunglasses:")), "䰉");
        emojis.put(new ArrayList<>(Collections.singletonList(":unamused:")), "䰊");
        emojis.put(new ArrayList<>(Collections.singletonList(":zzz:")), "䰋");
        emojis.put(new ArrayList<>(Collections.singletonList(":dizzy_face:")), "䰌");
        emojis.put(new ArrayList<>(Collections.singletonList(":expressionless:")), "䰍");
        emojis.put(new ArrayList<>(Collections.singletonList(":wink:")), "䰎");
        emojis.put(new ArrayList<>(Collections.singletonList(":worried:")), "䰏");

        emojis.put(new ArrayList<>(Collections.singletonList(":stuck_out_tongue_closed_eyes:")), "䰐");
        emojis.put(new ArrayList<>(Collections.singletonList(":yum:")), "䰑");
        emojis.put(new ArrayList<>(Collections.singletonList(":open_mouth:")), "䰒");
        emojis.put(new ArrayList<>(Collections.singletonList(":flushed:")), "䰓");
        emojis.put(new ArrayList<>(Collections.singletonList(":confused:")), "䰔");
        emojis.put(new ArrayList<>(Collections.singletonList(":sweat:")), "䰕");
        emojis.put(new ArrayList<>(Collections.singletonList(":star:")), "䰖");
        emojis.put(new ArrayList<>(Collections.singletonList(":cupid:")), "䰗");
        emojis.put(new ArrayList<>(Collections.singletonList(":shit:")), "䰘");
        emojis.put(new ArrayList<>(Collections.singletonList(":thumbsup:")), "䰙");
        emojis.put(new ArrayList<>(Collections.singletonList(":thumbsdown:")), "䰚");
        emojis.put(new ArrayList<>(Collections.singletonList(":punch:")), "䰛");
        emojis.put(new ArrayList<>(Collections.singletonList(":ok_hand:")), "䰜");
        emojis.put(new ArrayList<>(Collections.singletonList(":raised_hands:")), "䰝");
        emojis.put(new ArrayList<>(Collections.singletonList(":kiss:")), "䰞");
        emojis.put(new ArrayList<>(Collections.singletonList(":eyes:")), "䰟");

        emojis.put(new ArrayList<>(Collections.singletonList(":trollface:")), "䰠");
        emojis.put(new ArrayList<>(Collections.singletonList(":scream:")), "䰡");
        emojis.put(new ArrayList<>(Collections.singletonList(":smiling_imp:")), "䰢");
        emojis.put(new ArrayList<>(Collections.singletonList(":imp:")), "䰣");
        emojis.put(new ArrayList<>(Collections.singletonList(":innocent:")), "䰤");
        emojis.put(new ArrayList<>(Collections.singletonList(":musical_note:")), "䰥");
        emojis.put(new ArrayList<>(Collections.singletonList(":star2:")), "䰦");
        emojis.put(new ArrayList<>(Collections.singletonList(":sparkles:")), "䰧");
        emojis.put(new ArrayList<>(Collections.singletonList(":boom:")), "䰨");
        emojis.put(new ArrayList<>(Collections.singletonList(":blue_heart:")), "䰩");
        emojis.put(new ArrayList<>(Collections.singletonList(":fire:")), "䰪");
        emojis.put(new ArrayList<>(Collections.singletonList(":point_up:")), "䰫");
        emojis.put(new ArrayList<>(Collections.singletonList(":point_down:")), "䰬");
        emojis.put(new ArrayList<>(Collections.singletonList(":point_left:")), "䰭");
        emojis.put(new ArrayList<>(Collections.singletonList(":point_right:")), "䰮");
        emojis.put(new ArrayList<>(Collections.singletonList(":pray:")), "䰯");
    }

    public void onDisable() {
        config.saveAll();
        MELogger.severe(pdf.getFullName() + " disabled.");
    }

    private int getBukkitVersion() {
        String name = Bukkit.getServer().getClass().getPackage().getName();
        String v = name.substring(name.lastIndexOf('.') + 1) + ".";
        String[] version = v.replace('_', '.').split("\\.");

        int lesserVersion = 0;
        try {
            lesserVersion = Integer.parseInt(version[2]);
        } catch (NumberFormatException ignored) {
        }
        return Integer.parseInt((version[0] + version[1]).substring(1) + lesserVersion);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        List<String> tab = new ArrayList<>();
        if (cmd.getName().equalsIgnoreCase("MinEmojis") && args.length == 1) {
            for (List<String> emojis : MinEmojis.emojis.keySet()) {
                for (String emoji : emojis) {
                    if (emoji.startsWith(":") && emoji.endsWith(":")) {
                        tab.add(emoji);
                    }
                }
            }
        }
        return tab;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(config.getLangString("plugin-tag") + "§aHelp Page:");
            if (sender.hasPermission("minemojis.install")) {
                sender.sendMessage(config.getLangString("commands.install").replace("{cmd}", lbl));
            }
            if (sender.hasPermission("minemojis.download")) {
                sender.sendMessage(config.getLangString("commands.download").replace("{cmd}", lbl));
            }
            if (sender.hasPermission("minemojis.enable")) {
                sender.sendMessage(config.getLangString("commands.enable").replace("{cmd}", lbl));
            }
            if (sender.hasPermission("minemojis.list")) {
                sender.sendMessage(config.getLangString("commands.list").replace("{cmd}", lbl));
            }
            if (sender.hasPermission("minemojis.setsign")) {
                sender.sendMessage(config.getLangString("commands.setsign").replace("{cmd}", lbl));
            }
            sender.sendMessage(ChatColor.GRAY + "» " + ChatColor.ITALIC + MinEmojis.pdf.getFullName() + ", developed by FabioZumbi12!");
        }
        if (args.length == 1) {
            if ((args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("enable")) && sender.hasPermission("minemojis.enable")) {
                DeclinedPlayers.remove(sender.getName());
                sender.sendMessage(config.getLangString("plugin-tag") + config.getLangString("emojis.enabled"));
            }
            if ((args[0].equalsIgnoreCase("off") || args[0].equalsIgnoreCase("disable")) && sender.hasPermission("minemojis.enable")) {
                if (!DeclinedPlayers.contains(sender.getName())) {
                    DeclinedPlayers.add(sender.getName());
                }
                sender.sendMessage(config.getLangString("plugin-tag") + config.getLangString("emojis.disabled"));
            }
            if (args[0].equalsIgnoreCase("reload") && sender.hasPermission("minemojis.reload")) {
                config.reload();
                sender.sendMessage(ChatColor.AQUA + MinEmojis.pdf.getFullName() + " reloaded!");
            }
            if (args[0].equalsIgnoreCase("install") && sender instanceof Player && sender.hasPermission("minemojis.install")) {
                Player p = (Player) sender;
                if (getBukkitVersion() >= 188) {
                    p.setResourcePack("https://www.dropbox.com/s/mggt0usjvrrvgj5/MinEmojis.zip?dl=1");
                } else {
                    ResourcePackAPI.setResourcepack(p, "https://www.dropbox.com/s/mggt0usjvrrvgj5/MinEmojis.zip?dl=1");
                }
                installing.add(p.getName());
            }
            if (args[0].equalsIgnoreCase("download") && sender instanceof Player && sender.hasPermission("minemojis.download")) {
                Player p = (Player) sender;
                if (config.getList("download-help-lines") != null && config.getList("download-help-lines").size() > 0) {
                    for (String line : config.getList("download-help-lines")) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
                    }
                }
            }
            if (args[0].equalsIgnoreCase("list") && sender instanceof Player && sender.hasPermission("minemojis.list")) {
                Iterator<List<String>> emojits = MinEmojis.emojis.keySet().iterator();
                sender.sendMessage(config.getLangString("plugin-tag") + config.getLangString("default-color") + " Emojis:");
                try {
                    Class.forName("com.google.gson.JsonParser");
                    FancyMessage message = new FancyMessage();

                    while (emojits.hasNext()) {
                        List<String> shortcuts = emojits.next();
                        String[] emoji = shortcuts.toArray(new String[0]);
                        StringBuilder shortcut = new StringBuilder();
                        for (String shortc : emoji) {
                            shortcut.append(" ").append(config.getLangString("default-color")).append("|§r ").append(shortc);
                        }
                        shortcut = new StringBuilder(shortcut.substring(4));
                        message.text(config.getLangString("default-color") + "|§r" + MinEmojis.emojis.get(shortcuts) + config.getLangString("default-color") + "|§r")
                                .tooltip(MinEmojis.config.getLangString("shortcut") + shortcut)
                                .then(" ");
                    }
                    message.send(sender);
                } catch (ClassNotFoundException e) {
                    StringBuilder send = new StringBuilder();
                    while (emojits.hasNext()) {
                        List<String> shortcuts = emojits.next();
                        String[] emoji = shortcuts.toArray(new String[0]);
                        send.append("|").append(MinEmojis.emojis.get(shortcuts)).append(" = ").append(Arrays.toString(emoji)).append("|");
                    }
                    sender.sendMessage(send.toString());
                }
            }
            for (List<String> emojis : MinEmojis.emojis.keySet()) {
                if (emojis.contains(args[0]) && (sender.hasPermission("minemojis." + args[0]) || sender.hasPermission("minemojis.all"))) {
                    sender.sendMessage(config.getLangString("plugin-tag") + " " + args[0] + "§r = " + MinEmojis.emojis.get(emojis));
                }
            }
        }

        if (args.length >= 3) {
            //emoji setsign <line> <text>
            if (args[0].equalsIgnoreCase("setsign") && sender instanceof Player && sender.hasPermission("minemojis.setsign")) {
                Player p = (Player) sender;
                int line;
                try {
                    line = Integer.parseInt(args[1]);
                    if (line < 1 || line > 4) {
                        sender.sendMessage(config.getLangString("plugin-tag") + config.getLangString("setsign.nolines"));
                        return true;
                    }
                } catch (NumberFormatException e) {
                    sender.sendMessage(config.getLangString("plugin-tag") + config.getLangString("setsign.usage").replace("{cmd}", lbl));
                    return true;
                }
                StringBuilder message = new StringBuilder();
                for (String arg : args) {
                    if (arg.equals(args[0]) || arg.equals(args[1])) {
                        continue;
                    }
                    message.append(arg).append(" ");
                }
                String msg = formatEmoji(p, message.toString().substring(0, message.toString().length() - 1), true);
                HashMap<Integer, String> pmsg = new HashMap<>();
                if (signPlayers.containsKey(p.getName())) {
                    pmsg = signPlayers.get(p.getName());
                }
                pmsg.put(line, msg);
                signPlayers.put(p.getName(), pmsg);
                sender.sendMessage(config.getLangString("plugin-tag") + config.getLangString("setsign.setline-to").replace("{line}", "" + line).replace("{text}", ChatColor.translateAlternateColorCodes('&', msg)));
            }
        }
        return true;
    }

    private String formatEmoji(Player p, String msg, boolean sign) {
        for (List<String> emojis : MinEmojis.emojis.keySet()) {
            for (String emoji : emojis) {
                if (msg.contains(emoji) && (p.hasPermission("minemojis.emoji." + emoji.replace(":", "")) || p.hasPermission("minemojis.emoji.all"))) {
                    String emof = MinEmojis.emojis.get(emojis);
                    msg = msg.replace(emoji, sign ? "&f" + emof + "&r" : emof);
                }
            }
        }
        return msg;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event){
        Player p = event.getPlayer();
        if (config.getBool("config.resourcepack-onplayerjoin")) {
            if (getBukkitVersion() >= 188) {
                p.setResourcePack("https://www.dropbox.com/s/mggt0usjvrrvgj5/MinEmojis.zip?dl=1");
            } else {
                ResourcePackAPI.setResourcepack(p, "https://www.dropbox.com/s/mggt0usjvrrvgj5/MinEmojis.zip?dl=1");
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onInteractSign(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Block b = e.getClickedBlock();
        if (signPlayers.containsKey(p.getName())) {
            if (b != null && b.getState() instanceof Sign) {
                Sign s = (Sign) b.getState();
                for (Integer line : signPlayers.get(p.getName()).keySet()) {
                    s.setLine(line - 1, ChatColor.translateAlternateColorCodes('&', signPlayers.get(p.getName()).get(line)));
                }
                s.update();
            } else {
                p.sendMessage(config.getLangString("plugin-tag") + config.getLangString("setsign.no-sign"));
            }
            signPlayers.remove(p.getName());
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onSignChange(SignChangeEvent e) {
        Player p = e.getPlayer();
        if (p.hasPermission("minemojis.placesign") && !MinEmojis.DeclinedPlayers.contains(p.getName())) {
            for (int i = 0; i < e.getLines().length; i++) {
                e.setLine(i, ChatColor.translateAlternateColorCodes('&', formatEmoji(p, e.getLine(i), true)));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        String chat = e.getMessage();

        if (MinEmojis.DeclinedPlayers.contains(p.getName())) {
            return;
        }
        e.setMessage(formatEmoji(p, chat, false));
    }
}
