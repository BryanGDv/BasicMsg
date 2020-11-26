package code.listeners;

import code.Manager;
import code.utils.module.ModuleCheck;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import code.utils.Configuration;

import java.util.List;


public class ChatListener implements Listener{


    private final Manager manager;

    public ChatListener (Manager manager){
        this.manager = manager;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {

        Configuration players = manager.getFiles().getPlayers();
        ModuleCheck moduleCheck = manager.getPathManager();

        String sender = event.getPlayer().getName();

        if (moduleCheck.isCommandEnabled("ignore")) {

            if (!(players.contains("players"))) return;

            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                List<String> ignorelist = players.getStringList("players." + player.getUniqueId().toString() + ".players-ignored");

                if (ignorelist.contains(sender)) {

                    event.getRecipients().remove(player);
                }
            }
        }



    }

}