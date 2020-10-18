package code.events;

import code.CacheManager;
import code.Manager;
import code.utils.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JoinListener implements Listener {

    private final Manager manager;

    private final Configuration players;
    private final CacheManager cache;

    public JoinListener(Manager manager) {
        this.manager = manager;
        // All methods:
        this.players = manager.getFiles().getPlayers();
        this.cache = manager.getCache();
    }
    @EventHandler
    public boolean onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        Map<UUID, List<String>> ignorelist = cache.getIgnorelist();
        List<String> playerlist = players.getStringList("players." + player.getUniqueId().toString() + ".players-ignored");

        if (!ignorelist.containsKey(uuid) && (!(playerlist.isEmpty()))){
            ignorelist.put(uuid, playerlist);
        }
        return true;
    }
}
