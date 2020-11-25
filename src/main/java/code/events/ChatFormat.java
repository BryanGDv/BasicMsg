package code.events;

import code.Manager;
import code.bukkitutils.WorldManager;
import code.cache.UserCache;
import code.modules.GroupMethod;
import code.modules.ListenerManaging;
import code.modules.StaffChatMethod;
import code.modules.click.ChatMethod;
import code.modules.player.PlayerMessage;
import code.modules.player.PlayerStatic;
import code.revisor.RevisorManager;
import code.utils.Configuration;
import code.utils.HoverManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChatFormat implements Listener {

    private final Manager manager;


    public ChatFormat(Manager manager) {
        this.manager = manager;
        manager.getListManager().getModules().add("chat_format");
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {

        Player player = event.getPlayer();

        Configuration config = manager.getFiles().getConfig();
        Configuration command = manager.getFiles().getCommand();
        Configuration utils = manager.getFiles().getBasicUtils();

        PlayerMessage playersender = manager.getPlayerMethods().getSender();
        UserCache playerStatus = manager.getCache().getPlayerUUID().get(player.getUniqueId());

        StaffChatMethod staffChatMethod = manager.getPlayerMethods().getStaffChatMethod();

        event.setCancelled(true);

        manager.getPlugin().getLogger().info(String.valueOf(playerStatus.isClickMode()));

        if (playerStatus.isClickMode()) {
            return;
        }

        if (manager.getPathManager().isCommandEnabled("staffchat")) {

            ChatMethod chatMethod = manager.getPlayerMethods().getChatMethod();

            if (staffChatMethod.isUsingStaffSymbol(event)) {
                staffChatMethod.getStaffSymbol(event);
                return;
            }

            if (playerStatus.isStaffchatMode()) {

                if (playerStatus.isClickMode()) {
                    chatMethod.unset(player.getUniqueId());
                }

                for (Player playeronline : Bukkit.getServer().getOnlinePlayers()) {
                    UserCache onlineCache = manager.getCache().getPlayerUUID().get(playeronline.getUniqueId());

                    if (onlineCache.isStaffchatMode()) {
                        playersender.sendMessage(playeronline.getPlayer(), command.getString("commands.staff-chat.message")
                                .replace("%player%", player.getName())
                                .replace("%message%", event.getMessage()));
                    }
                    return;
                }
                return;
            }
        }


        if (!(manager.getPathManager().isOptionEnabled("chat_format"))) {
            return;

        }
        if (!(utils.getBoolean("utils.chat.enabled"))) {
            return;
        }

        String messagerevised = RevisorManager.revisor(event.getPlayer().getUniqueId(), event.getMessage());

        if (messagerevised == null) {
            return;
        }

        GroupMethod groupChannel = manager.getPlayerMethods().getGroupMethod();

        String message;
        String utilspath = groupChannel.getPlayerFormat(player);

        if (player.hasPermission(config.getString("config.perms.chat-color"))) {
            message = PlayerStatic.setColor(utilspath
                    .replace("%player%", player.getName())
                    .replace("%message%", messagerevised));

        } else {
            message = PlayerStatic.setColor(utilspath
                            .replace("%player%", player.getName())
                    , messagerevised);
        }

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            message = PlayerStatic.setVariables(player, message);
        }

        List<String> utilshover = groupChannel.getPlayerHover(player);

        HoverManager hover = new HoverManager(message);
        String hovertext = hover.hoverMessage(player, utilshover);

        String commandtext = groupChannel.getPlayerCmd(player)
                .replace("%player%", player.getName());
        hover.setHover(hovertext, commandtext);

        List<Player> playerList = null;

        if (utils.getBoolean("utils.chat.per-world-chat.enabled")) {
            event.getRecipients().clear();

            if (utils.getBoolean("utils.chat.per-world-chat.all-worlds")) {
                for (String worldname : WorldManager.getAllWorldChat()) {
                    if (player.getWorld().getName().equalsIgnoreCase(worldname)) {
                        World world = Bukkit.getWorld(worldname);
                        playerList = new ArrayList<>(world.getPlayers());

                    }
                }

            } else {
                playerList = new ArrayList<>();
                for (String worldname : WorldManager.getWorldChat(player.getWorld().getName())) {
                    World world = Bukkit.getWorld(worldname);
                    playerList.addAll(world.getPlayers());
                }
            }
        }else{
            playerList = new ArrayList<>(Bukkit.getServer().getOnlinePlayers());
        }

        if (playerList == null){
            manager.getPlugin().getLogger().info("How you came here?" + utils.getBoolean("utils.chat.per-world-chat.enabled"));
            return;
        }


        Iterator<Player> playerIterator = playerList.iterator();
            while (playerIterator.hasNext()){
                Player playerchannel = playerIterator.next();

                UserCache playerCache = manager.getCache().getPlayerUUID().get(playerchannel.getUniqueId());
                if (!playerCache.equalsChannelGroup(playerStatus.getChannelGroup())) {
                    playerIterator.remove();
                }
            }

        event.getRecipients().addAll(playerList);

        for (Player recipient : event.getRecipients()) {
            recipient.spigot().sendMessage(hover.getTextComponent());
        }


    }

}
