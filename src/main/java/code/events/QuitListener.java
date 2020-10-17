package code.events;

import code.CacheManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class QuitListener implements Listener{

    private CacheManager cache;

    public QuitListener(CacheManager cache){
        this.cache = cache;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Map<UUID, UUID> reply = cache.getReply();
        Set<UUID> socialspy  = cache.getSocialSpy();

        if (reply.containsKey(event.getPlayer().getUniqueId())) {
            cache.getReply().remove(event.getPlayer().getUniqueId());

        }if (socialspy.contains(event.getPlayer().getUniqueId())){
            socialspy.remove(event.getPlayer().getUniqueId());
        }

    }

}
