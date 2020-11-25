package code.modules;

import code.Manager;
import code.CacheManager;
import code.cache.UserCache;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class HelpOpMethod implements MethodService{

    private final Manager manager;

    private final Map<UUID, UserCache> cache;

    private String status;

    public HelpOpMethod(Manager manager) {
        this.manager = manager;
        this.cache = manager.getCache().getPlayerUUID();
    }
    public String getStatus(){
        return status;
    }

    public void toggle(UUID uuid){
        UserCache usercache = cache.get(uuid);

        if (usercache.isPlayerHelpOp()) {
            usercache.toggleHelpOp(false);
            status = manager.getFiles().getCommand().getString("commands.helpop.player.variable-off");
        }else{
            usercache.toggleHelpOp(true);
            status = manager.getFiles().getCommand().getString("commands.helpop.player.variable-on");
        }
    }

    public void set(UUID uuid){
        cache.get(uuid).toggleHelpOp(true);
    }

    public void unset(UUID uuid){
        cache.get(uuid).toggleHelpOp(false);
    }

}
