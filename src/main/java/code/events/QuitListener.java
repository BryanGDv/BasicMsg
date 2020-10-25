package code.events;

import code.CacheManager;
import code.Manager;
import code.modules.player.PlayerMessage;
import code.utils.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class QuitListener implements Listener{

    private final Manager manager;

    public QuitListener(Manager manager){
        this.manager = manager;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        CacheManager cache = manager.getCache();
        PlayerMessage player = manager.getPlayerMethods().getSender();
        Configuration command = manager.getFiles().getCommand();

        Map<UUID, UUID> reply = cache.getReply();
        Set<UUID> socialspy  = cache.getSocialSpy();
        Set<UUID> msgtoggle = cache.getMsgToggle();
        Set<UUID> playersound = cache.getPlayerSounds();

        Player you = event.getPlayer();

        if (reply.containsKey(you.getUniqueId())) {
            OfflinePlayer target = Bukkit.getPlayer(reply.get(you.getUniqueId()));

            if (target != null) {
                if (reply.containsValue(you.getUniqueId())) {
                    reply.remove(target.getUniqueId());
                    player.sendMessage(target.getPlayer(), command.getString("commands.msg-toggle.left")
                            .replace("%player%", target.getName())
                            .replace("%arg-1%", event.getPlayer().getName()));

                }

            }
            reply.remove(you.getUniqueId());

        }
        if (socialspy.contains(you.getUniqueId())) {
            socialspy.remove(you.getUniqueId());
        }
        if (msgtoggle.contains(you.getUniqueId())){
            msgtoggle.remove(you.getUniqueId());
        }
        if (playersound.contains(you.getUniqueId())){
            playersound.remove(you.getUniqueId());
        }

    }

}
