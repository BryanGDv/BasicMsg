package code.events;

import code.Manager;
import code.bukkitutils.WorldManager;
import code.modules.player.PlayerStatic;
import code.revisor.RevisorManager;
import code.utils.Configuration;
import code.utils.HoverManager;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.World;
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
        }

        List<String> utilshover = utils.getStringList("utils.chat.hover");

        String messagerevised = RevisorManager.revisor(event.getPlayer().getUniqueId(), message);

        if (messagerevised == null){
            event.setCancelled(true);
            return;
        }

        HoverManager hover = new HoverManager(messagerevised);

        String hovertext = hover.hoverMessage(player, utilshover);
        hover.setHover(hovertext);


        if (utils.getBoolean("utils.chat.per-world-chat.enabled")) {
            event.getRecipients().clear();

            if (utils.getBoolean("utils.chat.per-world-chat.all-worlds")) {
                for (String worldname : WorldManager.getAllWorldChat()) {
                    if (player.getWorld().getName().equalsIgnoreCase(worldname)) {
                        World world = Bukkit.getWorld(worldname);
                        event.getRecipients().addAll(world.getPlayers());
                    }
                }


            }else{
                for (String worldname : WorldManager.getWorldChat(player.getWorld().getName())) {
                    World world = Bukkit.getWorld(worldname);
                    event.getRecipients().addAll(world.getPlayers());
                }
            }
        }

        event.setCancelled(true);

        for (Player recipient : event.getRecipients()) {
            recipient.spigot().sendMessage(hover.getTextComponent());
        }


    }


}
