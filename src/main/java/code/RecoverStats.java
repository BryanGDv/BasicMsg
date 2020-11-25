package code;

import code.cache.UserCache;
import code.utils.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RecoverStats {

    private final Manager manager;

    public RecoverStats(Manager manager){
        this.manager = manager;
        setup();
    }


    public void setup(){
        Configuration players = manager.getFiles().getPlayers();
        Map<UUID, UserCache> hashMap = manager.getCache().getPlayerUUID();

        for (Player player : Bukkit.getServer().getOnlinePlayers()) {

            UUID playeruuid = player.getUniqueId();
            List<String> ignoredlist = players.getStringList("players." + playeruuid + ".players-ignored");

            hashMap.put(playeruuid, new UserCache(playeruuid));

            if (ignoredlist == null){
                return;
            }

            Map<UUID, List<String>> playerIgnored = manager.getCache().getIgnorelist();
            playerIgnored.put(playeruuid, ignoredlist);
        }
    }
}
