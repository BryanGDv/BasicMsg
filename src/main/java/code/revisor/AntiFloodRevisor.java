package code.revisor;

import code.CacheManager;
import code.Manager;
import code.modules.player.PlayerMessage;
import code.utils.Configuration;
import com.google.common.base.Strings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class AntiFloodRevisor {

    private Manager manager;
    private static Configuration utils;
    private static Plugin plugin;
    private static PlayerMessage playersender;

    private static int minflood;

    public static final String LETTERS = "AaBbCcDdEeFfGgHhIiJjKkMmNnLlOoPpQqRrSsTtUuVvWwXxYyZz0123456789";

    public AntiFloodRevisor(Manager manager){
        this.manager = manager;
        playersender = manager.getPlayerMethods().getSender();
        plugin = manager.getPlugin();
        utils = manager.getFiles().getBasicUtils();
        minflood = Math.max(0, utils.getInt("utils.chat.security.anti-flood.min-chars"));
    }

    public static String revisor(Player player, String string){

        if (!(utils.getBoolean("utils.chat.security.anti-flood.enabled"))){
            return string;
        }
        
        String stringflooded = string;
        int floodstatus = 0;

        for (char letter : LETTERS.toCharArray()){

            for(int count = 50; count > minflood; count--){

                String letterchanged = String.valueOf(letter);

                if (stringflooded.contains(Strings.repeat(letterchanged, count))) {
                    if (floodstatus < 2){
                        playersender.sendMessage(player, utils.getString("utils.chat.security.anti-flood.message"));
                    }

                    stringflooded = stringflooded
                            .replace(Strings.repeat(letterchanged, count), letterchanged);

                    floodstatus++;
                }
            }
        }

        return stringflooded;

    }

}
