package code.revisor;

import code.BasicMsg;
import code.Manager;
import code.bukkitutils.RunnableManager;
import code.modules.player.PlayerMessage;
import code.modules.player.PlayerStatic;
import code.utils.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class LinkRevisor {

    private final Manager manager;
    private static BasicMsg plugin;
    private static RunnableManager runnableManager;
    private static Configuration utils;
    private static PlayerMessage playersender;

    public LinkRevisor(Manager manager){
        this.manager = manager;
        plugin = manager.getPlugin();
        runnableManager = manager.getManagingCenter().getRunnableManager();
        utils = manager.getFiles().getBasicUtils();
        playersender = manager.getPlayerMethods().getSender();
    }

    public static String revisor(Player player, String string){

        if (!(utils.getBoolean("utils.chat.security.link-module.enabled"))){
            return string;
        }

        if (utils.getBoolean("utils.chat.security.link-module.block-point")){
            if (string.contains(".")){
                string = string.replace(".", utils.getString("utils.chat.security.link-module.replace-link"));
                Bukkit.getLogger().info(string);
                sendMessage(player, " . ");

                if (!(player.isOnline())){
                    return null;
                }

                return string;
            }
            return string;
        }


        List<String> blockList = utils.getStringList("utils.chat.security.link-module.blocked-links");

        for (String blockedWord : blockList){
            if (string.contains(blockedWord)){
                string = string.replace(".", utils.getString("utils.chat.security.link-module.replace-lnik"));
                sendMessage(player, blockedWord);

                if (!(player.isOnline())){
                    return null;
                }

                return string;
            }

        }
        return string;
    }

    private static void sendMessage(Player player, String blockedword) {

        playersender.sendMessage(player, utils.getString("utils.chat.security.link-module.message")
                .replace("%player%", player.getName())
                .replace("%blockedword%", blockedword));

        if (!(utils.getBoolean("utils.chat.security.link-module.command.enabled"))) {
            return;
        }


        runnableManager.sendCommand(Bukkit.getServer().getConsoleSender(), utils.getString("utils.chat.security.link-module.command.format")
                    .replace("%player%", player.getName())
                    .replace("%blockedword%", blockedword));
        }



}
