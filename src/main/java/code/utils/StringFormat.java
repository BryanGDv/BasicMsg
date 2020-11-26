package code.utils;

import code.Manager;
import code.methods.player.PlayerMessage;
import code.registry.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class StringFormat {

    private final ConfigManager config;
    private final Manager manager;

    public StringFormat(ConfigManager config, Manager manager){
        this.config = config;
        this.manager = manager;
    }

    public void loopString(CommandSender sender, Configuration config, String string){
        PlayerMessage player = manager.getPlayerMethods().getSender();
        for (String msg : config.getStringList(string)){
            player.sendMessage(sender, msg);
        }
    }

    public String replaceString(String string){
        return string
                .replace(config.getConfig().getString("config.p-variable"), config.getConfig().getString("config.prefix"))
                .replace(config.getConfig().getString("config.e-variable"), config.getConfig().getString("config.error"));
    }

    public String replacePlayerVariables(Player player, String string){
        return string
                // Player stats:
                .replace("%player%", player.getName())
                .replace("%displayname%", player.getDisplayName())
                .replace("%world%", player.getWorld().getName())

                // Level stats:
                .replace("%health%", String.valueOf(player.getHealth()))
                .replace("%maxhealth%", String.valueOf(player.getMaxHealth()))
                .replace("%foodlevel%", String.valueOf(player.getFoodLevel()))

                // Server stats:
                .replace("%online%", String.valueOf(Bukkit.getServer().getOnlinePlayers().size()))
                .replace("%servername%", Bukkit.getServer().getServerName())
                .replace("%ip%", Bukkit.getIp());

    }
}
