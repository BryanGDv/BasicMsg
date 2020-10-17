package code.modules;

import code.Manager;
import code.CacheManager;
import code.utils.Configuration;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class IgnoreMethod {

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


    public void add(CommandSender sender, Player player) {

        Player you = (Player) sender;
        UUID uuid = you.getUniqueId();


        List<String> ignoredPlayers;

        if (ignorelist.get(uuid) == null) {
            ignoredPlayers = new ArrayList<>();
        } else {
            ignoredPlayers = ignorelist.get(uuid);
        }

        ignoredPlayers.add(player.getName());
        ignorelist.put(uuid, ignoredPlayers);
        players.set("players." + uuid + ".players-ignored", ignoredPlayers);

        players.save();


    }

    public void remove(CommandSender sender, Player player){

        Player you = (Player) sender;
        UUID uuid = you.getUniqueId();

        List<String> ignoredPlayers = ignorelist.get(uuid);

        ignoredPlayers.remove(player.getName());
        players.set("players." + uuid + ".players-ignored", ignoredPlayers);
        players.save();

        if (players.getStringList("players." + uuid + ".players-ignored").isEmpty()){
            players.set("players." + uuid, null);
            players.save();
        }

    }
}
