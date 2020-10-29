package code.events;

import code.Manager;
import code.modules.player.PlayerStatic;
import code.utils.Configuration;
import code.utils.HoverManager;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.material.PressurePlate;

import java.awt.*;
import java.util.List;

public class ChatFormat implements Listener{

    private final Manager manager;


    public ChatFormat(Manager manager){
        this.manager = manager;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {

        Player player = event.getPlayer();

        Configuration config = manager.getFiles().getConfig();
        Configuration utils = manager.getFiles().getBasicUtils();

        if (!(utils.getBoolean("utils.chat.enabled"))){
            return;
        }

        event.setCancelled(true);

        String message;
        String utilspath = utils.getString("utils.chat.format");


        if (player.hasPermission(config.getString("config.perms.chat-color"))){
            message = PlayerStatic.setColor(utilspath
                    .replace("%message%", event.getMessage())
                    .replace("%player%", player.getName()));

        }else{
            message = PlayerStatic.setColor(utilspath, event.getMessage())
                    .replace("%player%", player.getName());
        }

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            message = PlayerStatic.setVariables(player, message);
            Bukkit.getServer().getLogger().info("Papi.true");
        }

        HoverManager hover = new HoverManager(message);

        List<String> utilshover = utils.getStringList("utils.chat.hover");

        String hovertext = hover.hoverMessage(player, utilshover);

        hover.setHover(hovertext);

         for (Player recipient : event.getRecipients()){
             recipient.spigot().sendMessage(hover.getTextComponent());
         }

    }


}
