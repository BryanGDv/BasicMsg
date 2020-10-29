package code.modules.player;

import code.utils.VariableManager;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PlayerStatic{

    private static VariableManager variable;

    public PlayerStatic(VariableManager variable){
        PlayerStatic.variable = variable;
    }
    public static String setColor(String path){
        return ChatColor.translateAlternateColorCodes('&', path);
    }

    public static String setColor(String path, String except){
        return ChatColor.translateAlternateColorCodes('&', path).replace("%message%", except);
    }

    public static String setVariables(Player player, String path){
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            return PlaceholderAPI.setPlaceholders(player, path);
        }
        return path;
    }
}
