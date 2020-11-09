package code;

import code.debug.DebugLogger;
import code.utils.Configuration;


import java.util.*;

public class CacheManager{

    private final Map<UUID, List<String>> ignoreCache = new HashMap<>();
    private final Map<UUID, UUID> replyCache = new HashMap<>();
    private final Map<String, Configuration> config = new HashMap<>();


    private final Set<UUID> playercooldown = new HashSet<>();
    private final Set<UUID> socialSpyCache = new HashSet<>();
    private final Set<UUID> msgtoggleCache = new HashSet<>();
    private final Set<UUID> playerSounds = new HashSet<>();

    private final Manager manager;


    public CacheManager(Manager manager){
        this.manager = manager;
        DebugLogger debug = manager.getLogs();
        debug.log("IgnoreCache loaded");
        debug.log("ReplyCache loaded!");
        debug.log("SocialspyCache loaded!");
    }
    public Set<UUID> getPlayercooldown(){
        return playercooldown;
    }

    public Map<UUID, List<String>> getIgnorelist(){
        return ignoreCache;
    }

    public Map<String, Configuration> getConfigFiles(){
        return config;
    }

    public Map<UUID, UUID> getReply(){
        return replyCache;
    }

    public Set<UUID> getSocialSpy(){
        return socialSpyCache;
    }

    public Set<UUID> getMsgToggle(){
        return msgtoggleCache;
    }

    public Set<UUID> getPlayerSounds(){
        return playerSounds;
    }
}
