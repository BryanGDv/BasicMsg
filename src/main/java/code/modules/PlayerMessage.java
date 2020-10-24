package code.modules;

import code.Manager;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerMessage{

    private Manager manager;

    public PlayerMessage(Manager manager){
        this.manager = manager;
    }

    public void sendMessage(CommandSender sender, String path, Boolean color, String message) {

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            if (PlaceholderAPI.containsPlaceholders(path)) {
                path = getPlaceholders(sender, path);
            }
        }

        sender.sendMessage(getMessage(path, color).replace("%message%", message));
    }


    public void sendMessage(CommandSender sender, String path) {

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            path = getPlaceholders(sender, path);
        }

        sender.sendMessage(getMessage(path));
    }

    public String getMessage(String message) {

        message = manager.getVariables().replaceString(message);
        
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public String getMessage(String message, Boolean color) {

        message = manager.getVariables().replaceString(message);

        if (color) {
            return ChatColor.translateAlternateColorCodes('&', message);
        }

        return message;
    }
    private String getPlaceholders(CommandSender sender, String path){

        Player player = (Player) sender;
        return PlaceholderAPI.setPlaceholders(player, path);
    }
}
