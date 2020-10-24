package code.modules;

import code.Manager;
import code.CacheManager;

import java.util.Set;
import java.util.UUID;

public class SocialSpyMethod implements MethodService{

    private final Manager manager;
    private final CacheManager cache;

    private final Set<UUID> socialspy;

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

        if (socialspy.contains(uuid)) {
            socialspy.remove(uuid);
            status = manager.getFiles().getCommand().getString("commands.socialspy.player.variable-off");

        }else{
            socialspy.add(uuid);
            status = manager.getFiles().getCommand().getString("commands.socialspy.player.variable-on");
        }
    }

    public void set(UUID uuid){
        socialspy.add(uuid);
    }

    public void unset(UUID uuid){
        socialspy.remove(uuid);
    }

    public Set<UUID> getVariable(){
        return socialspy;
    }
}
