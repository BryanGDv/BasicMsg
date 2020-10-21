package code.modules;

import code.CacheManager;
import code.Manager;
import java.util.Set;
import java.util.UUID;

public class MsgToggleMethod {

    private final Manager manager;
    private final CacheManager cache;

    private final Set<UUID> msgtoggle;

    public MsgToggleMethod(Manager manager) {
        this.manager = manager;
        this.cache = manager.getCache();
        this.msgtoggle = cache.getMsgToggle();

    }

    public void on(UUID uuid) {
        msgtoggle.add(uuid);
    }


    public void off(UUID uuid){
        msgtoggle.remove(uuid);

    }

}
