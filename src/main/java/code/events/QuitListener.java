package code.events;

import code.commands.cache.CacheManager;
import code.commands.cache.ReplyCache;
import code.commands.cache.SocialSpyCache;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener{

    private CacheManager cache;

    public QuitListener(CacheManager cache){
        this.cache = cache;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        ReplyCache reply = cache.getReply();
        SocialSpyCache socialspy  = cache.getSocialSpy();
        if (reply.get().containsKey(event.getPlayer().getUniqueId())) {
            cache.getReply().remove(event.getPlayer().getUniqueId());

        }if (socialspy.get().contains(event.getPlayer().getUniqueId())){
            socialspy.get().remove(event.getPlayer().getUniqueId());
        }

    }

}
