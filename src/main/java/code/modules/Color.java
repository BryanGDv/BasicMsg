package code.modules;

import org.bukkit.ChatColor;

public class Color{

   public static String color(String path){
       return ChatColor.translateAlternateColorCodes('&', path);

   }
}
