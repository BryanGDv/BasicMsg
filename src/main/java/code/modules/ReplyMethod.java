package code.modules;

import code.CacheManager;
import code.Manager;
import code.cache.UserCache;
import org.bukkit.command.CommandSender;

import java.util.Map;
import java.util.UUID;

public class ReplyMethod{

    private Manager manager;
    private CacheManager cache;

    public ReplyMethod(Manager manager) {
        this.manager = manager;
        this.cache = manager.getCache();
    }

    public void setReply(UUID player, UUID target){

        UserCache playerCache = manager.getCache().getPlayerUUID().get(player);
        UserCache targetCache = manager.getCache().getPlayerUUID().get(target);

        playerCache.setRepliedPlayer(target);
        targetCache.setRepliedPlayer(player);
    }
}
