package code.modules;

import code.Manager;
import code.CacheManager;
import code.cache.UserCache;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class SocialSpyMethod implements MethodService{

    private final Manager manager;

    private final Map<UUID, UserCache> cache;

    private String status;

    public SocialSpyMethod(Manager manager) {
        this.manager = manager;
        this.cache = manager.getCache().getPlayerUUID();
    }
    public String getStatus(){
        return status;
    }

    public void toggle(UUID uuid){
        UserCache usercache = cache.get(uuid);

        if (usercache.isSocialSpyMode()) {
            usercache.toggleSocialSpy(false);
            status = manager.getFiles().getCommand().getString("commands.socialspy.player.variable-off");
        }else{
            usercache.toggleSocialSpy(true);
            status = manager.getFiles().getCommand().getString("commands.socialspy.player.variable-on");
        }
        Bukkit.getLogger().info(String.valueOf(usercache.isSocialSpyMode()));
    }

    public void set(UUID uuid){
        cache.get(uuid).toggleSocialSpy(true);
    }

    public void unset(UUID uuid){
        cache.get(uuid).toggleSocialSpy(false);
    }

}
