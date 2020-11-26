package code.methods.player;

import code.Manager;
import code.revisor.RevisorManager;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerMessage{

    private Manager manager;

    public PlayerMessage(Manager manager){
        this.manager = manager;
    }

    public void sendMessage(CommandSender sender, String path, String message, Boolean revisor) {

        Player player = (Player) sender;

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            if (PlaceholderAPI.containsPlaceholders(path)) {
                path = getPlaceholders(sender, path);
            }
        }

        if (revisor){
            message = RevisorManager.revisor(player.getUniqueId(), message);
            if (message == null){
                return;
            }

        }


        sender.sendMessage(getMessage(path).replace("%message%", message));
    }


    public void sendMessage(CommandSender sender, String path) {

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            path = getPlaceholders(sender, path);
        }

        sender.sendMessage(getMessage(path));
    }

    public String getMessage(String message) {

        message = manager.getVariables().replaceString(message);
        
        return PlayerStatic.setColor(message);
    }

    private String getPlaceholders(CommandSender sender, String path){

        Player player = (Player) sender;
        return PlayerStatic.setVariables(player, path);
    }
}
