package code.modules;

import code.Manager;
import code.commands.cache.CacheManager;
import code.commands.cache.SocialSpyCache;

import java.util.HashSet;
import java.util.UUID;

public class SocialSpyMethod {

    private final Manager manager;
    private final CacheManager cache;

    private SocialSpyCache socialspy;
    private String status;

    public SocialSpyMethod(Manager manager) {
        this.manager = manager;
        this.cache = manager.getCache();
        this.socialspy = cache.getSocialSpy();
    }
    public String getStatus(){
        return status;
    }
    public void toggle(UUID uuid){

        if (socialspy.get().contains(uuid)) {
            socialspy.get().remove(uuid);
            status = manager.getFiles().getCommand().getString("commands.socialspy.player.variable-off");

        }else{
            socialspy.get().add(uuid);
            status = manager.getFiles().getCommand().getString("commands.socialspy.player.variable-on");
        }
    }

    public void set(UUID uuid){
        socialspy.get().add(uuid);
    }

    public void unset(UUID uuid){
        socialspy.get().remove(uuid);
    }

    public HashSet<UUID> getVariable(){
        return socialspy.get();
    }
}
