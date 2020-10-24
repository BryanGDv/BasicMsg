package code.modules;

import code.CacheManager;
import code.Manager;
import java.util.Set;
import java.util.UUID;

public class MsgToggleMethod implements MethodService{

    private final Manager manager;
    private final CacheManager cache;

    private final Set<UUID> msgtoggle;

    public MsgToggleMethod(Manager manager) {
        this.manager = manager;
        this.cache = manager.getCache();
        this.msgtoggle = cache.getMsgToggle();

    }

    public void set(UUID uuid) {
        msgtoggle.add(uuid);
    }


    public void unset(UUID uuid){
        msgtoggle.remove(uuid);

    }

}
