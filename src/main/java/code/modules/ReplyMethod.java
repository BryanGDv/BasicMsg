package code.modules;

import code.CacheManager;
import code.Manager;
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

        Map<UUID, UUID> reply = cache.getReply();

        if (!(reply.containsKey(player))) {
            reply.put(player, target);
            reply.put(target, player);
        } else {
            reply.replace(player, target);
            reply.replace(target, player);
        }
    }
}
