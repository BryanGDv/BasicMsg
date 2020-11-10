package code.revisor;

import code.CacheManager;
import code.Manager;
import code.utils.Configuration;
import com.google.common.base.Strings;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class AntiFloodRevisor {

    private Manager manager;
    private static Configuration utils;
    private static Plugin plugin;

    private static int minflood;

    public static final String LETTERS = "AaBbCcDdEeFfGgHhIiJjKkMmNnLlOoPpQqRrSsTtUuVvWwXxYyZz0123456789";

    public AntiFloodRevisor(Manager manager){
        this.manager = manager;
        plugin = manager.getPlugin();
        utils = manager.getFiles().getBasicUtils();
        minflood = Math.max(0, utils.getInt("utils.chat.security.anti-flood.min-chars"));
    }

    public static String revisor(String string){

        if (!(utils.getBoolean("utils.chat.security.anti-flood.enabled"))){
            return string;
        }

        String stringflooded = string;

        for (char letter : LETTERS.toCharArray()){
            for(int count = 50; count > minflood; count--){

                String letterchanged = String.valueOf(letter);

                stringflooded = stringflooded
                        .replace(Strings.repeat(letterchanged, count), letterchanged);
            }
        }

        return stringflooded;

    }

}
