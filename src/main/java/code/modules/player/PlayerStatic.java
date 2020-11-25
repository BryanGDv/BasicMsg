package code.modules.player;

import code.Manager;
import code.utils.VariableManager;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class PlayerStatic {

    private static VariableManager variable;
    private Manager manager;

    public PlayerStatic(Manager manager) {
        this.manager = manager;
        variable = manager.getVariables();
    }

    public static String setColor(String path){
        return ChatColor.translateAlternateColorCodes('&', path);
    }

    public static String setColor(String path, String except){
        return ChatColor.translateAlternateColorCodes('&', path)
                .replace("%message%", except);
    }

    public static String setVariables(Player player, String path){
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            return PlaceholderAPI.setPlaceholders(player, path);
        }
        return path;
    }

    public static String setFormat(Player player, String path){
        path = setVariables(player, path);
        path = variable.replaceString(path);

        return PlayerStatic.setColor(path);
    }
    public static List<String> setPlaceholdersList(Player player, List<String> stringList) {
        stringList.replaceAll(text -> PlayerStatic.setFormat(player, text));

        return stringList;
    }

    public static List<String> setPlaceholdersList(Player player, List<String> stringList, Boolean serverreplaces) {
        if (serverreplaces){
            stringList.replaceAll(text -> PlayerStatic.setFormat(player, text)
                    .replace("%playername%", player.getName()));
        } else {
            stringList.replaceAll(text -> PlayerStatic.setFormat(player, text));
        }
        return stringList;
    }

    public static List<String> setColorList( List<String> stringList){
        stringList.replaceAll(PlayerStatic::setColor);

        return stringList;
    }
}
