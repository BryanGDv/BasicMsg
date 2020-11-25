package code.api;

import code.CacheManager;
import code.Manager;
import code.cache.UserCache;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.*;

public class BasicAPI{

    private final Manager manager;

    private final CacheManager cache;

    private final Map<UUID, UserCache> userCacheMap;


    private final String pluginname = "BasicMsg";
    private final String pluginversion = "1.0";
    private final String pluginauthor = "BryanGaming";


    public BasicAPI(Manager manager){
        this.manager = manager;
        this.cache = manager.getCache();
        this.userCacheMap = manager.getCache().getPlayerUUID();
    }

    public boolean isPlayerIgnored(UUID targetuuid, UUID playerignored){
        OfflinePlayer player = Bukkit.getPlayer(playerignored);
        if (player == null){
            return false;
        }

        Map<UUID, List<String>> ignorelist = cache.getIgnorelist();
        if (ignorelist.containsKey(targetuuid)){
            return false;
        }
        List<String> ignoredplayers = ignorelist.get(targetuuid);

        for(String ignoreplayers : ignoredplayers){
            if (ignoreplayers.contains(player.getName())){
                return true;
            }
        }
        return false;
    }


    public List<String> getIgnoredPlayers(UUID uuid){
        if (!(cache.getIgnorelist().containsKey(uuid))){
            return null;
        }
        return cache.getIgnorelist().get(uuid);
    }

    public List<String> getSpyList(){
        List<String> socialspyList = new ArrayList<>();

        for (UserCache cache : userCacheMap.values()) {
            if (cache.isSocialSpyMode()) {
                socialspyList.add(cache.getPlayer().getName());
            }
        }
        return socialspyList;

    }

    public List<String> getMsgToggleList(){
        List<String> msgToggle = new ArrayList<>();

        for (UserCache cache : userCacheMap.values()) {
            if (cache.isMsgtoggleMode()) {
                msgToggle.add(cache.getPlayer().getName());
            }
        }

        return msgToggle;
    }

    public UUID getRepliedPlayer(UUID uuid){
        return userCacheMap.get(uuid).getRepliedPlayer();
    }

    public String getDescription(){
        return pluginname + "_" + pluginversion + "_" + pluginauthor;
    }

    public String getPluginName(){
        return pluginname;
    }

    public String getPluginVersion(){
        return pluginversion;
    }

    public String getPluginAuthor(){
        return pluginauthor;
    }



}