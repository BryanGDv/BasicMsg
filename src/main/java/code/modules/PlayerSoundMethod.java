package code.modules;

import code.CacheManager;
import code.Manager;

import java.util.Set;
import java.util.UUID;

public class PlayerSoundMethod implements MethodService {

    private final Manager manager;
    private final CacheManager cache;

    private final Set<UUID> playersounds;

    public PlayerSoundMethod(Manager manager){
        this.manager = manager;
        this.cache = manager.getCache();
        this.playersounds = cache.getPlayerSounds();
    }

    public void set(UUID uuid) {
        playersounds.add(uuid);
    }

    public void unset(UUID uuid) {
        playersounds.remove(uuid);
    }
}
