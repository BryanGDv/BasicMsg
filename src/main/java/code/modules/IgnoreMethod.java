package code.modules;

import code.Manager;
import code.CacheManager;
import code.utils.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class IgnoreMethod{

    private final Configuration players;
    private final Configuration messages;

    private final Manager manager;

    private final CacheManager cache;

    private final Map<UUID, List<String>> ignorelist;



    public IgnoreMethod(Manager manager) {
        this.manager = manager;
        // All methods:
        this.cache = manager.getCache();
        this.ignorelist = cache.getIgnorelist();
        this.players = manager.getFiles().getPlayers();
        this.messages = manager.getFiles().getMessages();

    }


    public void set(CommandSender sender, UUID uuid) {

        Player you = (Player) sender;
        UUID playeruuid = you.getUniqueId();

        OfflinePlayer player = Bukkit.getPlayer(uuid);

        List<String> ignoredPlayers;

        if (ignorelist.get(playeruuid) == null) {
            ignoredPlayers = new ArrayList<>();
        } else {
            ignoredPlayers = ignorelist.get(playeruuid);
        }

        ignoredPlayers.add(player.getName());
        ignorelist.put(playeruuid, ignoredPlayers);
        players.set("players." + playeruuid + ".players-ignored", ignoredPlayers);

        players.save();


    }

    public void unset(CommandSender sender, UUID uuid){

        Player you = (Player) sender;
        UUID playeruuid = you.getUniqueId();

        OfflinePlayer target = Bukkit.getPlayer(uuid);

        List<String> ignoredPlayers = ignorelist.get(playeruuid);
        ignoredPlayers.remove(target.getName());
        players.set("players." + playeruuid + ".players-ignored", ignoredPlayers);
        players.save();

        if (players.getStringList("players." + playeruuid + ".players-ignored").isEmpty()){
            players.set("players." + uuid, null);
            players.save();

        }

    }
}
