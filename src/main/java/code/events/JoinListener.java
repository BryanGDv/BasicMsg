package code.events;

import code.CacheManager;
import code.Manager;
import code.cache.UserCache;
import code.modules.ListenerManaging;
import code.utils.Configuration;
import code.utils.PathManager;
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
    private final Configuration sounds;

    private final CacheManager cache;

    public JoinListener(Manager manager) {
        this.manager = manager;
        // All methods:
        this.players = manager.getFiles().getPlayers();
        this.sounds = manager.getFiles().getSounds();
        this.cache = manager.getCache();
    }
    @EventHandler
    public boolean onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        ListenerManaging listenerManaging = manager.getPlayerMethods().getListenerManaging();

        Configuration config = manager.getFiles().getConfig();
        PathManager pathManager = manager.getPathManager();

        if (pathManager.isOptionEnabled("join_quit")){
            listenerManaging.setJoin(event);
        }

        if (cache.getPlayerUUID().get(uuid) == null){
            cache.getPlayerUUID().put(uuid, new UserCache(uuid));
        }

        if (player.hasPermission(config.getString("config.perms.helpop-watch"))){
            cache.getPlayerUUID().get(uuid).toggleHelpOp(true);
        }

        if (pathManager.isCommandEnabled("ignore")){
            Map<UUID, List<String>> ignorelist = cache.getIgnorelist();
            List<String> playerlist = players.getStringList("players." + player.getUniqueId().toString() + ".players-ignored");

            if (!ignorelist.containsKey(uuid) && (!(playerlist.isEmpty()))) {
                ignorelist.put(uuid, playerlist);
            }
        }

        return true;
    }
}
