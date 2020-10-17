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
    public void sendMessage(CommandSender sender, String path) {


        String message;

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            message = PlaceholderAPI.setPlaceholders((Player) sender , path);

        }else{
            message = path;
        }
        sender.sendMessage(this.getMessage(message));
    }
    public String getMessage(String message) {

        message = manager.getVariables().replaceString(message);
        return ChatColor.translateAlternateColorCodes('&', message);

    }

}
