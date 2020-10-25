package code;

import code.Manager;
import code.debug.ErrorManager;
import com.avaje.ebeaninternal.server.idgen.UuidIdGenerator;

import java.util.*;

public class CacheManager{

    private final Map<UUID, List<String>> ignoreCache = new HashMap<>();
    private final Map<UUID, UUID> replyCache = new HashMap<>();

    private final Set<UUID> socialSpyCache = new HashSet<>();
    private final Set<UUID> msgtoggleCache = new HashSet<>();
    private final Set<UUID> playerSounds = new HashSet<>();

    private final Manager manager;


    public CacheManager(Manager manager){
        this.manager = manager;
        ErrorManager debug = manager.getLogs();
        debug.log("IgnoreCache loaded");
        debug.log("ReplyCache loaded!");
        debug.log("SocialspyCache loaded!");
    }

    public Map<UUID, List<String>> getIgnorelist(){
        return ignoreCache;
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
